package slang4java.builder;

import slang4java.complationUnits.TModule;
import slang4java.procedure.Procedure;

import java.util.ArrayList;

public class TModuleBuilder extends AbstractBuilder {
    private ArrayList<Procedure> procs;        // array of procedures
    private ArrayList protos;       // array of function prototypes

    public TModuleBuilder() {
        this.procs = new ArrayList<>();
        this.protos = null;
    }

    public boolean add(Procedure p) {
        procs.add(p);
        return true;
    }

    public TModule GetProgram() {
        return new TModule(procs);
    }

    public Procedure getProc(String name) {
        for (Procedure p : procs)
            if (p.m_name.equals(name))
                return p;
        return null;
    }


}
