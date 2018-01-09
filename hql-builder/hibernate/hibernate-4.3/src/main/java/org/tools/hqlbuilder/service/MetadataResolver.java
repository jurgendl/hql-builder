package org.tools.hqlbuilder.service;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

public class MetadataResolver {
    public Map<String, ClassMetadata> getAllClassMetadata(SessionFactory sessionFactory) {
        return sessionFactory.getAllClassMetadata();
    }
}
