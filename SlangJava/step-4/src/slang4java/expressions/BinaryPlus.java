package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class BinaryPlus extends Exp {

    private Exp leftExpr, rightExpr;
    private TypeInfo _type;

    public BinaryPlus(Exp exp1, Exp exp2) {
        this.leftExpr = exp1;
        this.rightExpr = exp2;
    }

    @Override
    public SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) throws Exception {

        SymbolInfo eval_left = leftExpr.Evaluate(cont);
        SymbolInfo eval_right = rightExpr.Evaluate(cont);

        // Implement String concat logic
        if ((eval_left.Type == TypeInfo.TYPE_STRING)
                && (eval_right.Type == TypeInfo.TYPE_STRING)) {
            SymbolInfo retval = new SymbolInfo();
            retval.StringValue = eval_left.StringValue + eval_right.StringValue;
            retval.Type = TypeInfo.TYPE_STRING;
            retval.SymbolName = "";
            return retval;

        } else if ((eval_left.Type == TypeInfo.TYPE_NUMERIC)
                && (eval_right.Type == TypeInfo.TYPE_NUMERIC)) {
            SymbolInfo retval = new SymbolInfo();
            retval.DoubleValue = eval_left.DoubleValue + eval_right.DoubleValue;
            retval.Type = TypeInfo.TYPE_NUMERIC;
            retval.SymbolName = "";
            return retval;
        } else {
            throw new Exception("Type mismatch");
        }


    }

    @Override
    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) throws Exception {
        TypeInfo eval_leftTypeInfo = leftExpr.TypeCheck(cont);
        TypeInfo eval_rightTypeInfo = rightExpr.TypeCheck(cont);

        if((eval_leftTypeInfo == eval_rightTypeInfo) && (eval_leftTypeInfo != TypeInfo.TYPE_BOOL)){
            _type = eval_leftTypeInfo;
            return  _type;

        }
        else{
            System.out.println("left " + eval_leftTypeInfo.toString());
            System.out.println("right " + eval_rightTypeInfo.toString());
            throw new Exception("Binary Plus - type mismatch failure");
        }
    }

    @Override
    public TypeInfo GetType() {
        return _type;
    }
}
