package org.tools.hqlbuilder.webservice.services;

import java.util.HashMap;
import java.util.Map;

import org.tools.hqlbuilder.webservice.wicket.pages.Example;

public class ServiceImpl implements ServiceInterface {
    private Map<String, Example> examples = new HashMap<String, Example>();

    @Override
    public Example getExample(String id) {
        if (!examples.containsKey(id)) {
            Example value = new Example();
            value.setId(id);
            examples.put(id, value);
        }
        return examples.get(id);
    }

    @Override
    public void save(String id, Example example) {
        examples.put(id, example);
    }
}
