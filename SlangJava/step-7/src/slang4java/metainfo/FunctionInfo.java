package slang4java.metainfo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FunctionInfo {
    public TypeInfo _ret_value;
    public String _name;
    public ArrayList _typeinfo;

    public FunctionInfo(String name, TypeInfo ret_value, ArrayList formals) {

        _ret_value = ret_value;
        _typeinfo = formals;
        _name = name;
    }
}
