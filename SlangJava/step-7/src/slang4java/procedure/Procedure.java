package slang4java.procedure;

import lombok.Getter;
import slang4java.context.COMPILATION_CONTEXT;
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

    public String getM_name() {
        return m_name;
    }

    public ArrayList getM_formals() {
        return m_formals;
    }

    public TypeInfo GetType() {
        return _type;
    }

    public SymbolInfo ReturnValue() {
        return return_value;
    }

    public TypeInfo TypeCheck(COMPILATION_CONTEXT cont) {
        return TypeInfo.TYPE_NUMERIC;
    }

    @Override
    public SymbolInfo Execute(RUNTIEM_CONTEXT cont, ArrayList actuals) throws Exception {
        ArrayList variables = new ArrayList();
        int i = 0;
        if (m_formals != null && actuals != null) {
            for (SymbolInfo formal : m_formals) {
                SymbolInfo info = (SymbolInfo) actuals.get(i);
                info.SymbolName = formal.SymbolName;
                cont.getSymbolTable().Add(info);
                i++;
            }
        }
        for (Statement statement : m_statements) {
            return_value = statement.Execute(cont);
            if (return_value != null) {
                return return_value;
            }

        }

        return null;
    }
}

