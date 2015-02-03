package org.tools.hqlbuilder.common;

import org.junit.Assert;
import org.junit.Test;

public class MappingFactoryTest {
    private static final Class<DTO> TC = DTO.class;

    private static final Class<Pojo> SC = Pojo.class;

    private MappingFactory mappingFactory = new MappingFactory();

    @Test
    public void test() {
        Pojo pojo = getTestPojo();
        Mapping<Pojo, DTO> mapping = mappingFactory.build(SC, TC)//
                .redirect((s, t) -> t.setNestedDTOVeld(s.getNestedPojo().getNestedVeld()))//
        // .redirect((s, t) -> t.getNestedDTO().setNestedVeld(s.getNestedPojoVeld()))//
                ;
        DTO dto = mapping.map(pojo);
        Assert.assertEquals(pojo.getVeld1(), dto.getVeld1());
        Assert.assertEquals(pojo.getVeld2(), dto.getVeld2().intValue());
        Assert.assertEquals(pojo.getVeld3(), dto.getVeld3());
        Assert.assertEquals(pojo.getNestedPojo().getNestedVeld(), dto.getNestedDTOVeld());
        // Assert.assertEquals(pojo.getNestedPojoVeld(), dto.getNestedDTO().getNestedVeld());
    }

    private Pojo getTestPojo() {
        Pojo pojo = new Pojo();
        pojo.setVeld1("veld1");
        pojo.setVeld2(100);
        pojo.setVeld3("veld3");
        return pojo;
    }
}
