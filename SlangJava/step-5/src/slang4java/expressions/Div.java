package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class Div extends Expression {


    private Expression leftExpr, rightExpr;
    private TypeInfo _type;

    public Div(Expression leftExpr, Expression rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    @Override
    public SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) throws Exception{

        SymbolInfo eval_left = leftExpr.Evaluate(cont);
        SymbolInfo eval_right = rightExpr.Evaluate(cont);
        if ((eval_left.Type == TypeInfo.TYPE_NUMERIC)
                && (eval_right.Type == TypeInfo.TYPE_NUMERIC)) {
            SymbolInfo retval = new SymbolInfo();
            retval.DoubleValue = eval_left.DoubleValue / eval_right.DoubleValue;
            retval.Type = TypeInfo.TYPE_NUMERIC;
            retval.SymbolName = "";
            return retval;
        } else {

            throw new Exception("Type mismatch");
        }
    }

    @Override
    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) throws Exception{
        TypeInfo eval_leftTypeInfo = leftExpr.TypeCheck(cont);
        TypeInfo eval_rightTypeInfo = rightExpr.TypeCheck(cont);

        if ((eval_leftTypeInfo == eval_rightTypeInfo) && (eval_leftTypeInfo == TypeInfo.TYPE_NUMERIC)) {
            _type = eval_leftTypeInfo;
            return _type;

        } else {
            System.out.println("left " + eval_leftTypeInfo.toString());
            System.out.println("right " + eval_rightTypeInfo.toString());
            throw new Exception("Binary Minus - type mismatch failure");
        }
    }

    @Override
    public TypeInfo GetType() {
        return _type;
    }
}
