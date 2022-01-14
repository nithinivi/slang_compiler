package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class NumericConstant extends Expression {
    private double _value;
    private SymbolInfo info;

    public NumericConstant(double _value) {
        info = new SymbolInfo();
        info.SymbolName = null;
        info.DoubleValue = _value;
        info.Type = TypeInfo.TYPE_NUMERIC;
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
