package slang4java.procedure;

import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.metainfo.SymbolInfo;
import slang4java.metainfo.SymbolTable;
import slang4java.metainfo.TypeInfo;
import slang4java.statements.Statement;

import java.util.ArrayList;

public class Procedure extends AbstractProcedure {

    public String m_name;                                // procedure name
    public ArrayList<SymbolInfo> m_formals = null;       // formal parameters
    public ArrayList<Statement> m_statements = null;     // list of statements
    public SymbolTable m_locals = null;                  // local variables
    public TypeInfo _type = TypeInfo.TYPE_ILLEGAL;
    public SymbolInfo return_value = null;               // returns


    public Procedure(String name,
                     ArrayList<SymbolInfo> formals,
                     ArrayList<Statement> statements,
                     SymbolTable locals,
                     TypeInfo type) {
        this.m_name = name;
        this.m_formals = formals;
        this.m_statements = statements;
        this.m_locals = locals;
        this._type = type;
    }

    @Override
    public SymbolInfo Execute(RUNTIEM_CONTEXT cont) throws Exception {
        for (Statement statement : m_statements)
            statement.Execute(cont);

        return null;
    }
}

