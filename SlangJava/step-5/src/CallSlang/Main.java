package CallSlang;

import slang4java.context.COMPILATION_CONTEXT;
import slang4java.context.RUNTIEM_CONTEXT;
import slang4java.lexer.RDParser;
import slang4java.statements.Statement;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        byte[] b;
        try (InputStream stream = new FileInputStream(
                "/home/nithihn/learn/slang_compiler/SlangJava/part04/src/CallSlang/third.sl")) {
            b = new byte[stream.available()];
            int read = stream.read(b);
        }
        String program = new String(b);
        RDParser pars = null;
        pars = new RDParser(program);
        COMPILATION_CONTEXT ctx = new COMPILATION_CONTEXT();
        ArrayList stmts = pars.Parse(ctx);
        RUNTIEM_CONTEXT rtx = new RUNTIEM_CONTEXT();
        for (Object o : stmts) {
            Statement s = (Statement) o;
            s.Execute(rtx);
        }
    }
}


