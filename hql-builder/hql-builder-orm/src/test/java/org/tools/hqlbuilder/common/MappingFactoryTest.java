package org.tools.hqlbuilder.common;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class MappingFactoryTest {
    private MappingFactory mappingFactory = new MappingFactory();

    @Test
    public void test() {
        try {
            Mapping<Pojo, DTO> mapping = mappingFactory.build(Pojo.class, DTO.class)//
                    .add((s, t) -> t.setNestedDTOVeld(s.getNestedPojo().getNestedVeld()))//
                    .add((s, t) -> t.getNestedDTO().setNestedVeld(s.getNestedPojoVeld()))//
                    .collect(mappingFactory, "collection", "collection", () -> new ArrayList<CommonNestedDTO>(), CommonNestedDTO.class)//
                    ;
            mappingFactory.build(CommonNestedPojo.class, CommonNestedDTO.class);
            Pojo pojo = getTestPojo();
            DTO dto = mapping.map(mappingFactory, pojo);
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
}