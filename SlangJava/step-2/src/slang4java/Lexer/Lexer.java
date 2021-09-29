package slang4java.Lexer;

public class Lexer {

    String iExpr;
    int index;
    double number;
    private int length;

    public Lexer(String iExpr) {
        this.iExpr = iExpr;
        length = iExpr.length();
        index = 0;
    }


    public TOKEN getToken() {
        TOKEN tok = TOKEN.ILLEGAL_TOK;

        while (index < length && (iExpr.toCharArray()[index] == '' ||
                iExpr.toCharArray()[index] == '\t'))
            index++;

        // if end of string
        if (index == length) {
            return TOKEN.TOK_NULL;
        }

        switch (iExpr.toCharArray()[index]) {
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
                tok = TOKEN.TOK_OPREN;
                index++;
                break;
            }
            case ')' -> {
                tok = TOKEN.TOK_CPREN;
                index++;
                break;
            }
            case Character.isDigit(iExpr.toCharArray()[index]) ->{
                String str = "";
                while (index < length && (iExpr.toCharArray()[index] == '0'
                        || iExpr.toCharArray()[index] == '1'
                        || iExpr.toCharArray()[index] == '2'
                        || iExpr.toCharArray()[index] == '3'
                        || iExpr.toCharArray()[index] == '4'
                        || iExpr.toCharArray()[index] == '5'
                        || iExpr.toCharArray()[index] == '6'
                        || iExpr.toCharArray()[index] == '7'
                        || iExpr.toCharArray()[index] == '8'
                        || iExpr.toCharArray()[index] == '9')) {
                    str += (iExpr.toCharArray()[index]);
                    index++;
                }
                number = Double.parseDouble(str);
                tok = TOKEN.TOK_DOUBLE;
            }

            default -> throw new IllegalStateException("Unexpected value: " + iExpr.toCharArray()[index]);
        }


        return tok;

    }

    public double getNumber(){
        return number;
    }
}
