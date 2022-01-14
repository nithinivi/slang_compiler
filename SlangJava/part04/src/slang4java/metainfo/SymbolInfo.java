package slang4java.metainfo;

public class SymbolInfo {
    public String SymbolName; // name of symbol


    public TypeInfo Type; // type of variable


    // Holds the actual variable data
    public String StringValue;
    public double DoubleValue;
    public boolean BoolValue;


    public String getName() {
        return SymbolName;
    }

    public TypeInfo getType() {
        return Type;
    }
}