package slang4java.procedure;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;

public abstract class AbstractProcedure {
    public abstract SymbolInfo Execute(RUNTIEM_CONTEXT cont) throws Exception;
}
