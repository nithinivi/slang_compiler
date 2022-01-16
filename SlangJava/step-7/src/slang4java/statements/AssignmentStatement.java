package slang4java.statements;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.expressions.AbstractExpression;
import slang4java.expressions.Variable;
import slang4java.metainfo.SymbolInfo;

public class AssignmentStatement extends Statement {
    private Variable variable;
    private AbstractExpression expression;

    public AssignmentStatement(SymbolInfo variable, AbstractExpression expression) {
        this.variable = new Variable(variable);
        this.expression = expression;
    }

    @Override
    public SymbolInfo Execute(RUNTIEM_CONTEXT cont) throws Exception{
        SymbolInfo var = expression.Evaluate(cont);
        cont.getSymbolTable().Assign(variable,var);
        return null;
    }
}
