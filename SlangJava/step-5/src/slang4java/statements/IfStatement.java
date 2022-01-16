package slang4java.statements;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.expressions.AbstractExpression;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;

import java.util.ArrayList;

public class IfStatement extends Statement {
    // IF <Bexpr> Then
    // <statementlist>
    // [ ELSE <statementlist> ] optional
    // ENDIF

    private AbstractExpression condition;
    private ArrayList _ifStatementsList;
    private ArrayList _elseStatementsList;

    public IfStatement(AbstractExpression cond, ArrayList ifList, ArrayList elseList) {
        this.condition = cond;
        this._ifStatementsList = ifList;
        this._elseStatementsList = elseList;
    }

    @Override
    public SymbolInfo Execute(RUNTIEM_CONTEXT cont) throws Exception {
        SymbolInfo excutedCondiation = condition.Evaluate(cont);

        if (excutedCondiation == null || excutedCondiation.Type != TypeInfo.TYPE_BOOL)
            return null;
        if (excutedCondiation.BoolValue) {
            for (Object s : _ifStatementsList) {
                Statement statement = (Statement) s;
                statement.Execute(cont);
            }
        } else if (_elseStatementsList != null) {
            for (Object s : _elseStatementsList) {
                Statement statement = (Statement) s;
                statement.Execute(cont);
            }
        }
        return null;
    }
}
