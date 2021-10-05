
package SlangWithStack;

import java.util.TooManyListenersException;

enum TOKEN {
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

class Lexer {
    private String IExpr;
    private int index;
    private int length;
    private double number;

    public Lexer(String IExpr) {
        this.IExpr = IExpr;
        this.length = IExpr.length();
        this.index = 0;
    }

    private char getCharAtIndex() {
        return IExpr.toCharArray()[index];
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
            }
            case '-' -> {
                tok = TOKEN.TOK_SUB;
                index++;
            }
            case '*' -> {
                tok = TOKEN.TOK_MUL;
                index++;
            }
            case '/' -> {
                tok = TOKEN.TOK_DIV;
                index++;
            }
            case '(' -> {
                tok = TOKEN.TOK_OPAREN;
                index++;
            }
            case ')' -> {
                tok = TOKEN.TOK_CPAREN;
                index++;
            }
            default -> {
                if (Character.isDigit(getCharAtIndex())) {
                    StringBuilder str = new StringBuilder();
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
                        str.append(getCharAtIndex());
                        index++;
                    }
                    number = Double.parseDouble(str.toString());
                    tok = TOKEN.TOK_DOUBLE;
                } else {
                    throw new IllegalStateException("Unexpected value: " + getCharAtIndex());
                }
            }

        }
        return tok;
    }

    public double getNumber() {
        return number;
    }

}


class Stack {
    double[] stk;
    int top_stack;

    public Stack() {
        this.stk = new double[256];
        this.top_stack = 0;
    }

    public void clear() {
        top_stack = 0;
    }

    public void push(double dbl) {
        if (top_stack == 255) {
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
        if (top_stack == 0) {
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

class RDParser extends Lexer {
    TOKEN currentToken;
    Stack ValueStack = new Stack();

    public RDParser(String IExpr) {
        super(IExpr);
    }


    public double callExpr() {
        ValueStack.clear();
        currentToken = getToken();
        Expr();
        return ValueStack.pop();
    }

    private void Expr() {
        // 2 * (2 + 2)
        TOKEN l_token;
        Term();
        while (currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB) {
            l_token = currentToken;
            currentToken = getToken();
            Expr();
            double x = ValueStack.pop();
            double y = ValueStack.pop();
            ValueStack.push((l_token == TOKEN.TOK_PLUS) ? x + y : x - y);
        }

    }

    private void Term() {
        //2*2
        TOKEN l_token;
        Factor();

        while (currentToken == TOKEN.TOK_MUL || currentToken == TOKEN.TOK_DIV) {
            l_token = currentToken;
            currentToken = getToken();
            Term();
            double x = ValueStack.pop();
            double y = ValueStack.pop();
            ValueStack.push((l_token == TOKEN.TOK_MUL) ? x * y : x / y);
        }
    }

    private void Factor() {
        TOKEN l_token;
        if (currentToken == TOKEN.TOK_DOUBLE) {
            // push the values to stack
            double number = getNumber();
            ValueStack.push(number);
            currentToken = getToken();

        } else if (currentToken == TOKEN.TOK_OPAREN) {
            // at this point another expression can be found
            // can be containing other expressions
            // test1 ->  1 * (* 2)
            // test2 -> 1 (*2)
            currentToken = getToken();
            // this is hence the next token was not taken
            Expr();
            if (currentToken != TOKEN.TOK_CPAREN) {
                System.out.println("Expected  closing parenthesis");
                try {
                    throw new Exception("Expected  closing parenthesis");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB) {
            l_token = currentToken;
            currentToken = getToken();
            // there are chances that the next token can be illegal
            // what happens if the text is (-) , -()  ? is this edge condition
            //
            Factor();
            double x = ValueStack.pop();
            if (l_token == TOKEN.TOK_SUB) {
                x = -x;
            }
            ValueStack.push(x);
        } else {
            System.out.println("Illegal Token");
            try {
                throw new Exception("Illegal Token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

class CallSlang {

    public static void main(String[] args) {
        String stmt = "2*(2+2)";
        RDParser parser = new RDParser(stmt);
        double result = parser.callExpr();
        System.out.println(result);

    }
}



