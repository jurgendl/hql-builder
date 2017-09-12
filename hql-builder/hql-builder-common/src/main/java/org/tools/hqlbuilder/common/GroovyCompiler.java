package org.tools.hqlbuilder.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeExpressionTransformer;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * @author Jurgen
 */
public class GroovyCompiler {
    static Binding binding = new Binding();

    static boolean intToLong = true;

    static GroovyShell sh;
    static {
        List<String> vars = Arrays.asList("context", "args");
        CompilationCustomizer conversionCustomizer = new CompilationCustomizer(CompilePhase.CONVERSION) {
            @Override
            public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
                new ClassCodeExpressionTransformer() {
                    @Override
                    protected SourceUnit getSourceUnit() {
                        return source;
                    }

                    public Expression transform(Expression exp) {
                        System.out.println(exp);
                        if (exp instanceof ConstantExpression) {
                            Object value = ConstantExpression.class.cast(exp).getValue();
                            if (value != null) {
                                if (value instanceof BigDecimal) {
                                    return new ConstantExpression(BigDecimal.class.cast(value).doubleValue());
                                }
                                if (value instanceof BigInteger) {
                                    return new ConstantExpression(BigInteger.class.cast(value).longValue());
                                }
                                if (intToLong && value instanceof Integer) {
                                    return new ConstantExpression(Integer.class.cast(value).longValue());
                                }
                            }
                        } else if (exp instanceof VariableExpression) {
                            VariableExpression v = VariableExpression.class.cast(exp);
                            if (!vars.contains(v.getText())) {
                                return new ConstantExpression(v.getText());
                            }
                        }
                        return super.transform(exp);
                    }
                }.visitClass(classNode);
            }
        };
        String[] packages = {
                "java.io",
                "java.net",
                "java.text",
                "java.sql",
                "java.math",
                "java.util",
                "java.util.regex",
                "org.joda.time",
                "org.apache.commons.lang3",
                "org.apache.commons.lang3.builder",
                "java.time" };
        ImportCustomizer imports = new ImportCustomizer()//
                .addStarImports(packages)//
                .addStaticStars("java.lang.Math")//
                .addStaticStars("java.util.Collections")//
                .addStaticStars("java.util.Arrays");
        CompilerConfiguration compilationCustomizer = new CompilerConfiguration()//
                .addCompilationCustomizers(imports)//
        // .addCompilationCustomizers(new SecureASTCustomizer().setStaticImportsBlacklist(...)...)
                .addCompilationCustomizers(conversionCustomizer)
        ;
        // compilationCustomizer.setTolerance(0);
        sh = new GroovyShell(//
                Thread.currentThread().getContextClassLoader(), //
                binding, //
                compilationCustomizer);
    }

    public static Object eval(String code) {
        return eval(code, (Map<String, Object>) null);
    }

    public static Object eval(String code, Object x) {
        return eval(code, Collections.singletonMap("x", x));
    }

    public static Object eval(String code, Map<String, Object> params) {
        try {
            return internal(code, params);
        } catch (Exception ex1) {
            System.err.println(ex1);
            try {
                intToLong = false;
                return internal(code, params);
            } catch (Exception ex2) {
                System.err.println(ex2);
                return internal("'" + code + "'", params);
            } finally {
                intToLong = true;
            }
        }
    }

    protected static synchronized Object internal(String expression, Map<String, Object> params) {
        binding.getVariables().clear();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                binding.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return sh.evaluate(expression);
    }
}
