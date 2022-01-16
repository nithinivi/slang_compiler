package slang4java.context;

import lombok.Getter;
import lombok.Setter;
import slang4java.complationUnits.TModule;
import slang4java.metainfo.SymbolTable;

public class RUNTIEM_CONTEXT {


    @Getter
    @Setter
    private SymbolTable symbolTable;

    private TModule _prog = null;

    public RUNTIEM_CONTEXT(TModule module) {
        symbolTable = new SymbolTable();
        _prog = module;
    }

    public TModule getProgram(){
        return _prog;
    }


}
