package slang4java.builder;

import slang4java.lexer.RDPaser;
import slang4java.expressions.Exp;

public class ExpressionBuilder extends AbstractBuilder{
    public String _expr_string;

    public ExpressionBuilder(String _expr_string) {
        this._expr_string = _expr_string;
    }

    public Exp getExpression(){
        RDPaser p = new RDPaser(_expr_string);
        return  p.callExpr();
    }

}
