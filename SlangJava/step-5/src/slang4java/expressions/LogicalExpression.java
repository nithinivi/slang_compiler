package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.lexer.TOKEN;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

public class LogicalExpression extends AbstractExpression {

    private AbstractExpression lExpression, rExpression;
    private TOKEN operator;

    TypeInfo _type;

    public LogicalExpression(AbstractExpression a, AbstractExpression b, TOKEN o) {
        this.lExpression = a;
        this.rExpression = b;
        this.operator = o;
    }

    @Override
    public SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) throws Exception {
        SymbolInfo left_eval = lExpression.Evaluate(cont);
        SymbolInfo right_eval = rExpression.Evaluate(cont);

        if (left_eval.Type == TypeInfo.TYPE_BOOL && right_eval.Type == TypeInfo.TYPE_BOOL) {

            SymbolInfo ret_val = new SymbolInfo();
            ret_val.Type = TypeInfo.TYPE_BOOL;
            ret_val.SymbolName = "";

            if (operator == TOKEN.TOK_AND)
                ret_val.BoolValue = left_eval.BoolValue && right_eval.BoolValue;
            else if (operator == TOKEN.TOK_OR)
                ret_val.BoolValue = left_eval.BoolValue || right_eval.BoolValue;
            else
                return null;

            return ret_val;
        }

        return null;
    }

    @Override
    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) throws Exception {

        TypeInfo left_type = lExpression.TypeCheck(cont);
        TypeInfo right_type = rExpression.TypeCheck(cont);

        if (left_type == right_type && left_type == TypeInfo.TYPE_BOOL)
            _type = TypeInfo.TYPE_BOOL;
        else
            throw new Exception("Wrong type in expression");

        return _type;
    }

    @Override
    public TypeInfo GetType() {
        return _type;
    }
}
