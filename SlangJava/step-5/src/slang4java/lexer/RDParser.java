/*
 * <Expr> ::= <Term> | Term { + | - } <Expr>
 * <Term> ::= <Factor> | <Factor> { * | / } <Term>
 * <Factor>::= <number> | ( <expr> ) | { + | - } <factor>
 * */

package slang4java.lexer;

import slang4java.builder.ProcedureBuilder;
import slang4java.builder.TModuleBuilder;
import slang4java.complationUnits.TModule;
import slang4java.context.COMPILATION_CONTEXT;
import slang4java.expressions.*;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;
import slang4java.procedure.Procedure;
import slang4java.statements.*;
import slang4java.support.CParserException;
import slang4java.support.CSyntaxErrorLog;

import java.util.ArrayList;

public class RDParser extends Lexer {


    protected TOKEN currentToken;  // current token
    protected TOKEN lastToken;     // penultimate token
    TModuleBuilder prog = null;

    public RDParser(String iExpr) {
        super(iExpr);
        prog = new TModuleBuilder();

    }

    public Expression callExpr(ProcedureBuilder ctx) {
        currentToken = getToken();
        return Expr(ctx);

    }

    private Expression Expr(ProcedureBuilder ctx) {
        TOKEN l_token;
        Expression returnValue = Term(ctx);

        while (currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB) {
            l_token = currentToken;
            currentToken = getToken();
            Expression e1 = Term(ctx);
            if (l_token == TOKEN.TOK_PLUS) {
                returnValue = new BinaryPlus(returnValue, e1);
            } else {
                returnValue = new BinaryMinus(returnValue, e1);
            }
        }
        return returnValue;
    }

