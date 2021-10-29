package slang4java.statements;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.expressions.Exp;
import slang4java.expressions.Variable;
import slang4java.metainfo.SymbolInfo;

public class AssignmentStatement extends Stmt{
    private Variable variable;
    private Exp exp;

    public AssignmentStatement(Variable variable, Exp exp) {
        this.variable = variable;
        this.exp = exp;
    }

    @Override
    public SymbolInfo Execute(RUNTIEM_CONTEXT cont) throws Exception{
        SymbolInfo var = exp.Evaluate(cont);
        cont.getSymbolTable().Assign(variable,var);
        return null;
    }
}
