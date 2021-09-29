package CallSlang;

import slang4java.expressions.BinaryExp;
import slang4java.expressions.Exp;
import slang4java.expressions.NumericConstant;
import slang4java.expressions.UnaryExp;
import slang4java.OPERATOR;

public class Main {

    public static void main(String[] args) {
        // AST for 10  * 10
        Exp e;
        e = new BinaryExp(new NumericConstant(10), new NumericConstant(10), OPERATOR.MUL);
        System.out.println(e.Evaluate(null));


        //AST for - (10 + (30 + 50))
        e = new UnaryExp(
                new BinaryExp(
                    new NumericConstant(10),
                    new BinaryExp(new NumericConstant(30), new NumericConstant(50), OPERATOR.PLUS),
                    OPERATOR.PLUS),
                OPERATOR.MINUS);

        System.out.println(e.Evaluate(null));

    }
}
