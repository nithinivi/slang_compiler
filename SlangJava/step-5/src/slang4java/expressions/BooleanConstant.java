package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class BooleanConstant extends Expression {
    private SymbolInfo info;


    public BooleanConstant(boolean pvalue) {
        info = new SymbolInfo();
        info.SymbolName = null;
        info.Type = TypeInfo.TYPE_BOOL;
        info.BoolValue = pvalue;
    }

    @Override
    public SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) {
        return info;
    }

    @Override
    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) {
        return info.Type;
    }

    @Override
    public TypeInfo GetType() {
        return info.Type;
    }
}
