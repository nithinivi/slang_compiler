package slang4java.expressions;

import slang4java.RUNTIEM_CONTEXT;

public class NumericConstant extends Exp {
    private double _value;

    public NumericConstant(double _value) {
        this._value = _value;
    }

    @Override
    public double Evaluate(RUNTIEM_CONTEXT cont) {
        return _value;
    }
}
