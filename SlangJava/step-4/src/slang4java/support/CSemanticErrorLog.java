package slang4java.support;

import java.util.ArrayList;

public class CSemanticErrorLog {
    static int ErrorCount = 0;
    static ArrayList lst = new ArrayList();

    public static void CleanUP(){
        lst.clear();
        ErrorCount =0;
    }

    public static String GetLog() {


        String str = "Logged data by the user and processing status" + "\r\n";
        str += "--------------------------------------\r\n";

        int xt = lst.size();

        if (xt == 0) {
            str += "NIL" + "\r\n";

        } else {

            for (int i = 0; i < xt; ++i) {
                str = str + lst.get(i).toString() + "\r\n";
            }
        }
        str += "--------------------------------------\r\n";
        return str;
    }

    public static void AddLine(String str) {

        lst.add(str.substring(0));
        ErrorCount++;
    }

    public static void AddFromUser(String str) {

        lst.add(str.substring(0));
        ErrorCount++;
    }
}
