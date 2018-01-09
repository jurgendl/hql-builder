package org.tools.hqlbuilder.service;

import java.util.Map;

import org.hibernate.SessionFactory;

public class MetadataResolver {
    public Map<String, ?> getAllClassMetadata(SessionFactory sessionFactory) {
        return sessionFactory.getAllClassMetadata();
    }
}
