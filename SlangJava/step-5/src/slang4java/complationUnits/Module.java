package slang4java.complationUnits;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.procedure.Procedure;

import java.util.ArrayList;

public class Module {
    private ArrayList<Procedure> procedures;

    public Module(ArrayList<Procedure> procedures) {
        this.procedures = procedures;
    }

    public SymbolInfo Execute(RUNTIEM_CONTEXT cont) throws Exception {
        Procedure p = Find("Main");
        if (p != null)
            return p.Execute(cont);
        return null;
    }

    private Procedure Find(String str) {
        for (Procedure p : procedures) {
            String procedure_name = p.m_name;
            if (procedure_name.toUpperCase().compareTo(str.toUpperCase()) == 0)
                return p;
        }
        return null;
    }
}
