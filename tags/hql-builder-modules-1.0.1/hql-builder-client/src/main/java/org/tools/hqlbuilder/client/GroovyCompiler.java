package org.tools.hqlbuilder.client;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author Jurgen
 */
public class GroovyCompiler {
    /** jsengine */
    private static final ScriptEngine groovyengine = new ScriptEngineManager().getEngineByName("groovy");

    private static final String imports;

    static {
        StringBuilder sb = new StringBuilder();
        String[] packages = {
                "java.io",
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
            sb.append("import ").append(pack).append(".*;\n");
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

        try {
            return groovyengine.eval(imports + code);
        } catch (Exception ex) {
            return groovyengine.eval("'" + code + "'");
        }
    }

    /**
     * init was successful
     * 
     * @return
     */
    public static boolean success() {
        return groovyengine != null;
    }
}
