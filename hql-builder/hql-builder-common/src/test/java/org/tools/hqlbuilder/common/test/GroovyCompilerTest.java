package org.tools.hqlbuilder.common.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.tools.hqlbuilder.common.GroovyCompiler;


public class GroovyCompilerTest {
    @Test
    public void testDefoLong() {
        Assert.assertEquals(Long.class, GroovyCompiler.eval("1").getClass());
    }

    @Test
    public void testDefoCalc() {
        Assert.assertEquals(2l, GroovyCompiler.eval("max(1,2)"));
    }

    @Test
    public void testDefoLongCalc() {
        Assert.assertEquals(Long.class, GroovyCompiler.eval("max(1,2)").getClass());
    }

    @Test
    public void testDefoJoda() {
        Assert.assertEquals(org.joda.time.LocalDate.class, GroovyCompiler.eval("new LocalDate()").getClass());
    }

    @Test
    public void testDefoCalcArray() {
        Object eval = GroovyCompiler.eval("[1,2]");
        Assert.assertTrue(List.class.isAssignableFrom(eval.getClass()));
        Assert.assertEquals(2, ((List<?>) eval).size());
        Assert.assertEquals(Long.class, ((List<?>) eval).get(0).getClass());
    }

    @Test
    public void testForceInt() {
        Assert.assertEquals(Integer.class, GroovyCompiler.eval("(int) 2").getClass());
    }

    @Test
    public void testBlockExit() {
        // FIXME GroovyCompiler.eval("java.lang.System.exit(1)");
    }

    @Test
    public void testSomething() {
        Object o = GroovyCompiler
                .eval("[param0:25, param1:84296, param3$0$:['/uri/part']]");
        Map<String, Object> m = new HashMap<>();
        m.put("param0", 25l);
        m.put("param1", 84296l);
        m.put("param3$0$", Arrays.asList("/uri/part"));
        Assert.assertEquals(m, o);
    }

    @Test
    public void testSomethingElse() {
        Object o = GroovyCompiler.eval("[param0:'bronId1%', param1:20]");
        Map<String, Object> m = new HashMap<>();
        m.put("param0", "bronId1%");
        m.put("param1", 20l);
        Assert.assertEquals(m, o);
    }

    @Test
    public void testSomethingElseToo() {
        Object o = GroovyCompiler.eval("[param0:bronId1, param1:20]");
        Map<String, Object> m = new HashMap<>();
        m.put("param0", "bronId1");
        m.put("param1", 20l);
        Assert.assertEquals(m, o);
    }

    @Test
    public void testLocalDate() {
        Object o = GroovyCompiler.eval("new LocalDate(2000,1,1)");
        Assert.assertEquals(new LocalDate(2000, 1, 1), o);
        o = GroovyCompiler.eval("[param0:new LocalDate(2000,1,1), param1:20]"); // *1* LocalDate doesn't accept long values
        Map<String, Object> m = new HashMap<>();
        m.put("param0", new LocalDate(2000, 1, 1));
        m.put("param1", 20); // isn't a long value as expected because of *1*
        Assert.assertEquals(m, o);
    }

    public static enum GCEnum {
        test;
    }

    @Test
    public void testEnum() {
        Object o = GroovyCompiler.eval("org.tools.hqlbuilder.common.test.GroovyCompilerTest.GCEnum.test");
        Assert.assertEquals(GCEnum.test, o);
    }
}
