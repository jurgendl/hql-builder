package org.tools.hqlbuilder.common.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tools.hqlbuilder.common.GroovyCompiler;

public class GroovyCompilerTest {
    public String toString(Object o) {
        return o.toString().replace("{", "[").replace("}", "]").replace("=", ":").replace("%", "PERCENT");
    }

    @Test
    public void testDefoLong() {
		Assertions.assertEquals(Long.class, GroovyCompiler.eval("1").getClass());
    }

    @Test
    public void testDefoCalc() {
		Assertions.assertEquals(2l, GroovyCompiler.eval("max(1,2)"));
    }

    @Test
    public void testDefoLongCalc() {
		Assertions.assertEquals(Long.class, GroovyCompiler.eval("max(1,2)").getClass());
    }

    @Test
    public void testDefoJoda() {
		Assertions.assertEquals(org.joda.time.LocalDate.class, GroovyCompiler.eval("new LocalDate()").getClass());
    }

    @Test
    public void testDefoCalcArray() {
        Object eval = GroovyCompiler.eval("[1,2]");
		Assertions.assertTrue(List.class.isAssignableFrom(eval.getClass()));
		Assertions.assertEquals(2, ((List<?>) eval).size());
		Assertions.assertEquals(Long.class, ((List<?>) eval).get(0).getClass());
    }

    @Test
    public void testForceInt() {
		Assertions.assertEquals(Integer.class, GroovyCompiler.eval("(int) 2").getClass());
    }

    @Test
    public void testBlockExit() {
        // FIXME GroovyCompiler.eval("java.lang.System.exit(1)");
    }

    @Test
    public void testSomething() {
        Map<String, Object> m = new HashMap<>();
        m.put("param0", 25l);
        m.put("param1", 84296l);
        m.put("param3$0$", Arrays.asList("/uri/part"));
		Assertions.assertEquals(m, GroovyCompiler.eval("[param0:25, param1:84296, param3$0$:['/uri/part']]"));
    }

    @Test
    public void testSomethingElse() {
        Map<String, Object> m = new HashMap<>();
        m.put("param0", "bronId1%");
        m.put("param1", 20l);
		Assertions.assertEquals(m, GroovyCompiler.eval(toString(m)));
    }

    @Test
    public void testSomethingElseToo() {
        Map<String, Object> m = new HashMap<>();
        m.put("param0", "bronId1");
        m.put("param1", 20l);
		Assertions.assertEquals(m, GroovyCompiler.eval(toString(m)));
    }

    @Test
    public void testLocalDate() {
		Assertions.assertEquals(new LocalDate(2000, 1, 1), GroovyCompiler.eval("new LocalDate(2000,1,1)"));
        Map<String, Object> m = new HashMap<>();
        m.put("param0", new LocalDate(2000, 1, 1));
        m.put("param1", 20); // isn't a long value as expected because of *1*
		Assertions.assertEquals(m, GroovyCompiler.eval("[param0:new LocalDate(2000,1,1), param1:20]"));// *1*
																										// LocalDate
																										// doesn't
																										// accept
																										// long
																										// values
    }

    public static enum GCEnum {
        test;
    }

    @Test
    public void testEnum() {
        Object o = GroovyCompiler.eval("org.tools.hqlbuilder.common.test.GroovyCompilerTest.GCEnum.test");
		Assertions.assertEquals(GCEnum.test, o);
    }
}
