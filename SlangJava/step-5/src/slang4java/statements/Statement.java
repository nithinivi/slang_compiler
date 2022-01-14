package slang4java.statements;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;

public abstract class Statement {

    public abstract SymbolInfo Execute(RUNTIEM_CONTEXT cont) throws Exception;
}

