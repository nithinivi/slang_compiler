package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class UnaryPlus extends AbstractExpression {
    private AbstractExpression expression;
    private TypeInfo _type;

    public UnaryPlus(AbstractExpression expression) {
        this.expression = expression;
    }

    @Override
    public SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) throws Exception{
        SymbolInfo eval_exp = expression.Evaluate(cont);
        if (eval_exp.Type == TypeInfo.TYPE_NUMERIC) {
            SymbolInfo retval = new SymbolInfo();
            retval.DoubleValue = eval_exp.DoubleValue;
            retval.Type = TypeInfo.TYPE_NUMERIC;
            retval.SymbolName = "";
            return retval;
        } else {

            throw new Exception("Type mismatch");
        }
    }

    @Override
    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) throws Exception {
        TypeInfo eval_expTypeInfo = expression.TypeCheck(cont);

        if (eval_expTypeInfo == TypeInfo.TYPE_NUMERIC) {
            _type = eval_expTypeInfo;
            return _type;

        } else {
            System.out.println("right " + eval_expTypeInfo.toString());
            throw new Exception("Binary Minus - type mismatch failure");
        }
    }

    @Override
    public TypeInfo GetType() {
        return _type;
    }
}
