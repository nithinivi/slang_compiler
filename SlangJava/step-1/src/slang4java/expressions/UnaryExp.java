package slang4java.expressions;

import slang4java.OPERATOR;
import slang4java.RUNTIEM_CONTEXT;

public class UnaryExp extends Exp {
    private Exp _ex1;
    private OPERATOR _op;

    public UnaryExp(Exp _ex1, OPERATOR _op) {
        this._ex1 = _ex1;
        this._op = _op;
    }


    @Override
    public double Evaluate(RUNTIEM_CONTEXT cont) {
        switch (this._op) {
            case PLUS -> {
                return +_ex1.Evaluate(cont);
            }

            case MINUS -> {
                return -_ex1.Evaluate(cont);
            }
        }
        return Double.NaN;
    }
}
