package org.tools.hqlbuilder.webservice.services;

import org.tools.hqlbuilder.webservice.wicket.pages.Example;

public interface ServiceInterface {
    public abstract Example getExample(String id);

    public abstract void save(String id, Example example);
}
