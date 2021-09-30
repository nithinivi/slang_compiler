package slang4java.lexer;

import java.util.Locale;

public class Lexer {

    String iExpr;
    int index;
    double number;
    private int length;
    private ValueTable[] _val = null;
    private String lastStr;


    public Lexer(String iExpr) {
        this.iExpr = iExpr;
        length = iExpr.length();
        index = 0;

        _val = new ValueTable[2];
        _val[0] = new ValueTable(TOKEN.TOK_PRINT, "PRINT");
        _val[1] = new ValueTable(TOKEN.TOK_PRINTLN, "PRINTLINE");
    }

    public TOKEN getToken() {
        TOKEN tok = TOKEN.ILLEGAL_TOKEN;

        while (index < length && iExpr.toCharArray()[index] == ' ' || index < length && iExpr.toCharArray()[index] == '\t')
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
            case ';' -> {
                tok = TOKEN.TOK_SEMI;
                index++;
                break;
            }
            default -> {

                if (Character.isDigit(iExpr.toCharArray()[index])) {
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
                } else if (Character.isLetter(iExpr.toCharArray()[index])) {
                    String temp = "";
                    while (index < length && Character.isLetterOrDigit(iExpr.toCharArray()[index]) ||
                            iExpr.toCharArray()[index] == '_') {
                        temp += String.valueOf(iExpr.toCharArray()[index]);
                        index++;
                    }

                    temp = temp.toUpperCase();

                    for (int i = 0; i < this._val.length; ++i) {
                        if (_val[i].Value.compareTo(temp) == 0) {
                            return _val[i].tok;
                        }
                    }
                    this.lastStr = temp;
                    return TOKEN.TOK_UNQUOTED_STRING;


                } else {
                    throw new IllegalStateException("Unexpected value: " + iExpr.toCharArray()[index]);
                }
                //            default ->
            }
        }

        return tok;

    }

    public double getNumber() {
        return number;
    }
}

class ValueTable {
    public TOKEN tok;
    public String Value;

    public ValueTable(TOKEN tok, String Value) {
        this.tok = tok;
        this.Value = Value;
    }
}

