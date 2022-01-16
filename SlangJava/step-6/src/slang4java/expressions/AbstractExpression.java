package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public abstract class AbstractExpression {
    public abstract SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) throws Exception;
    public abstract TypeInfo TypeCheck(COMPILATION_CONTEXT cont) throws Exception;
    public abstract TypeInfo GetType();
}
