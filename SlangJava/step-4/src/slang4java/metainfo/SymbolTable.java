package slang4java.metainfo;

import slang4java.expressions.Variable;

import java.util.HashMap;

public class SymbolTable {
    HashMap<String, SymbolInfo> dt = new HashMap<String, SymbolInfo>();

    public void Add(SymbolInfo s) {
        dt.put(s.SymbolName, s);
    }

    public SymbolInfo getSymbol(String symbolname) {
        return dt.get(symbolname);
    }

    public void Assign(Variable var, SymbolInfo value) {
        value.SymbolName = var.getVar_name();
        dt.put(var.getVar_name(), value);
    }


    public void Assign(String var, SymbolInfo value) {
        dt.put(var, value);
    }
}
