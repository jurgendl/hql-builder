package org.tools.hqlbuilder.common;

import java.util.Map;

/**
 * @author Jurgen
 */
public class GroovyCompiler {
    protected static ThreadLocal<GroovyCompilerInstance> threadLocalGroovyCompilerInstance = new ThreadLocal<GroovyCompilerInstance>() {
        @Override
        protected GroovyCompilerInstance initialValue() {
            return new GroovyCompilerInstance();
        }
    };

    public static Object eval(String code) {
        return threadLocalGroovyCompilerInstance.get().eval(code);
    }

    public static Object eval(String code, Object x) {
        return threadLocalGroovyCompilerInstance.get().eval(code, x);
    }

    public static Object eval(String code, Map<String, Object> params) {
        return threadLocalGroovyCompilerInstance.get().eval(code, params);
    }
}
