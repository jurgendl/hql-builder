package be.ugent.oasis.tools.hqlbuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * JavaScriptCompiler
 * 
 * @author jdlandsh
 */
public class JavaScriptCompiler {
    /** jsengine */
    private static final ScriptEngine jsengine = new ScriptEngineManager().getEngineByName("js");

    private static final String imports;

    static {
        StringBuilder sb = new StringBuilder();
        String[] packages = {
                "java.io",
                "java.lang",
                "java.net",
                "java.text",
                "java.sql",
                "java.math",
                "java.util",
                "java.util.regex",
                "org.joda.time",
                "org.apache.commons.lang",
                "org.apache.commons.lang.builder" };

        for (String pack : packages) {
            sb.append("importPackage(").append(pack).append(");\n");
        }

        imports = sb.toString();
    }

    /**
     * compile code
     * 
     * @param code
     * 
     * @return
     * 
     * @throws Exception
     * @throws RuntimeException
     */
    public static Object eval(String code) throws Exception {
        if (!success()) {
            throw new RuntimeException("initialization exception");
        }

        return jsengine.eval(imports + code);
    }

    /**
     * init was successful
     * 
     * @return
     */
    public static boolean success() {
        return jsengine != null;
    }
}
