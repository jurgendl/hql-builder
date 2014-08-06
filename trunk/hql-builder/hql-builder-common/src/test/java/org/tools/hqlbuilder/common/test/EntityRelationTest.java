package org.tools.hqlbuilder.common.test;

import java.util.Collections;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class EntityRelationTest extends org.junit.Assert {
    @Test
    public void testManyToMany() {
        ManyToMany mm = new ManyToMany();
        ManyToManyBack mb = new ManyToManyBack();

        mm.addManyToManyBack(mb);
        assertTrue(mb.getManyToMany().contains(mm));

        mm.removeManyToManyBack(mb);
        assertFalse(mb.getManyToMany().contains(mm));

        mm.removeManyToManyBack(mb);

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

        mm.addManyToManyBack(mb);
        mm.addManyToManyBack(mb);
        assertTrue(mb.getManyToMany().contains(mm));
        assertEquals(mb.getManyToMany().size(), 1);

        mm.setManyToManyBack(null);
        assertEquals(mb.getManyToMany().size(), 0);

        mm.addManyToManyBack(mb);
        mm.setManyToManyBack(Collections.EMPTY_SET);
        assertEquals(mb.getManyToMany().size(), 0);
    }

    @Test
    public void testManyToManyInvers() {
        ManyToManyBack mb = new ManyToManyBack();
        ManyToMany mm = new ManyToMany();

        mb.addManyToMany(mm);
        assertTrue(mm.getManyToManyBack().contains(mb));

        mb.removeManyToMany(mm);
        assertFalse(mm.getManyToManyBack().contains(mb));

        mb.removeManyToMany(mm);

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

        mb.addManyToMany(mm);
        mb.addManyToMany(mm);
        assertTrue(mb.getManyToMany().contains(mm));
        assertEquals(mb.getManyToMany().size(), 1);

        mb.setManyToMany(null);
        assertEquals(mb.getManyToMany().size(), 0);

        mb.addManyToMany(mm);
        mb.setManyToMany(Collections.EMPTY_SET);
        assertEquals(mb.getManyToMany().size(), 0);
    }

    @Test
    public void testOneToMany() {
        ManyToOne mo = new ManyToOne();
        OneToMany om = new OneToMany();

        mo.addOneToMany(om);
        assertEquals(om.getManyToOne(), mo);

        mo.removeOneToMany(om);
        assertEquals(om.getManyToOne(), null);

        mo.removeOneToMany(om);

        mo.addOneToMany(om);
        mo.clearOneToMany();
        assertEquals(om.getManyToOne(), null);

        mo.setOneToMany(Collections.singleton(om));
        assertEquals(om.getManyToOne(), mo);

        mo.addOneToMany(om);
        mo.addOneToMany(om);
        assertEquals(om.getManyToOne(), mo);
        assertEquals(mo.getOneToMany().size(), 1);

        mo.setOneToMany(null);
        assertEquals(mo.getOneToMany().size(), 0);

        mo.addOneToMany(om);
        mo.setOneToMany(Collections.EMPTY_SET);
        assertEquals(mo.getOneToMany().size(), 0);
    }

    @Test
    public void testOneToManyInvers() {
        OneToMany om = new OneToMany();
        ManyToOne mo = new ManyToOne();

        om.setManyToOne(mo);
        assertTrue(mo.getOneToMany().contains(om));
        assertTrue(mo.getOneToMany().size() == 1);

        om.setManyToOne(null);
        assertTrue(mo.getOneToMany().size() == 0);
    }

    @Test
    public void testOneToManyInversAuto() {
        OneToMany om = new OneToMany();
        ManyToOne mo = new ManyToOne();
        ManyToOne moX = new ManyToOne();

        om.setManyToOne(mo);
        assertEquals(om.getManyToOne(), mo);
        assertTrue(mo.getOneToMany().contains(om));
        assertFalse(moX.getOneToMany().contains(om));

        om.setManyToOne(moX);
        assertEquals(om.getManyToOne(), moX);
        assertFalse(mo.getOneToMany().contains(om));
        assertTrue(moX.getOneToMany().contains(om));
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

    @Test
    public void testManyToManyNull() {
        ManyToMany mm = new ManyToMany();

        mm.setManyToManyBack(null);
        mm.addManyToManyBack(null);
        mm.removeManyToManyBack(null);
    }

    @Test
    public void testOneToOneNull() {
        OneToOne oo = new OneToOne();

        oo.setOneToOneBack(null);
    }

    @Test
    public void testOneToManyNull() {
        ManyToOne mo = new ManyToOne();

        mo.setOneToMany(null);
        mo.addOneToMany(null);
        mo.removeOneToMany(null);
    }

    @Test
    public void testManyToOneNull() {
        OneToMany om = new OneToMany();

        om.setManyToOne(null);
    }
}
