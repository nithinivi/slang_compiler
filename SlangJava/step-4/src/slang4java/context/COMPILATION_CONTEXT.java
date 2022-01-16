package slang4java.context;

import lombok.Getter;
import lombok.Setter;
import slang4java.metainfo.SymbolTable;

public class COMPILATION_CONTEXT {

    @Setter
    @Getter
    private SymbolTable symbolTable;

    public COMPILATION_CONTEXT() {
        this.symbolTable = new SymbolTable();
    }
}
