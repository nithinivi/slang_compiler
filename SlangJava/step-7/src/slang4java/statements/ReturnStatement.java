package slang4java.statements;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.expressions.AbstractExpression;
import slang4java.metainfo.SymbolInfo;

public class ReturnStatement extends Statement {
    private AbstractExpression expression;
    private SymbolInfo info = null;

    public ReturnStatement(AbstractExpression expression) {
        this.expression = expression;
    }

    @Override
    public SymbolInfo Execute(RUNTIEM_CONTEXT cont) throws Exception {
        info = (expression == null) ? null : expression.Evaluate(cont);
        return info;
    }
}
