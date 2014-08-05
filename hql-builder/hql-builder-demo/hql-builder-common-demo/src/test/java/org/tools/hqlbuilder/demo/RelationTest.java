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

        mm.setManyToManyBack(Collections.singleton(mb));
        assertTrue(mb.getManyToMany().contains(mm));

        mm.addManyToManyBack(mb);
        mm.clearManyToManyBack();
        assertFalse(mb.getManyToMany().contains(mm));

        ManyToManyBack mbe = new ManyToManyBack();
        mm.addManyToManyBack(mb);
        mm.addManyToManyBack(mbe);
        assertEquals(mm.getManyToManyBack().size(), 2);
        assertEquals(mb.getManyToMany().size(), 1);
        assertEquals(mbe.getManyToMany().size(), 1);

        mm.removeManyToManyBack(mbe);
        assertEquals(mm.getManyToManyBack().size(), 1);
        assertEquals(mb.getManyToMany().size(), 1);
        assertEquals(mbe.getManyToMany().size(), 0);

        mm.addManyToManyBack(mbe);
        mm.clearManyToManyBack();
        assertEquals(mm.getManyToManyBack().size(), 0);
        assertEquals(mb.getManyToMany().size(), 0);
        assertEquals(mbe.getManyToMany().size(), 0);
    }

    @Test
    public void testManyToManyInvers() {
        ManyToManyBack mb = new ManyToManyBack();
        ManyToMany mm = new ManyToMany();

        mb.addManyToMany(mm);
        assertTrue(mm.getManyToManyBack().contains(mb));

        mb.removeManyToMany(mm);
        assertFalse(mm.getManyToManyBack().contains(mb));

        mb.setManyToMany(Collections.singleton(mm));
        assertTrue(mm.getManyToManyBack().contains(mb));

        mb.addManyToMany(mm);
        mb.clearManyToMany();
        assertFalse(mm.getManyToManyBack().contains(mb));

        ManyToMany mme = new ManyToMany();
        mb.addManyToMany(mm);
        mb.addManyToMany(mme);
        assertEquals(mb.getManyToMany().size(), 2);
        assertEquals(mm.getManyToManyBack().size(), 1);
        assertEquals(mme.getManyToManyBack().size(), 1);

        mb.removeManyToMany(mme);
        assertEquals(mb.getManyToMany().size(), 1);
        assertEquals(mm.getManyToManyBack().size(), 1);
        assertEquals(mme.getManyToManyBack().size(), 0);

        mb.addManyToMany(mme);
        mb.clearManyToMany();
        assertEquals(mb.getManyToMany().size(), 0);
        assertEquals(mm.getManyToManyBack().size(), 0);
        assertEquals(mme.getManyToManyBack().size(), 0);
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
        om.clearManyToOne();
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
    public void testOneToManyInversAuto() {
        ManyToOne mo = new ManyToOne();
        OneToMany om = new OneToMany();
        OneToMany omX = new OneToMany();

        mo.setOneToMany(om);
        assertEquals(mo.getOneToMany(), om);
        assertTrue(om.getManyToOne().contains(mo));
        assertFalse(omX.getManyToOne().contains(mo));

        mo.setOneToMany(omX);
        assertEquals(mo.getOneToMany(), omX);
        assertFalse(om.getManyToOne().contains(mo));
        assertTrue(omX.getManyToOne().contains(mo));
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
    public void testOneToOneAuto() {
        OneToOne oo = new OneToOne();
        OneToOneBack ob = new OneToOneBack();
        OneToOneBack obX = new OneToOneBack();

        oo.setOneToOneBack(ob);
        assertEquals(ob.getOneToOne(), oo);
        assertEquals(obX.getOneToOne(), null);

        oo.setOneToOneBack(obX);
        assertEquals(ob.getOneToOne(), null);
        assertEquals(obX.getOneToOne(), oo);
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

    @Test
    public void testOneToOneInversAuto() {
        OneToOneBack ob = new OneToOneBack();
        OneToOne oo = new OneToOne();
        OneToOne ooX = new OneToOne();

        ob.setOneToOne(oo);
        assertEquals(oo.getOneToOneBack(), ob);
        assertEquals(ooX.getOneToOneBack(), null);

        ob.setOneToOne(ooX);
        assertEquals(oo.getOneToOneBack(), null);
        assertEquals(ooX.getOneToOneBack(), ob);
    }
}
