package org.tools.hqlbuilder.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;

import javassist.Modifier;

public class MetadataResolver {
    private static class ME<K, V> implements Map.Entry<K, V> {
        K k;

        V v;

        public ME(K k, V v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public V getValue() {
            return v;
        }

        @Override
        public V setValue(V value) {
            this.v = value;
            return value;
        }

        @Override
        public K getKey() {
            return k;
        }
    }

    @SuppressWarnings("deprecation")
    public Map<String, ?> getAllClassMetadata(SessionFactory sessionFactory) {
        return sessionFactory.getMetamodel()
                .getEntities()
                .stream()
                .map(javax.persistence.metamodel.EntityType::getJavaType)
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .map(c -> new ME<>(c.getName(), sessionFactory.getClassMetadata(c)))
                .collect(Collectors.toMap(ME::getKey, ME::getValue));
    }
}