    private Expression Term(ProcedureBuilder ctx) {
        TOKEN l_token;
        Expression returnValue = null;
        try {
            returnValue = Factor(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (currentToken == TOKEN.TOK_MUL || currentToken == TOKEN.TOK_DIV) {
            l_token = currentToken;
            currentToken = getToken();
            Expression e1 = Term(ctx);
            if (l_token == TOKEN.TOK_MUL) {
                returnValue = new Mul(returnValue, e1);
            } else {
                returnValue = new Div(returnValue, e1);
            }

        }
        return returnValue;

    }

    private Expression Factor(ProcedureBuilder ctx) throws Exception {
        TOKEN l_token;
        Expression returnValue = null;

        if (currentToken == TOKEN.TOK_NUMERIC) {
            returnValue = new NumericConstant(getNumber());
            currentToken = getToken();
        } else if (currentToken == TOKEN.TOK_STRING) {
            returnValue = new StringLiteral(getString());
            currentToken = getToken();

        } else if (currentToken == TOKEN.TOK_BOOL_FALSE || currentToken == TOKEN.TOK_BOOL_TRUE) {
            returnValue = new BooleanConstant(
                currentToken == TOKEN.TOK_BOOL_TRUE ? true : false
            );
            currentToken = getToken();
        } else if (currentToken == TOKEN.TOK_OPREN) {
            currentToken = getToken();
            returnValue = Expr(ctx);
            if (currentToken != TOKEN.TOK_CPREN) {
                System.out.println("Missing Closing Parens");
                throw new Exception("Missing parens");
            }
            currentToken = getToken();

        } else if (currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB) {

            l_token = currentToken;
            currentToken = getToken();
            returnValue = Factor(ctx);

            if (l_token == TOKEN.TOK_PLUS) {
                returnValue = new UnaryPlus(returnValue);
            } else {
                returnValue = new UnaryMinus(returnValue);
            }


        } else if (currentToken == TOKEN.TOK_UNQUOTED_STRING) {
            String str = getString();
            SymbolInfo info = ctx.getSymbolTable().getSymbol(str);
            if (info == null)
                throw new Exception("unidentifed symbol");
            getNext();
            returnValue = new Variable(info);

        } else {
            System.out.println("Illegal Token");
            throw new Exception("Illegal Token");
        }

        return returnValue;
    }

    public ArrayList Parse(ProcedureBuilder ctx) throws Exception {
        getNext();
        return statementList(ctx);
    }

    public TModule doParse() throws Exception {
        ProcedureBuilder procedureBuilder = new ProcedureBuilder("MAIN",
            new COMPILATION_CONTEXT());
        ArrayList statements = Parse(procedureBuilder);
        for (Object s : statements) {
            Statement statement = (Statement) s;
            procedureBuilder.addStatement(statement);
        }
        Procedure procedure = procedureBuilder.GetProcedure();
        prog.add(procedure);
        return prog.GetProgram();
    }

    private ArrayList statementList(ProcedureBuilder ctx) throws Exception {
        ArrayList arr = new ArrayList();
        while (currentToken != TOKEN.TOK_NULL) {
            Statement temp = Statement(ctx);
            if (temp != null) {
                arr.add(temp);
            }
        }
        return arr;
    }

    private Statement Statement(ProcedureBuilder ctx) throws Exception {
        Statement returnValue = null;
        switch (currentToken) {
            case TOK_VAR_BOOL, TOK_VAR_NUMBER, TOK_VAR_STRING -> {
                returnValue = ParseVariableDeclStatement(ctx);
                getNext();
                return returnValue;
            }

            case TOK_PRINT -> {
                try {
                    returnValue = ParsePrintStatement(ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getNext();
            }
            case TOK_PRINTLN -> {
                try {
                    returnValue = ParsePrintLnStatement(ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getNext();
            }

            case TOK_UNQUOTED_STRING -> {
                returnValue = ParseAssignmentStatement(ctx);
                getNext();
                return returnValue;
            }

            default -> {
                throw new Exception("Invalid Statement");
            }
        }
        return returnValue;
    }


    private Statement ParseVariableDeclStatement(ProcedureBuilder ctx) throws Exception {
        TOKEN tok = currentToken;
        getNext();
        if (currentToken == TOKEN.TOK_UNQUOTED_STRING) {
            SymbolInfo symb = new SymbolInfo();
            symb.SymbolName = getString();
            symb.Type = (tok == TOKEN.TOK_VAR_BOOL)
                ? TypeInfo.TYPE_BOOL : (tok == TOKEN.TOK_VAR_NUMBER)
                ? TypeInfo.TYPE_NUMERIC : TypeInfo.TYPE_STRING;

            getNext();
            if (currentToken == TOKEN.TOK_SEMI) {
                // add to symbol table for type analysis
                ctx.getSymbolTable().Add(symb);
                return new VariableDeclStatement(symb);
            } else {
                CSyntaxErrorLog.AddLine("; expected");
                CSyntaxErrorLog.AddLine(getCurrentLine(getIndex()));
                throw new CParserException(-100, ", or ; expected", getIndex());
            }
        } else {
            CSyntaxErrorLog.AddLine(" invalid variable declaration ");
            CSyntaxErrorLog.AddLine(getCurrentLine(getIndex()));
            throw new CParserException(-100, ", or ; expected", getIndex());
        }
    }

    private Statement ParseAssignmentStatement(ProcedureBuilder ctx) throws Exception {
        // Retrive the variable and look if up
        // the symbol table. if not found throw Exception

        String variable = getString();
        SymbolInfo symbol = ctx.getSymbolTable().getSymbol(variable);

        if (symbol == null) {

            CSyntaxErrorLog.AddLine("Variable not found " + getNumber());
            CSyntaxErrorLog.AddLine(getCurrentLine(getIndex()));
            throw new CParserException(-100, "Variable not found", getIndex());
        }

        // token should be assignment
        getNext();
        if (currentToken != TOKEN.TOK_ASSIGN) {
            String errorMessage = "Slang Compile Time Error - Missing '=' at line: "
                + getCurrentLine(getIndex());
            throw new Exception(errorMessage);
        }

        getNext();
        Expression expression = Expr(ctx);

        //type check
        if (expression.TypeCheck(ctx.getContext()) != symbol.Type) {
            throw new Exception("Type mismatch is assignment");
        }

        if (currentToken != TOKEN.TOK_SEMI) {
            CSyntaxErrorLog.AddLine(" ; expected ");
            CSyntaxErrorLog.AddLine(getCurrentLine(getIndex()));
            throw new CParserException(-100, "; expected", getIndex());
        }

        return new AssignmentStatement(symbol, expression);
    }


    private Statement ParsePrintLnStatement(ProcedureBuilder ctx) throws Exception {
        getNext();
        Expression a = Expr(ctx);
        if (currentToken != TOKEN.TOK_SEMI) {
            throw new Exception("expected ; ");
        }
        return new PrintStatement(a);
    }


    private Statement ParsePrintStatement(ProcedureBuilder ctx) throws Exception {
        getNext();
        Expression a = Expr(ctx);

        a.TypeCheck(ctx.getContext());
        if (currentToken != TOKEN.TOK_SEMI) {

            throw new Exception("expected ; ");
        }
        return new PrintLineStatement(a);
    }

    protected TOKEN getNext() {
        lastToken = currentToken;
        currentToken = getToken();
        return currentToken;
    }


}