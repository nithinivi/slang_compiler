package slang4java.expressions;

import lombok.Setter;
import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class Variable extends AbstractExpression {

    @Setter
    private String var_name;
    private TypeInfo _type;


    public Variable(SymbolInfo symbolInfo) {
        this.var_name = symbolInfo.SymbolName;
    }

    public Variable(COMPILATION_CONTEXT compilation_context,
                    String name,
                    double _value) {
        SymbolInfo symbolInfo = new SymbolInfo();
        symbolInfo.SymbolName = name;
        symbolInfo.Type = TypeInfo.TYPE_NUMERIC;
        symbolInfo.DoubleValue = _value;
        compilation_context.getSymbolTable().Add(symbolInfo);
        var_name = name;
    }


    public Variable(COMPILATION_CONTEXT compilation_context,
                    String name,
                    boolean _value) {
        SymbolInfo symbolInfo = new SymbolInfo();
        symbolInfo.SymbolName = name;
        symbolInfo.Type = TypeInfo.TYPE_BOOL;
        symbolInfo.BoolValue = _value;
        compilation_context.getSymbolTable().Add(symbolInfo);
        var_name = name;
    }


    public Variable(COMPILATION_CONTEXT compilation_context,
                    String name,
                    String _value) {
        SymbolInfo symbolInfo = new SymbolInfo();
        symbolInfo.SymbolName = name;
        symbolInfo.Type = TypeInfo.TYPE_STRING;
        symbolInfo.StringValue = _value;
        compilation_context.getSymbolTable().Add(symbolInfo);
        var_name = name;
    }

    @Override
    public SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) {
        if (cont.getSymbolTable() == null) {

            return null;
        } else {
            SymbolInfo symbolInfo = cont.getSymbolTable().getSymbol(var_name);
            return symbolInfo;
        }
    }


    @Override
    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) {
        if (cont.getSymbolTable() != null) {
            SymbolInfo symbolInfo = cont.getSymbolTable().getSymbol(var_name);
            if (symbolInfo != null) {
                _type = symbolInfo.Type;
                return _type;
            }
        }
        return TypeInfo.TYPE_ILLEGAL;
    }

    @Override
    public TypeInfo GetType() {
        return _type;
    }

    public String getVar_name() {
        return var_name;
    }

}
