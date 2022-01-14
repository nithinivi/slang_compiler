package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.lexer.RelationalOperator;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class RelationalExpression extends Expression {

    RelationalOperator operator;
    private Expression lExpression, rExpression;
    TypeInfo _type;
    TypeInfo _opType;    //Operand type


    public RelationalExpression(Expression a, Expression b, RelationalOperator o) {
        this.lExpression = a;
        this.rExpression = b;
        this.operator = o;

    }

    @Override
    public SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) throws Exception {

        SymbolInfo left_eval = lExpression.Evaluate(cont);
        SymbolInfo right_eval = rExpression.Evaluate(cont);
        SymbolInfo ret_val = new SymbolInfo();

        if (left_eval.Type == TypeInfo.TYPE_NUMERIC && right_eval.Type == TypeInfo.TYPE_NUMERIC) {

            ret_val.Type = TypeInfo.TYPE_BOOL;
            ret_val.SymbolName = "";

            if (operator == RelationalOperator.TOK_EQ)
                ret_val.BoolValue = left_eval.DoubleValue == right_eval.DoubleValue;
            else if (operator == RelationalOperator.TOK_NEQ)
                ret_val.BoolValue = left_eval.DoubleValue != right_eval.DoubleValue;
            else if (operator == RelationalOperator.TOK_GT)
                ret_val.BoolValue = left_eval.DoubleValue > right_eval.DoubleValue;
            else if (operator == RelationalOperator.TOK_GTE)
                ret_val.BoolValue = left_eval.DoubleValue >= right_eval.DoubleValue;
            else if (operator == RelationalOperator.TOK_LT)
                ret_val.BoolValue = left_eval.DoubleValue < right_eval.DoubleValue;
            else if (operator == RelationalOperator.TOK_LTE)
                ret_val.BoolValue = left_eval.DoubleValue <= right_eval.DoubleValue;
            return ret_val;

        } else if (left_eval.Type == TypeInfo.TYPE_STRING && right_eval.Type == TypeInfo.TYPE_STRING) {

            ret_val.Type = TypeInfo.TYPE_BOOL;
            ret_val.SymbolName = "";

            if (operator == RelationalOperator.TOK_EQ)
                ret_val.BoolValue = left_eval.StringValue.compareTo(right_eval.StringValue) == 0;
            else if (operator == RelationalOperator.TOK_NEQ)
                ret_val.BoolValue = left_eval.StringValue.compareTo(right_eval.StringValue) != 0;
            else
                ret_val.BoolValue = false;
            return ret_val;


        } else if (left_eval.Type == TypeInfo.TYPE_BOOL && right_eval.Type == TypeInfo.TYPE_BOOL) {

            ret_val.Type = TypeInfo.TYPE_BOOL;
            ret_val.SymbolName = "";

            if (operator == RelationalOperator.TOK_EQ)
                ret_val.BoolValue = left_eval.BoolValue == right_eval.BoolValue;
            else if (operator == RelationalOperator.TOK_NEQ)
                ret_val.BoolValue = left_eval.BoolValue != right_eval.BoolValue;
            else
                ret_val.BoolValue = false;
            return ret_val;

        }
        return null;
    }

    @Override
    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) throws Exception {
        TypeInfo left_type = lExpression.TypeCheck(cont);
        TypeInfo right_type = rExpression.TypeCheck(cont);

        if (left_type != right_type)
            throw new Exception("Wrong Type in expression");

        if (left_type == TypeInfo.TYPE_STRING && (!(
            operator == RelationalOperator.TOK_EQ || operator == RelationalOperator.TOK_NEQ)))
            throw new Exception(" Only == and != are supported for String Type");

        if (left_type == TypeInfo.TYPE_BOOL && (!(
            operator == RelationalOperator.TOK_EQ || operator == RelationalOperator.TOK_NEQ)))
            throw new Exception(" Only == and != supported for Bool Type");

        _opType = left_type;
        _type = TypeInfo.TYPE_BOOL;

        return _type;
    }

    @Override
    public TypeInfo GetType() {
        return _type;
    }
}
