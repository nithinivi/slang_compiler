package slang4java.statements;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.expressions.Expression;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class PrintStatement extends Stmt{

    private Expression _ex;

    public PrintStatement(Expression _ex) {
        this._ex = _ex;
    }

    @Override
    public SymbolInfo Execute(RUNTIEM_CONTEXT cont) {

        SymbolInfo val = null;
        try {
            val = _ex.Evaluate(cont);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (val.Type == TypeInfo.TYPE_NUMERIC) {
            System.out.print(val.DoubleValue);
        } else if (val.Type == TypeInfo.TYPE_STRING) {
            System.out.print(val.StringValue);
        } else {
            System.out.print(val.BoolValue);
        }
        return null;

    }
}