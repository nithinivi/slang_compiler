import java.util.List;
import java.util.Stack;

public class Extensions {
    public static Expr ParseOne() {
        Expr r = new BinaryExpr(new NumericConstant(2),
                new BinaryExpr(new NumericConstant(3), new NumericConstant(4), OPERATOR.MUL),
                OPERATOR.PLUS);
        return r;
    }

    public static Expr ParseTwo() {
        Expr r = new BinaryExpr(new NumericConstant(3), new NumericConstant(4), OPERATOR.MUL);

        return r;
    }

    public static List<ItemList> FlattenExpr(Expr e) {
        FlattenVisitor f = new FlattenVisitor();
        e.accept(f);
        return f.flattenedExpr();
    }


    public static double evaluate(List<ItemList> ls) {
        Stack<Double> stk = new Stack<Double>();

        for (ItemList s : ls) {
            if (s.knd == ExprKind.VALUE) {
                stk.push(s.value);
            } else {
                switch (s.op) {
                    case PLUS:
                        stk.push(stk.pop() + stk.pop());
                        break;
                    case MINUS:
                        stk.push(stk.pop() - stk.pop());
                        break;
                    case DIV:
                        double n = stk.pop();
                        stk.push(stk.pop() / n);
                        break;
                    case MUL:
                        stk.push(stk.pop() * stk.pop());
                        break;
                }
            }
        }

        return stk.pop();
    }

}


class EntryPoint2 {

    public static void main(String[] args) {
        Expr nd = Extensions.ParseTwo();
        StackEvaluator s = new StackEvaluator();
        nd.accept(s);
        List<ItemList> lsrs =   Extensions.FlattenExpr(nd);

    }
}
