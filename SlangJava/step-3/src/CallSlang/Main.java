package CallSlang;

import slang4java.builder.ExpressionBuilder;
import slang4java.expressions.BinaryExp;
import slang4java.expressions.Exp;
import slang4java.expressions.NumericConstant;
import slang4java.expressions.UnaryExp;
import slang4java.OPERATOR;
import slang4java.lexer.RDPaser;
import slang4java.statements.Stmt;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String a = "PRINTLINE 2*10; ";
        RDPaser p = new RDPaser(a);
        ArrayList arr = p.Parse();
        for (Object obj : arr) {
            Stmt s = ((Stmt) obj);
            s.Execute(null);
            
        }

    }
}

