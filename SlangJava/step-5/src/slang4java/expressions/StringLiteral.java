package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class StringLiteral extends Expression {
    private SymbolInfo info;

    public StringLiteral(String value) {
        info = new SymbolInfo();
        info.StringValue = value;
        info.SymbolName = null;
        info.Type = TypeInfo.TYPE_STRING;
    }

    @Override
    public SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) throws Exception {
        return info;
    }

    @Override
    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) throws Exception {
        return info.Type;
    }

    @Override
    public TypeInfo GetType() {
        return info.Type;
    }
}
