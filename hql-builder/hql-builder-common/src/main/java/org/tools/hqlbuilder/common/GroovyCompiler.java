package org.tools.hqlbuilder.common;

import groovy.util.Eval;

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
                "org.apache.commons.lang.builder" };

        for (String pack : packages) {
            sb.append("import ").append(pack).append(".*;\n");
        }

        imports = sb.toString();
    }

    /**
     * compile code
     */
    public static Object eval(String code, Object... params) {
        try {
            return internal(code, params);
        } catch (Exception ex) {
            return internal("'" + code + "'", params);
        }
    }

    protected static Object internal(String code, Object... params) {
        if (params != null) {
            if (params.length == 0) {
                return Eval.me(imports + code);
            } else if (params.length == 1) {
                return Eval.x(params[0], imports + code);
            } else if (params.length == 2) {
                return Eval.xy(params[0], params[1], imports + code);
            } else if (params.length == 3) {
                return Eval.xyz(params[0], params[1], params[2], imports + code);
            } else {
                throw new UnsupportedOperationException("up to 3 parameters <> " + params.length);
            }
        }
        return Eval.me(imports + code);
    }
}
