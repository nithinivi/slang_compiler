package slang4java.Lexer;

import slang4java.OPERATOR;
import slang4java.expressions.BinaryExp;
import slang4java.expressions.Exp;
import slang4java.expressions.NumericConstant;
import slang4java.expressions.UnaryExp;

public class RDPaser extends Lexer{

    TOKEN currentToken ;

    public RDPaser(String iExpr) {
        super(iExpr);
    }

    public Exp callExpr(){
        currentToken = getToken();
        return Expr();

    }

    private Exp Expr() {
        TOKEN l_token;
        Exp returnValue = Term();

        while (currentToken==TOKEN.TOK_PLUS||currentToken==TOKEN.TOK_SUB){
            l_token = currentToken;
            currentToken = getToken();
            Exp e1 = Term();
            returnValue = new BinaryExp(returnValue , e1 ,
                    l_token == TOKEN.TOK_PLUS ? OPERATOR.PLUS : OPERATOR.MINUS);
        }
        return returnValue;
    }

    private Exp Term(){
        TOKEN l_token;
        Exp returnValue = null;
        try {
            returnValue = Factor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (currentToken==TOKEN.TOK_MUL||currentToken==TOKEN.TOK_DIV){
            l_token = currentToken;
            currentToken = getToken();
            Exp e1 = Term();
            returnValue = new BinaryExp(returnValue , e1 ,
                    l_token == TOKEN.TOK_MUL ? OPERATOR.MUL : OPERATOR.DIV);
        }
        return returnValue;

    }

    private Exp Factor() throws Exception {
        TOKEN l_token;
        Exp returnValue = null;
        if(currentToken == TOKEN.TOK_DOUBLE){
            returnValue = new NumericConstant(getNumber());
            currentToken = getToken();
        }
        else if (currentToken == TOKEN.TOK_OPREN){
            currentToken = getToken();
            returnValue = Expr();
            if(currentToken != TOKEN.TOK_CPREN){
                System.out.println("Missing Closing Parens");
                throw new Exception("Missing parens");
            }
            currentToken = getToken();

        }
        else if (currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB){
            l_token =   currentToken;
            currentToken = getToken();
            returnValue = Factor();
            returnValue = new UnaryExp(returnValue , l_token==TOKEN.TOK_PLUS? OPERATOR.PLUS:OPERATOR.MINUS);
        }

        else{
            System.out.println("Illegal Token");
            throw new Exception("Illegal Token");
        }

        return returnValue;
    }


}
