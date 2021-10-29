package slang4java.statements;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.expressions.Exp;

public class PrintLineStatement extends Stmt{

    private Exp _ex;

    public PrintLineStatement(Exp _ex) {
        this._ex = _ex;
    }

    @Override
    public boolean Execute(RUNTIEM_CONTEXT cont) {

        double a= _ex.Evaluate(cont);
        System.out.println(String.valueOf(a));
        return  true;

    }
}
