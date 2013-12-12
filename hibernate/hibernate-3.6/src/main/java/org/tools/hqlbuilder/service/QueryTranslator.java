package org.tools.hqlbuilder.service;

import java.util.Map;

public class QueryTranslator extends org.hibernate.hql.ast.QueryTranslatorImpl {
	public QueryTranslator(String queryIdentifier, String query,
			@SuppressWarnings("rawtypes") Map enabledFilters, Object factory) {
		super(queryIdentifier, query, enabledFilters, (org.hibernate.engine.SessionFactoryImplementor)factory);
	}
}
