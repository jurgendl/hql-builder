package org.tools.hqlbuilder.common;

import java.util.Collections;
import java.util.Map;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * @author Jurgen
 */
public class GroovyCompiler {
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
                "org.apache.commons.lang3.builder" };

        for (String pack : packages) {
            sb.append("import ").append(pack).append(".*;\n");
        }

        imports = sb.toString();
    }

    public static Object eval(String code) {
        return eval(code, (Map<String, Object>) null);
    }

    public static Object eval(String code, Object x) {
        return eval(code, Collections.singletonMap("x", x));
    }

    /**
     * compile code
     */
    public static Object eval(String code, Map<String, Object> params) {
        try {
            return internal(code, params);
        } catch (Exception ex) {
            return internal("'" + code + "'", params);
        }
    }

    protected static Object internal(String expression, Map<String, Object> params) {
        Binding b = new Binding();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                b.setVariable(entry.getKey(), entry.getValue());
            }
        }
        GroovyShell sh = new GroovyShell(b);
        return sh.evaluate(imports + expression);
    }
}
