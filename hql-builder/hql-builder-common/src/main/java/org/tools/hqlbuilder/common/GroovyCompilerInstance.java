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
public class GroovyCompilerInstance {
    private Binding binding = new Binding();

    private boolean intToLong = true;

    private boolean literals = false;

    private GroovyShell sh;

    public GroovyCompilerInstance() {
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
                        // System.out.println(exp);
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
                            if (literals) {
                                VariableExpression v = VariableExpression.class.cast(exp);
                                if (!vars.contains(v.getText())) {
                                    return new ConstantExpression(v.getText());
                                }
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
                // .addCompilationCustomizers(new
                // SecureASTCustomizer().setImportsBlacklist(...)...)
                .addCompilationCustomizers(conversionCustomizer);
        // compilationCustomizer.setTolerance(0);
        sh = new GroovyShell(//
                Thread.currentThread().getContextClassLoader(), //
                binding, //
                compilationCustomizer);
    }

    public Object eval(String code) {
        return eval(code, (Map<String, Object>) null);
    }

    public Object eval(String code, Object x) {
        return eval(code, Collections.singletonMap("x", x));
    }

    public Object eval(String code, Map<String, Object> params) {
        return eval(0, code, params);
    }

    protected Object eval(int depth, String code, Map<String, Object> params) {
        System.out.println("pass " + depth + ": " + code);
        if (depth > 2) {
            return internal("'" + code + "'", params);
        }
        try {
            return internal(code, params);
        } catch (groovy.lang.MissingPropertyException mpe) {
            System.out.println(mpe);
            try {
                literals = true;
                System.out.println("literals = true");
                return eval(++depth, code, params);
            } finally {
                literals = false;
                System.out.println("literals = false");
            }
        } catch (groovy.lang.GroovyRuntimeException grte) {
            System.out.println(grte);
            try {
                intToLong = false;
                System.out.println("intToLong = false");
                return eval(++depth, code, params);
            } finally {
                System.out.println("intToLong = true");
                intToLong = true;
            }
        }
    }

    protected synchronized Object internal(String expression, Map<String, Object> params) {
        System.out.println("expression=" + expression);
        binding.getVariables().clear();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                binding.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return sh.evaluate(expression);
    }
}
