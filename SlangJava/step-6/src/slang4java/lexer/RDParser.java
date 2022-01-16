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


    public AbstractExpression BinaryExpression(ProcedureBuilder pb) {

        TOKEN l_token;
        AbstractExpression ret_value = LexicalExpression(pb);
        while (currentToken == TOKEN.TOK_AND || currentToken == TOKEN.TOK_OR) {
            l_token = currentToken;
            currentToken = getToken();
            AbstractExpression expression2 = LexicalExpression(pb);
            ret_value = new LogicalExpression(ret_value, expression2, l_token);
        }
        return ret_value;
    }


    private AbstractExpression LexicalExpression(ProcedureBuilder pb) {
        TOKEN l_token;
        AbstractExpression ret_value = Expression(pb);
        while (
            currentToken == TOKEN.TOK_GT ||
                currentToken == TOKEN.TOK_GTE ||
                currentToken == TOKEN.TOK_LT ||
                currentToken == TOKEN.TOK_LTE ||
                currentToken == TOKEN.TOK_EQ ||
                currentToken == TOKEN.TOK_NEQ
        ) {
            l_token = currentToken;
            currentToken = getToken();
            AbstractExpression expression2 = Expression(pb);
            RelationalOperator operator = getRelationalOperator(l_token);
            ret_value = new RelationalExpression(ret_value, expression2, operator);
        }
        return ret_value;
    }


    private AbstractExpression Expression(ProcedureBuilder ctx) {
        TOKEN l_token;
        AbstractExpression returnValue = Term(ctx);

        while (currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB) {
            l_token = currentToken;
            currentToken = getToken();
            AbstractExpression e1 = Term(ctx);
            if (l_token == TOKEN.TOK_PLUS) {
                returnValue = new BinaryPlus(returnValue, e1);
            } else {
                returnValue = new BinaryMinus(returnValue, e1);
            }
        }
        return returnValue;
    }

    private AbstractExpression Term(ProcedureBuilder ctx) {
        TOKEN l_token;
        AbstractExpression returnValue = null;
        try {
            returnValue = Factor(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (currentToken == TOKEN.TOK_MUL || currentToken == TOKEN.TOK_DIV) {
            l_token = currentToken;
            currentToken = getToken();
            AbstractExpression e1 = Term(ctx);
            if (l_token == TOKEN.TOK_MUL) {
                returnValue = new Mul(returnValue, e1);
            } else {
                returnValue = new Div(returnValue, e1);
            }

        }
        return returnValue;

    }

    private AbstractExpression Factor(ProcedureBuilder ctx) throws Exception {
        TOKEN l_token;
        AbstractExpression returnValue = null;

        if (currentToken == TOKEN.TOK_NUMERIC) {
            returnValue = new NumericConstant(getNumber());
            currentToken = getToken();

        } else if (currentToken == TOKEN.TOK_STRING) {
            returnValue = new StringLiteral(getString());
            currentToken = getToken();

        } else if (currentToken == TOKEN.TOK_BOOL_FALSE || currentToken == TOKEN.TOK_BOOL_TRUE) {
            returnValue = new BooleanConstant(currentToken == TOKEN.TOK_BOOL_TRUE ? true : false);
            currentToken = getToken();

        } else if (currentToken == TOKEN.TOK_OPREN) {
            currentToken = getToken();

            returnValue = BinaryExpression(ctx);
            if (currentToken != TOKEN.TOK_CPREN)
                throw syntaxError("Missing Closing Parens");

            currentToken = getToken();

        } else if (currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB) {

            l_token = currentToken;
            currentToken = getToken();
            returnValue = Factor(ctx);

            if (l_token == TOKEN.TOK_PLUS)
                returnValue = new UnaryPlus(returnValue);
            else
                returnValue = new UnaryMinus(returnValue);

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

    public ArrayList<Statement> Parse(ProcedureBuilder ctx) throws Exception {
        getNext();
        return statementList(ctx);
    }

    public TModule doParse() throws Exception {
        ProcedureBuilder procedureBuilder = new ProcedureBuilder("MAIN", new COMPILATION_CONTEXT());
        ArrayList<Statement> statements = Parse(procedureBuilder);
        for (Statement s : statements) {
            procedureBuilder.addStatement(s);
        }
        Procedure procedure = procedureBuilder.GetProcedure();
        prog.add(procedure);
        return prog.GetProgram();
    }

    private ArrayList<Statement> statementList(ProcedureBuilder ctx) throws Exception {
        ArrayList<Statement> arr = new ArrayList<>();
        while (
            (currentToken != TOKEN.TOK_ELSE) && (currentToken != TOKEN.TOK_ENDIF) &&
                (currentToken != TOKEN.TOK_WEND) && (currentToken != TOKEN.TOK_NULL)
        ) {

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
                returnValue = parseVariableDeclStatement(ctx);
                getNext();
                return returnValue;
            }

            case TOK_PRINT -> {
                try {
                    returnValue = parsePrintStatement(ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getNext();
            }
            case TOK_PRINTLN -> {
                try {
                    returnValue = parsePrintLnStatement(ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getNext();
            }

            case TOK_UNQUOTED_STRING -> {
                returnValue = parseAssignmentStatement(ctx);
                getNext();
                return returnValue;
            }

            case TOK_IF -> {
                returnValue = parseIfStatement(ctx);
                getNext();
                return returnValue;
            }

            case TOK_WHILE -> {
                returnValue = parseWhileStatement(ctx);
                getNext();
                return returnValue;

            }

            default -> {
                throw new Exception("Invalid Statement");
            }
        }
        return returnValue;
    }


    private Statement parseVariableDeclStatement(ProcedureBuilder ctx) throws Exception {
        TOKEN tok = currentToken;
        getNext();
        if (currentToken == TOKEN.TOK_UNQUOTED_STRING) {
            SymbolInfo symb = new SymbolInfo();
            symb.SymbolName = getString();
            symb.Type = (tok == TOKEN.TOK_VAR_BOOL) ?
                TypeInfo.TYPE_BOOL : (tok == TOKEN.TOK_VAR_NUMBER) ?
                TypeInfo.TYPE_NUMERIC : TypeInfo.TYPE_STRING;

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

    private Statement parseAssignmentStatement(ProcedureBuilder ctx) throws Exception {
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
            String errorMessage = "Slang Compile Time Error - Missing '=' at line: " + getCurrentLine(getIndex());
            throw new Exception(errorMessage);
        }

        getNext();
        AbstractExpression expression = Expression(ctx);

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


    private Statement parsePrintLnStatement(ProcedureBuilder ctx) throws Exception {
        getNext();
        AbstractExpression a = Expression(ctx);
        if (currentToken != TOKEN.TOK_SEMI)
            throw new Exception("expected ; ");

        return new PrintStatement(a);
    }


    private Statement parsePrintStatement(ProcedureBuilder ctx) throws Exception {
        getNext();
        AbstractExpression a = Expression(ctx);

        a.TypeCheck(ctx.getContext());
        if (currentToken != TOKEN.TOK_SEMI)
            throw syntaxError("expected ; ");

        return new PrintLineStatement(a);
    }


    private Statement parseIfStatement(ProcedureBuilder pb) throws Exception {
        getNext();
        ArrayList<Statement> trueStatements = null;
        ArrayList<Statement> falseStatements = null;
        AbstractExpression condition = BinaryExpression(pb);

        if (pb.typeCheck(condition) != TypeInfo.TYPE_BOOL)
            throw new Exception("expects boolen expression");


        if (currentToken != TOKEN.TOK_THEN)
            throw syntaxError("then expected");

        getNext();
        trueStatements = statementList(pb);

        if (currentToken == TOKEN.TOK_ENDIF)
            return new IfStatement(condition, trueStatements, falseStatements);

        if (currentToken != TOKEN.TOK_ELSE)
            throw syntaxError("else expected");

        getNext();
        falseStatements = statementList(pb);

        if (currentToken != TOKEN.TOK_ENDIF)
            throw syntaxError("END IF expected");

        return new IfStatement(condition, trueStatements, falseStatements);

    }

    private Statement parseWhileStatement(ProcedureBuilder pb) throws Exception {
        getNext();
        AbstractExpression condition = BinaryExpression(pb);

        if (pb.typeCheck(condition) != TypeInfo.TYPE_BOOL)
            throw new Exception("expects boolen expression");

        ArrayList<Statement> statements = statementList(pb);
        if (currentToken != TOKEN.TOK_WEND)
            throw syntaxError("expected wend");

        return new WhileStatement(condition, statements);

    }

    protected TOKEN getNext() {
        lastToken = currentToken;
        currentToken = getToken();
        return currentToken;
    }


    //convert lexical operator to relational operator
    private RelationalOperator getRelationalOperator(TOKEN operatorToken) {
        switch (operatorToken) {
            case TOK_LT:
                return RelationalOperator.TOK_LT;
            case TOK_LTE:
                return RelationalOperator.TOK_LTE;
            case TOK_GT:
                return RelationalOperator.TOK_GT;
            case TOK_GTE:
                return RelationalOperator.TOK_GTE;
            case TOK_NEQ:
                return RelationalOperator.TOK_NEQ;
            case TOK_EQ:
                return RelationalOperator.TOK_EQ;
            default:
                break;
        }

        return null;
    }

    private Exception syntaxError(String errorMsg) throws Exception {
        int index = getIndex();
        if (lastToken == TOKEN.TOK_STRING) {
            index -= getString().length();
        }
        String errorMessage = "Slang Compile Time Error - SYNTAX ERROR AT: "
            + getCurrentLine(index) + "\n" + errorMsg;
        return new Exception(errorMessage);
    }


}