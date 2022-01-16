package slang4java.procedure;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.statements.Statement;

import java.util.ArrayList;

public abstract class AbstractProcedure {

    public abstract SymbolInfo Execute(RUNTIEM_CONTEXT cont, ArrayList actuals) throws Exception;
}
