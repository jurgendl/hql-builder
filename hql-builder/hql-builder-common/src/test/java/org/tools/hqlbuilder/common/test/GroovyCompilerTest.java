package org.tools.hqlbuilder.common.test;

import java.util.List;

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
        GroovyCompiler.eval("max(1,2)");
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

}
