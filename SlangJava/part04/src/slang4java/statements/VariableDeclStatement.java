package slang4java.statements;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.expressions.Exp;
import slang4java.expressions.Variable;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class VariableDeclStatement extends Stmt {
    private SymbolInfo symbolInfo =null;
    private Variable variable =null;

    public VariableDeclStatement(SymbolInfo var) {
        this.symbolInfo = var;
    }

    @Override
    public SymbolInfo Execute(RUNTIEM_CONTEXT cont) {
        cont.getSymbolTable().Add(symbolInfo);
        variable = new Variable(symbolInfo);
        return null;
    }
}
