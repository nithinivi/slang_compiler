package slang4java.builder;

import slang4java.complationUnits.TModule;
import slang4java.metainfo.FunctionInfo;
import slang4java.metainfo.TypeInfo;
import slang4java.procedure.Procedure;

import java.util.ArrayList;
import java.util.function.Function;

public class TModuleBuilder extends AbstractBuilder {
    private ArrayList<Procedure> procs;        // array of procedures
    private ArrayList protos;       // array of function prototypes

    public TModuleBuilder() {
        this.procs = new ArrayList<>();
        this.protos = new ArrayList();
    }

    public boolean isFunction(String name) {
        for (Object o : protos) {
            FunctionInfo func = (FunctionInfo) o;
            if (func._name.equals(name))
                return true;
        }
        return false;

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

    public void AddFunctionProtoType(String name, TypeInfo ret_type,
                                     ArrayList type_infos) {
        FunctionInfo info = new FunctionInfo(name, ret_type, type_infos);
        protos.add(info);
    }


}
