package slang4java.lexer;

public class Lexer {

    String expression;
    int index;
    double number;
    private int length;
    private String string;

    private ValueTable[] keywords = null;

    public Lexer(String expression) {
        this.expression = expression;
        this.length = this.expression.length();
        this.index = 0;

        this.keywords = new ValueTable[7];
        this.keywords[0] = new ValueTable(TOKEN.TOK_BOOL_FALSE, "FALSE");
        this.keywords[1] = new ValueTable(TOKEN.TOK_BOOL_TRUE, "TRUE");
        this.keywords[2] = new ValueTable(TOKEN.TOK_VAR_STRING, "STRING");
        this.keywords[3] = new ValueTable(TOKEN.TOK_VAR_BOOL, "BOOLEAN");
        this.keywords[4] = new ValueTable(TOKEN.TOK_VAR_NUMBER, "NUMERIC");
        this.keywords[5] = new ValueTable(TOKEN.TOK_PRINT, "PRINT");
        this.keywords[6] = new ValueTable(TOKEN.TOK_PRINTLN, "PRINTLINE");

        this.keywords[7] = new ValueTable(TOKEN.TOK_IF, "IF");
        this.keywords[8] = new ValueTable(TOKEN.TOK_THEN, "THEN");
        this.keywords[9] = new ValueTable(TOKEN.TOK_ELSE, "ELSE");
        this.keywords[10] = new ValueTable(TOKEN.TOK_ENDIF, "ENDIF");
        this.keywords[11] = new ValueTable(TOKEN.TOK_WHILE, "WHILE");
        this.keywords[12] = new ValueTable(TOKEN.TOK_WEND, "WEND");
    }

    public int getIndex() {
        return index;
    }

    public double getNumber() {
        return number;
    }

    public String getString() {
        return string;
    }

    public TOKEN getToken() {
        TOKEN tok;
        boolean restart;

        do {
            restart = false;
            tok = TOKEN.ILLEGAL_TOKEN;

            while (index < length && (
                expression.charAt(index) == ' ' ||
                    expression.charAt(index) == '\t' ||
                    System.lineSeparator().contains(
                        String.valueOf(expression.charAt(index)))))
                index++;

            // if end of string
            if (index == length) {
                return TOKEN.TOK_NULL;
            }
            switch (expression.charAt(index)) {
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
                    if (expression.charAt(index + 1) == '/') {
                        skipToNextLine();
                        restart = true;
                    } else {
                        tok = TOKEN.TOK_DIV;
                        index++;
                    }
                }
                case '=' -> {
                    if (expression.charAt(index + 1) == '=') {
                        tok = TOKEN.TOK_EQ;
                        index += 2;
                    } else {
                        tok = TOKEN.TOK_ASSIGN;
                        index++;
                    }
                }
                case '!' -> {
                    tok = TOKEN.TOK_NOT;
                }
                case '>' -> {
                    if (expression.charAt(index + 1) == '=') {
                        tok = TOKEN.TOK_GTE;
                        index += 2;
                    } else {
                        tok = TOKEN.TOK_GT;
                        index++;
                    }
                }
                case '<' -> {
                    if (expression.charAt(index + 1) == '=') {
                        tok = TOKEN.TOK_LTE;
                        index += 2;
                    } else if (expression.charAt(index + 1) == '>') {
                        tok = TOKEN.TOK_NEQ;
                        index += 2;
                    } else {
                        tok = TOKEN.TOK_LT;
                        index++;
                    }
                }
                case '&' -> {
                    if (expression.charAt(index + 1) == '&') {
                        tok = TOKEN.TOK_AND;
                        index += 2;
                    } else {
                        tok = TOKEN.ILLEGAL_TOKEN;
                        index++;
                    }
                }
                case '|' -> {
                    if (expression.charAt(index + 1) == '|') {
                        tok = TOKEN.TOK_OR;
                        index += 2;
                    } else {
                        tok = TOKEN.ILLEGAL_TOKEN;
                        index++;
                    }
                }
                case '(' -> {
                    tok = TOKEN.TOK_OPREN;
                    index++;
                }
                case ')' -> {
                    tok = TOKEN.TOK_CPREN;
                    index++;
                }
                case ';' -> {
                    tok = TOKEN.TOK_SEMI;
                    index++;
                }
                case '"' -> {
                    String tempString = "";
                    index++;

                    while (index < length && expression.charAt(index) != '"') {
                        tempString += expression.charAt(index);
                        index++;
                    }
                    if (index == length) {
                        tok = TOKEN.ILLEGAL_TOKEN;
                    } else {
                        index++;
                        string = tempString;
                        tok = TOKEN.TOK_STRING;

                    }
                    return tok;
                }

                default -> {

                    if (Character.isDigit(expression.charAt(index))) {

                        String tempString = "";
                        while (index < length && Character.isDigit(expression.charAt(index))
                        ) {
                            tempString += (expression.charAt(index));
                            index++;
                        }

                        // Cover the decimal points
                        if (expression.charAt(index) == '.') {
                            tempString += '.';
                            index++;
                            while (index < length && Character.isDigit(expression.charAt(index))) {
                                tempString += expression.charAt(index);
                                index++;
                            }
                        }

                        number = Double.parseDouble(tempString);
                        tok = TOKEN.TOK_NUMERIC;

                    } else if (Character.isLetter(expression.charAt(index))) {
                        String tempString = "";
                        while (index < length &&
                            Character.isLetterOrDigit(expression.charAt(index)) ||
                            expression.charAt(index) == '_') {

                            tempString += String.valueOf(expression.charAt(index));
                            index++;
                        }

                        tempString = tempString.toUpperCase();

                        for (ValueTable keyword : keywords) {
                            if (keyword.Value.compareTo(tempString) == 0) {
                                return keyword.tok;
                            }
                        }

                        this.string = tempString;
                        tok = TOKEN.TOK_UNQUOTED_STRING;

                    } else {
                        tok = TOKEN.ILLEGAL_TOKEN;
                    }
                }
            }
        } while (restart);

        return tok;

    }

    public String getCurrentLine(int pindex) {
        int tindex = pindex;

        if (pindex >= length) {
            tindex = length - 1;
        }

        while ((tindex > 0) && (expression.charAt(tindex) != '\n')) {
            tindex--;
        }

        if (expression.charAt(tindex) != '\n') {
            tindex++;
        }

        String currentLine = "";
        while ((tindex > 0) && (expression.charAt(tindex) != '\n')) {
            currentLine = currentLine + expression.charAt(tindex);
            tindex++;
        }

        return currentLine;
    }

    //skip to next line
    private void skipToNextLine() {

        // move the index to the end of line
        while (index < length
            && !System.lineSeparator().contains(
            String.valueOf(expression.charAt(index)))) {
            index++;
        }

        // moves over the line separator characters
        while (index < length
            && System.lineSeparator().contains(
            String.valueOf((expression.charAt(index))))) {
            index++;

        }

    }


}

