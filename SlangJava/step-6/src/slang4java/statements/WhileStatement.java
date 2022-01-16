package slang4java.statements;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.expressions.AbstractExpression;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

import java.util.ArrayList;

public class WhileStatement extends Statement {

    private AbstractExpression condition;
    private ArrayList<Statement> statementsList;

    public WhileStatement(AbstractExpression cond, ArrayList<Statement> statementsList) {
        this.condition = cond;
        this.statementsList = statementsList;
    }


    @Override
    public SymbolInfo Execute(RUNTIEM_CONTEXT cont) throws Exception {
        while (true) {
            SymbolInfo evaluated_condition = condition.Evaluate(cont);
            if (evaluated_condition == null || evaluated_condition.Type != TypeInfo.TYPE_BOOL)
                return null;

            if (!evaluated_condition.BoolValue)
                return null;

            for (Statement s : statementsList) {
                SymbolInfo tsp = s.Execute(cont);
                if (tsp != null)
                    return tsp;
            }
        }
    }
}
