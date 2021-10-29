package slang4java.expressions;

import slang4java.lexer.OPERATOR;
import slang4java.context.RUNTIEM_CONTEXT;

public class BinaryExp extends Exp {

    private Exp _ex1, _ex2;
    private OPERATOR _op;

    public BinaryExp(Exp _ex1, Exp ex2, OPERATOR _op) {
        this._ex1 = _ex1;
        this._ex2 = ex2;
        this._op = _op;
    }


    @Override
    public double Evaluate(RUNTIEM_CONTEXT cont) {
        switch (_op) {
            case PLUS -> {
                return this._ex1.Evaluate(cont) + this._ex2.Evaluate(cont);
            }
            case MINUS -> {
                return this._ex1.Evaluate(cont) - this._ex2.Evaluate(cont);
            }
            case DIV -> {
                return this._ex1.Evaluate(cont) / this._ex2.Evaluate(cont);
            }
            case MUL -> {
                return this._ex1.Evaluate(cont) * this._ex2.Evaluate(cont);
            }
        }
        return Double.NaN;

    }
}
