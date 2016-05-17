package org.tools.hqlbuilder.service;

import java.util.Map;

import org.hibernate.hql.internal.ast.QueryTranslatorImpl;

public class QueryTranslator extends QueryTranslatorImpl {
    public QueryTranslator(String queryIdentifier, String query, @SuppressWarnings("rawtypes") Map enabledFilters, Object factory) {
        super(queryIdentifier, query, enabledFilters, (org.hibernate.engine.spi.SessionFactoryImplementor) factory);
    }
}
