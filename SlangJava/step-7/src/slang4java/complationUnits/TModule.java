package slang4java.complationUnits;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.procedure.Procedure;

import java.util.ArrayList;

public class TModule {
    private ArrayList<Procedure> procedures;

    public TModule(ArrayList<Procedure> procedures) {
        this.procedures = procedures;
    }

    public SymbolInfo Execute(RUNTIEM_CONTEXT cont, ArrayList actuals) throws Exception {
        Procedure p = Find("Main");
        if (p != null)
            return p.Execute(cont, actuals);
        return null;
    }

    public Procedure Find(String str) {
        for (Procedure p : procedures) {
            String procedure_name = p.m_name;
            if (procedure_name.toUpperCase().compareTo(str.toUpperCase()) == 0)
                return p;
        }
        return null;
    }
}
