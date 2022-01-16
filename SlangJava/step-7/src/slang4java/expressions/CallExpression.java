package slang4java.expressions;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.TypeInfo;
import slang4java.procedure.Procedure;

import java.util.ArrayList;

public class CallExpression extends AbstractExpression {
    Procedure procedure;
    ArrayList<AbstractExpression> actuals;
    String procedureName;
    boolean _isRecurse;
    TypeInfo _type;

    public CallExpression(Procedure procedure, ArrayList<AbstractExpression> actuals) {
        this.procedure = procedure;
        this.actuals = actuals;
    }

    public CallExpression(String procedureName, boolean _isRecurse, ArrayList<AbstractExpression> actuals) {
        this.procedureName = procedureName;
        if (_isRecurse)
            this._isRecurse = true;
        this.actuals = actuals;
        this.procedure = null;

    }

    @Override
    public SymbolInfo Evaluate(RUNTIEM_CONTEXT cont) throws Exception {
        if (procedure != null) {
            // ordinary funciton call

            RUNTIEM_CONTEXT rtx = new RUNTIEM_CONTEXT(cont.getProgram());
            ArrayList<SymbolInfo> list = new ArrayList<>();

            for (AbstractExpression exp : actuals)
                list.add(exp.Evaluate(cont));

            return procedure.Execute(rtx, list);

        } else {
            // recursive function
            // lookup the function table and resolve the addresss

            procedure = cont.getProgram().Find(procedureName);
            RUNTIEM_CONTEXT rtx = new RUNTIEM_CONTEXT(cont.getProgram());
            ArrayList<SymbolInfo> list = new ArrayList<>();

            for (AbstractExpression exp : actuals)
                list.add(exp.Evaluate(cont));

            return procedure.Execute(rtx, list);

        }
    }

    @Override
    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) throws Exception {
        if (procedure!=null)
            _type = procedure.TypeCheck(cont);
        return _type;
    }

    @Override
    public TypeInfo GetType() {
        return _type;
    }
}
