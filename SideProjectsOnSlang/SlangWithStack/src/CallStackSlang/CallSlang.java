package CallStackSlang;

import SlangWithStack.ExprEval;

public class CallSlang {

    public static void main(String[] args) {
        String stmt = "2*(2+2)";
        ExprEval.RDParser parser = new ExprEval.RDParser(stmt);
        double result = parser.callExpr();
        System.out.println(result);

    }
}
