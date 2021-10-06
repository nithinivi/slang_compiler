import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

enum OPERATOR {
    ILLEGAL,
    PLUS,
    MINUS,
    MUL,
    DIV
}

class RUNTIME_CONTEXT {
    public RUNTIME_CONTEXT() {
    }
}


abstract class Expr {
    public abstract double accept(IExprVisitor exprVisitor);
}

class NumericConstant extends Expr {

    private @Getter double value;

    //public double NUM { get { return _value; } }

    public NumericConstant(double _value) {
        value = _value;
    }


    @Override
    public double accept(IExprVisitor exprVisitor) {
        return exprVisitor.Visit(this);
    }
}

@Getter
@Setter
@AllArgsConstructor
class BinaryExpr extends Expr {
    public Expr left;
    public Expr right;
    public OPERATOR op;

    @Override
    public double accept(IExprVisitor exprVisitor) {
        return exprVisitor.Visit(this);
    }
}


class UnaryExpr extends Expr {
    public Expr right;
    public OPERATOR op;

    public UnaryExpr(Expr r, OPERATOR op) {
        right = new BinaryExpr(new NumericConstant(0),
                r, op);
    }

    @Override
    public double accept(IExprVisitor exprVisitor) {
        return exprVisitor.Visit(this);
    }
}

interface IExprVisitor {
    double Visit(NumericConstant num);
    double Visit(BinaryExpr bin);
    double Visit(UnaryExpr un);
}


class StackEvaluator implements IExprVisitor {
    private Stack<Double> evalStack = new Stack<Double>();

    public double getValue() {
        return evalStack.pop();
    }

    public StackEvaluator(Stack<Double> evalStack) {
        this.evalStack.clear();
    }

    @Override
    public double Visit(NumericConstant num) {
        evalStack.push(num.getValue());
        return 0;
    }

    @Override
    public double Visit(BinaryExpr bin) {
        bin.left.accept(this);
        bin.right.accept(this);


        if (bin.op == OPERATOR.PLUS) {
            evalStack.push(evalStack.pop() + evalStack.pop());
        } else if (bin.op == OPERATOR.MINUS) {
            evalStack.push(evalStack.pop() - evalStack.pop());
        } else if (bin.op == OPERATOR.MUL) {
            evalStack.push(evalStack.pop() / evalStack.pop());
        } else if (bin.op == OPERATOR.DIV) {
            evalStack.push(evalStack.pop() * evalStack.pop());
        }

        return Double.NaN;
    }

    @Override
    public double Visit(UnaryExpr un) {
        un.right.accept(this);

        if (un.op == OPERATOR.PLUS) {
            evalStack.push(evalStack.pop());
        } else if (un.op == OPERATOR.MINUS) {
            evalStack.push(-evalStack.pop());

        }
        return Double.NaN;
    }

}


class TreeEvaluatorVisitor implements IExprVisitor{

    @Override
    public double Visit(NumericConstant num) {
        return num.getValue();
    }

    @Override
    public double Visit(BinaryExpr bin) {
        if (bin.op == OPERATOR.PLUS)
            return bin.left.accept(this) + bin.right.accept(this);
        else if (bin.op == OPERATOR.MUL)
            return bin.left.accept(this) * bin.right.accept(this);
        else if (bin.op == OPERATOR.DIV)
            return bin.left.accept(this) / bin.right.accept(this);
        else if (bin.op == OPERATOR.MINUS)
            return bin.left.accept(this) - bin.right.accept(this);

        return Double.NaN;
    }

    @Override
    public double Visit(UnaryExpr un) {
        if (un.op == OPERATOR.PLUS)
            return +un.right.accept(this);
        else if (un.op == OPERATOR.MINUS)
            return -un.right.accept(this);
        return Double.NaN;
    }
}

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


class RDParser extends Lexer {
    TOKEN currentToken;

    public RDParser(String IExpr) {
        super(IExpr);
    }


    public Expr callExpr() {
        currentToken = getToken();
        return Expr();
    }

    private Expr Expr() {
        // 2 * (2 + 2)
        TOKEN l_token;
        Expr returnValue = Term();
        while (currentToken == TOKEN.TOK_PLUS || currentToken == TOKEN.TOK_SUB) {
            l_token = currentToken;
            currentToken = getToken();
            Expr e1 = Expr();
            returnValue = new BinaryExpr(returnValue, e1,
                    l_token == TOKEN.TOK_PLUS ? OPERATOR.PLUS : OPERATOR.MINUS);
        }

        return returnValue;

    }

    private Expr Term() {
        //2*2
        TOKEN l_token;
        Expr returnValue = Factor();

        while (currentToken == TOKEN.TOK_MUL || currentToken == TOKEN.TOK_DIV) {
            l_token = currentToken;
            currentToken = getToken();
            Expr e1 = Term();
            returnValue = new BinaryExpr(returnValue, e1,
                    l_token == TOKEN.TOK_MUL ? OPERATOR.MUL : OPERATOR.DIV);
        }
        return returnValue;
    }

    private Expr Factor() {
        TOKEN l_token;
        Expr returnValue = null;

        if (currentToken == TOKEN.TOK_DOUBLE) {
            // push the values to stack
            double number = getNumber();
            returnValue = new NumericConstant(number);
            currentToken = getToken();

        } else if (currentToken == TOKEN.TOK_OPAREN) {
            // at this point another expression can be found
            // can be containing other expressions
            // test1 ->  1 * (* 2)
            // test2 -> 1 (*2)
            currentToken = getToken();
            // this is hence the next token was not taken
            returnValue = Expr();
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
            returnValue = Factor();
            return new UnaryExpr(returnValue,
                    l_token == TOKEN.TOK_PLUS ? OPERATOR.PLUS : OPERATOR.MINUS);
        } else {
            System.out.println("Illegal Token");
            try {
                throw new Exception("Illegal Token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }
}

    class EntryPoint {

        public static void main(String[] args) {
            String stmt = "2*(2+2)";
            RDParser parser = new RDParser(stmt);
            Expr result = parser.callExpr();
            System.out.println(result.accept(null));

        }
    }



