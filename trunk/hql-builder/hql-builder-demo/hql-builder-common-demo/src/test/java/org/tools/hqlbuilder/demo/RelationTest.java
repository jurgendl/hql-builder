package org.tools.hqlbuilder.demo;

import java.util.Collections;

import org.junit.Test;

public class RelationTest extends org.junit.Assert {
    @Test
    public void testManyToMany() {
        ManyToMany mm = new ManyToMany();
        ManyToManyBack mb = new ManyToManyBack();

        mm.addManyToManyBack(mb);
        assertTrue(mb.getManyToMany().contains(mm));

        mm.removeManyToManyBack(mb);
        assertFalse(mb.getManyToMany().contains(mm));

        mm.addManyToManyBack(mb);
        mm.clearManyToManyBack();
        assertFalse(mb.getManyToMany().contains(mm));
    }

    @Test
    public void testManyToManyInvers() {
        ManyToManyBack mb = new ManyToManyBack();
        ManyToMany mm = new ManyToMany();

        mb.addManyToMany(mm);
        assertTrue(mm.getManyToManyBack().contains(mb));

        mb.removeManyToMany(mm);
        assertFalse(mm.getManyToManyBack().contains(mb));

        mb.addManyToMany(mm);
        mb.clearFromManyToMany();
        assertFalse(mm.getManyToManyBack().contains(mb));
    }

    @Test
    public void testOneToMany() {
        OneToMany om = new OneToMany();
        ManyToOne mo = new ManyToOne();

        om.addManyToOne(mo);
        assertEquals(mo.getOneToMany(), om);

        om.removeManyToOne(mo);
        assertEquals(mo.getOneToMany(), null);

        om.addManyToOne(mo);
        om.clearFromManyToOne();
        assertEquals(mo.getOneToMany(), null);

        om.setManyToOne(Collections.singleton(mo));
        assertEquals(mo.getOneToMany(), om);
    }

    @Test
    public void testOneToManyInvers() {
        ManyToOne mo = new ManyToOne();
        OneToMany om = new OneToMany();

        mo.setOneToMany(om);
        assertTrue(om.getManyToOne().contains(mo));
        assertTrue(om.getManyToOne().size() == 1);

        mo.setOneToMany(null);
        assertTrue(om.getManyToOne().size() == 0);
    }

    @Test
    public void testOneToOne() {
        OneToOne oo = new OneToOne();
        OneToOneBack ob = new OneToOneBack();

        oo.setOneToOneBack(ob);
        assertEquals(ob.getOneToOne(), oo);

        oo.setOneToOneBack(null);
        assertEquals(ob.getOneToOne(), null);
    }

    @Test
    public void testOneToOneInvers() {
        OneToOneBack ob = new OneToOneBack();
        OneToOne oo = new OneToOne();

        ob.setOneToOne(oo);
        assertEquals(oo.getOneToOneBack(), ob);

        ob.setOneToOne(null);
        assertEquals(oo.getOneToOneBack(), null);
    }
}
