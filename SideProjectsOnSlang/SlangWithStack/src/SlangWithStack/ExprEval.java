
package SlangWithStack;

import java.util.TooManyListenersException;

public class ExprEval {
    public enum TOKEN {
        ILLEGAL_TOKEN,
        TOK_PLUS,
        TOK_MUL,
        TOK_DIV,
        TOK_SUB,
        TOK_OPAREN,
        TOK_CPAREN,
        TOK_DOUBLE,
        TOK_NULL
    }

    public class Lexer {
        private String IExpr;
        private int index = 1;
        private int length;
        private double number;

        public Lexer(String IExpr) {
            this.IExpr = IExpr;
            this.length = IExpr.length();
            this.index = 0;
        }

        private char getCharAtIndex() {
            return getCharAtIndex();
        }

        public TOKEN getToken() {

            TOKEN tok = TOKEN.ILLEGAL_TOKEN;
            while (index < length && (getCharAtIndex() == ' ' || getCharAtIndex() == '\t'))
                index++;
            if (index == length)
                return TOKEN.TOK_NULL;

            switch (getCharAtIndex()) {
                case '+' -> {
                    tok = TOKEN.TOK_PLUS;
                    index++;
                    break;
                }
                case '-' -> {
                    tok = TOKEN.TOK_SUB;
                    index++;
                    break;
                }
                case '*' -> {
                    tok = TOKEN.TOK_MUL;
                    index++;
                    break;
                }
                case '/' -> {
                    tok = TOKEN.TOK_DIV;
                    index++;
                    break;
                }
                case '(' -> {
                    tok = TOKEN.TOK_OPAREN;
                    index++;
                    break;
                }
                case ')' -> {
                    tok = TOKEN.TOK_CPAREN;
                    index++;
                    break;
                }
                default -> {
                    if (Character.isDigit(getCharAtIndex())) {
                        String str = "";
                        while (index < length && (getCharAtIndex() == '0'
                                || getCharAtIndex() == '1'
                                || getCharAtIndex() == '2'
                                || getCharAtIndex() == '3'
                                || getCharAtIndex() == '4'
                                || getCharAtIndex() == '5'
                                || getCharAtIndex() == '6'
                                || getCharAtIndex() == '7'
                                || getCharAtIndex() == '8'
                                || getCharAtIndex() == '9')) {
                            str += (getCharAtIndex());
                            index++;
                        }
                        number = Double.parseDouble(str);
                        tok = TOKEN.TOK_DOUBLE;
                    } else {
                        throw new IllegalStateException("Unexpected value: " + getCharAtIndex());
                    }
                }

            }
            return tok;
        }

        public double getNumber(){
            return number;
        }

    }


    class Stack{
        double[] stk;
        int top_stack =0;

        public Stack() {
            this.stk = new double[256];
            this.top_stack =0;
        }

        public void clear(){
            top_stack=0;
        }

        public void push(double dbl) {
            if(top_stack==255){
                System.out.println("Stack Overflow");
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            stk[top_stack++] = dbl;
        }
        public double pop() {
            if(top_stack==0){
                System.out.println("Stack underflow");
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return stk[--top_stack];
        }
    }

    class RDParser extends Lexer{
        TOKEN currentToken;
        Stack ValueStack = new Stack();

        public RDParser(String IExpr) {
            super(IExpr);
        }


        public double callExpr() {
            ValueStack.clear();
            currentToken = getToken();
            Expr();
            double nd = ValueStack.pop();
            return nd;
        }

        private void Expr() {
            TOKEN l_token;
            Term();
            while (currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB) {
                l_token = currentToken;
                currentToken = getToken();
                Expr();
                double x = ValueStack.pop();
                double y = ValueStack.pop();
                ValueStack.push((l_token == TOKEN.TOK_PLUS) ? x+y : x-y);
            }

        }

        private void Term() {
            TOKEN l_token;
            Factor();

            while (currentToken == TOKEN.TOK_MUL || currentToken == TOKEN.TOK_DIV) {
                l_token = currentToken;
                currentToken = getToken();
                Term();
                double x = ValueStack.pop();
                double y = ValueStack.pop();
                ValueStack.push((l_token == TOKEN.TOK_PLUS) ? x*y : x/y);
            }
        }

        private void Factor() {
            TOKEN l_token;
            if (currentToken ==TOKEN.TOK_DOUBLE ){
                // push the values to stack
                ValueStack.push(getNumber());
                currentToken = getToken();

            }
            else if(currentToken == TOKEN.TOK_OPAREN){
                // at this point another expression can be found
                // can be containing other expressions
                Expr();
                if(currentToken != TOKEN.TOK_CPAREN){
                    System.out.println("Expected  closing parenthesis");
                    try {
                        throw new Exception("Expected  closing parenthesis");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB){
                l_token = currentToken;
                currentToken = getToken();
                // there are chances that the next token can be illegal
                // what happens if the text is (-) , -() ? is this edge condition
                //
                Factor();
                double x = ValueStack.pop();
                if(l_token == TOKEN.TOK_SUB){x = -x;}
                ValueStack.push(x);
            }
            else{
                System.out.println("Illegal Token");
                try {
                    throw new Exception("Illegal Token");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }





}
