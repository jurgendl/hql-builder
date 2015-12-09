package org.tools.hqlbuilder.common.test;

import java.util.Collections;

import org.junit.Test;
import org.tools.hqlbuilder.common.EntityRelationHelper;
import org.tools.hqlbuilder.common.EntityRelationHelper.EntityRelationException;

@SuppressWarnings("unchecked")
public class EntityRelationTest extends org.junit.Assert {
    @Test
    public void test() {
        new EntityRelationHelper<Dummy>(new Dummy()).toString();
    }

    @Test
    public void testManyToMany() {
        ManyToMany mm = new ManyToMany();
        ManyToManyBack mb = new ManyToManyBack();

        mm.setManyToManyBack(null);
        // mm.addManyToManyBack(null);
        // mm.removeManyToManyBack(null);

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

        mb.setManyToMany(null);
        // mb.addManyToMany(null);
        // mb.removeManyToMany(null);

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

        om.setManyToOne(null);

        mo.setOneToMany(null);
        // mo.addOneToMany(null);
        // mo.removeOneToMany(null);

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
    public void testManyToOne() {
        OneToMany om = new OneToMany();
        ManyToOne mo = new ManyToOne();

        om.setManyToOne(mo);
        assertTrue(mo.getOneToMany().contains(om));
        assertTrue(mo.getOneToMany().size() == 1);

        om.setManyToOne(mo);

        om.setManyToOne(null);
        assertTrue(mo.getOneToMany().isEmpty());
    }

    @Test
    public void testManyToOneAuto() {
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
        assertEquals(ob.getOneToOne(), null);
        assertEquals(oo.getOneToOneBack(), null);

        oo.setOneToOneBack(null);
        assertEquals(ob.getOneToOne(), null);
        assertEquals(oo.getOneToOneBack(), null);

        oo.setOneToOneBack(ob);
        assertEquals(ob.getOneToOne(), oo);
        assertEquals(oo.getOneToOneBack(), ob);

        oo.setOneToOneBack(null);
        assertEquals(ob.getOneToOne(), null);
        assertEquals(oo.getOneToOneBack(), null);

        OneToOneBack obb = new OneToOneBack();
        oo.setOneToOneBack(ob);
        oo.setOneToOneBack(obb);
        assertEquals(ob.getOneToOne(), null);
        assertEquals(obb.getOneToOne(), oo);
        assertEquals(oo.getOneToOneBack(), obb);
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

        ob.setOneToOne(null);

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
    public void testSimpleManyToMany() {
        SimpleManyToMany smm = new SimpleManyToMany();
        Dummy d = new Dummy();

        smm.addDummy(d);
        assertTrue(smm.getDummy().contains(d));
        assertEquals(smm.getDummy().size(), 1);

        smm.removeDummy(d);
        assertEquals(smm.getDummy().size(), 0);

        smm.removeDummy(d);

        smm.addDummy(d);
        assertTrue(smm.getDummy().contains(d));
        assertEquals(smm.getDummy().size(), 1);

        smm.addDummy(d);
        smm.clearDummy();
        assertEquals(smm.getDummy().size(), 0);

        smm.addDummy(d);
        smm.setDummy(Collections.EMPTY_LIST);
        assertEquals(smm.getDummy().size(), 0);

        smm.addDummy(d);
        smm.setDummy(null);
        assertEquals(smm.getDummy().size(), 0);
    }

    @Test
    public void testSimpleManyToOne() {
        SimpleManyToOne smo = new SimpleManyToOne();
        Dummy d = new Dummy();

        smo.setDummy(d);
        assertEquals(smo.getDummy(), d);

        smo.setDummy(d);
        assertEquals(smo.getDummy(), d);

        smo.setDummy(null);
        assertEquals(smo.getDummy(), null);
    }

    @Test
    public void testSimpleOneToMany() {
        SimpleOneToMany som = new SimpleOneToMany();
        Dummy d = new Dummy();

        som.addDummy(d);
        assertTrue(som.getDummy().contains(d));
        assertEquals(som.getDummy().size(), 1);

        som.removeDummy(d);
        assertEquals(som.getDummy().size(), 0);

        som.removeDummy(d);

        som.addDummy(d);
        assertTrue(som.getDummy().contains(d));
        assertEquals(som.getDummy().size(), 1);

        som.addDummy(d);
        som.clearDummy();
        assertEquals(som.getDummy().size(), 0);

        som.addDummy(d);
        som.setDummy(Collections.EMPTY_LIST);
        assertEquals(som.getDummy().size(), 0);

        som.addDummy(d);
        som.setDummy(null);
        assertEquals(som.getDummy().size(), 0);

    }

    @Test
    public void testSimpleOneToOne() {
        SimpleOneToOne soo = new SimpleOneToOne();
        Dummy d = new Dummy();

        soo.setDummy(d);
        assertEquals(soo.getDummy(), d);

        soo.setDummy(d);
        assertEquals(soo.getDummy(), d);

        soo.setDummy(null);
        assertEquals(soo.getDummy(), null);
    }

    @Test
    public void testSortedSet() {
        WithSortedSet wss = new WithSortedSet();
        wss.setDummy(null);
    }

    @Test
    public void testIllegalField() {
        IllegalField err = new IllegalField();
        Dummy dummy = new Dummy();

        try {
            err.setDummy(dummy);
            fail("expected EntityRelationException");
        } catch (EntityRelationException ex) {
            assertTrue(true);
        }

        try {
            err.setDummys(Collections.singleton(dummy));
            fail("expected EntityRelationException");
        } catch (EntityRelationException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void ooTest() {
        PTest p = new PTest();
        ETest e = new ETest();
        e.setP(p);
    }

    @Test
    public void testSimpleBi() {
        ToMany x = new ToMany();
        FromMany bi = new FromMany();
        x.addBidirectional(bi);
        x.removeBidirectional(bi);
    }

    // @Test
    // public void testSimpleUni() {
    // ToMany x = new ToMany();
    // FromMany uni = new FromMany();
    // x.addUnidirectional(uni);
    // x.removeUnidirectional(uni);
    // }
}
