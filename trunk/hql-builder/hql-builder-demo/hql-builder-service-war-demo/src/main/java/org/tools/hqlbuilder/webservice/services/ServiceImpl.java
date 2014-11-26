package org.tools.hqlbuilder.webservice.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.tools.hqlbuilder.webservice.wicket.pages.Example;

public class ServiceImpl implements ServiceInterface {
    private Map<String, Example> examples = new HashMap<String, Example>();

    @Override
    public Example getExample(String id) {
        if (!examples.containsKey(id)) {
            Example value = new Example();
            value.setId(id);
            List<String> options = new ArrayList<String>(new HashSet<String>(Arrays.asList(new Example().getLongText().toLowerCase()
                    .replaceAll("[^a-z ]", "").replaceAll("  ", " ").split(" "))));
            List<String> manyOptions = new ArrayList<String>();
            manyOptions.add(options.get(10));
            manyOptions.add(options.get(20));
            manyOptions.add(options.get(30));
            value.setManyOptions(manyOptions);
            examples.put(id, value);
        }
        return examples.get(id);
    }

    @Override
    public void save(String id, Example example) {
        examples.put(id, example);
    }
}
