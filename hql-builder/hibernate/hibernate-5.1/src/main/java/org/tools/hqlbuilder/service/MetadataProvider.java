package org.tools.hqlbuilder.service;

import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryBuilderFactory;
import org.hibernate.boot.spi.SessionFactoryBuilderImplementor;

// https://stackoverflow.com/questions/34612019/programmatic-schemaexport-schemaupdate-with-hibernate-5-and-spring-4/34683767#34683767
public class MetadataProvider implements SessionFactoryBuilderFactory {
    private static MetadataImplementor metadata;

    @SuppressWarnings("static-access")
    @Override
    public SessionFactoryBuilder getSessionFactoryBuilder(MetadataImplementor metadata, SessionFactoryBuilderImplementor defaultBuilder) {
        this.metadata = metadata;
        return defaultBuilder; // Just return the one provided in the argument itself. All we care about is the metadata :)
    }

    public static MetadataImplementor getMetadata() {
        return metadata;
    }
}