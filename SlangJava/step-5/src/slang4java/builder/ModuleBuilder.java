package slang4java.builder;

import slang4java.complationUnits.Module;
import slang4java.procedure.Procedure;

import java.util.ArrayList;

public class ModuleBuilder extends AbstractBuilder {
    private ArrayList<Procedure> procs;        // array of procedures
    private ArrayList protos = null;       // array of function prototypes

    public ModuleBuilder(ArrayList<Procedure> procs) {
        this.procs = procs;
    }

    public boolean add(Procedure p) {
        procs.add(p);
        return true;
    }

    public Module GetProgram() {
        return new Module(procs);
    }

    public Procedure getProc(String name) {
        for (Procedure p : procs)
            if (p.m_name.equals(name))
                return p;
        return null;
    }


}
