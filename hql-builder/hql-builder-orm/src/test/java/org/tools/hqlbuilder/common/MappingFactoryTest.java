package org.tools.hqlbuilder.common;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class MappingFactoryTest {
    private MappingFactory mappingFactory = new MappingFactory();

    private Pojo getTestPojo() {
        Pojo pojo = new Pojo();
        pojo.setVeld1("veld1");
        pojo.setVeld2(100);
        pojo.setVeld3("veld3");
        {
            CommonNestedPojo commonNested = new CommonNestedPojo();
            commonNested.setCommonNestedField("commonNestedField");
            pojo.setCommonNested(commonNested);
        }
        {
            CommonNestedPojo commonNested = new CommonNestedPojo();
            commonNested.setCommonNestedField("commonNestedField");
            pojo.getCollection().add(commonNested);
        }
        return pojo;
    }

    @Test
    public void test1() {
        try {
            Mapping<Pojo, DTO> mapping = this.mappingFactory.build(Pojo.class, DTO.class)//
                    .add((s, t) -> t.setNestedDTOVeld(s.getNestedPojo().getNestedVeld()))//
                    .add((s, t) -> t.getNestedDTO().setNestedVeld(s.getNestedPojoVeld()))//
                    .collect(this.mappingFactory, "collection", "collection", () -> new ArrayList<CommonNestedDTO>(), CommonNestedDTO.class)//
            ;
            this.mappingFactory.build(CommonNestedPojo.class, CommonNestedDTO.class);
            Pojo pojo = this.getTestPojo();
            DTO dto = mapping.map(this.mappingFactory, pojo);
            Assert.assertEquals(pojo.getVeld1(), dto.getVeld1());
            Assert.assertEquals(pojo.getVeld2(), dto.getVeld2().intValue());
            Assert.assertEquals(pojo.getVeld3(), dto.getVeld3());
            Assert.assertEquals(pojo.getNestedPojo().getNestedVeld(), dto.getNestedDTOVeld());
            Assert.assertEquals(pojo.getNestedPojoVeld(), dto.getNestedDTO().getNestedVeld());
            Assert.assertEquals(pojo.getCommonNested().getCommonNestedField(), dto.getCommonNested().getCommonNestedField());
            Assert.assertEquals(pojo.getCollection().iterator().next().getCommonNestedField(), dto.getCollection().iterator().next()
                    .getCommonNestedField());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail();
        }
    }

    @Test
    public void test2() {
        MappingFactory mf = new MappingFactory();
        mf.build(Retro.class, RetroB.class);
        Retro r1 = new Retro();
        Retro r2 = new Retro();
        r1.setRetro(r2);
        r2.setRetro(r1);
        mf.map(r1, RetroB.class);
    }
}
