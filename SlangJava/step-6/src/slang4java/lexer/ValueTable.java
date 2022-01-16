package slang4java.lexer;

class ValueTable {
    public TOKEN tok;
    public String Value;

    public ValueTable(TOKEN tok, String Value) {
        this.tok = tok;
        this.Value = Value;
    }
}
