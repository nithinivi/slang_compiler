package slang4java.context;

import lombok.Getter;
import lombok.Setter;
import slang4java.metainfo.SymbolTable;

public class RUNTIEM_CONTEXT {
    @Getter
    @Setter
    private SymbolTable symbolTable;

    public RUNTIEM_CONTEXT() {
        symbolTable = new SymbolTable();
    }


}
