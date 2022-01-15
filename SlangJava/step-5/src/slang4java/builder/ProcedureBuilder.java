package slang4java.builder;

import lombok.Getter;
import lombok.Setter;
import slang4java.context.COMPILATION_CONTEXT;
import slang4java.expressions.Expression;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.SymbolTable;
import slang4java.metainfo.TypeInfo;
import slang4java.procedure.Procedure;
import slang4java.statements.Statement;

import java.util.ArrayList;

public class ProcedureBuilder extends AbstractBuilder {

    @Setter
    @Getter
    private String procedureName;           // hardcoded as main

    @Setter
    @Getter
    TypeInfo typeInfo = TypeInfo.TYPE_ILLEGAL;     // return type of procedure

    ArrayList<SymbolInfo> formalsParameters;                    // procedures doesn't takes any argument
    ArrayList<Statement> m_statements = new ArrayList<>();      // statements
    COMPILATION_CONTEXT ctx = null;                             // compilation context for type anaylsis


    public ProcedureBuilder(String name, COMPILATION_CONTEXT ctx) {
        this.procedureName = name;
        this.ctx = ctx;
    }

    public boolean addLocal(SymbolInfo info) {
        ctx.getSymbolTable().Add(info);
        return true;
    }

    public boolean addFormals(SymbolInfo info) {
        formalsParameters.add(info);
        return true;
    }

    public void addStatement(Statement stmt) {
        m_statements.add(stmt);
    }

    public TypeInfo typeCheck(Expression exp) throws Exception {
        return exp.TypeCheck(ctx);
    }

    public SymbolInfo getSymbol(String name) {
        return ctx.getSymbolTable().getSymbol(name);
    }

    public boolean checkProto(String name) {
        return true;
    }

    public COMPILATION_CONTEXT getContext() {
        return ctx;
    }

    public SymbolTable getSymbolTable() {
        return ctx.getSymbolTable();
    }

    public Procedure GetProcedure() {
        return new Procedure(procedureName, formalsParameters,
            m_statements, ctx.getSymbolTable(), typeInfo);
    }


}
