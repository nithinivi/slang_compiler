package CallSlang;

import slang4java.builder.ExpressionBuilder;
import slang4java.expressions.BinaryExp;
import slang4java.expressions.Exp;
import slang4java.expressions.NumericConstant;
import slang4java.expressions.UnaryExp;
import slang4java.OPERATOR;

public class Main {

    public static void main(String[] args) {
        ExpressionBuilder b = new ExpressionBuilder("-2 * ( 3 + 3) ");
        Exp e = b.getExpression();
        System.out.println(e.Evaluate(null));


    }
}
