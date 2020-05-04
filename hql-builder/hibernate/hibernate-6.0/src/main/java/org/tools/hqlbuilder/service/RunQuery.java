package org.tools.hqlbuilder.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.type.Type;
import org.jhaws.common.lang.ObjectWrapper;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.QueryParameter;

public class RunQuery {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(RunQuery.class);

    protected static final String SQL_ALIAS_BY_ENTITY_ALIAS = "sqlAliasByEntityAlias";

    protected static final String SQL_ALIASES = "sqlAliases";

    protected static final String ENTITY_PERSISTERS = "entityPersisters";

    protected static final String ENTITY_ALIASES = "entityAliases";

    protected static final String SCALAR_COLUMN_NAMES = "scalarColumnNames";

    protected static final String QUERY_RETURN_ALIASES = "queryReturnAliases";

    protected static final String QUERY_RETURN_TYPES = "queryReturnTypes";

    protected static final String DOT = ".";

    protected static final String QUERY_LOADER = "queryLoader";

    protected static final String QUERY_IDENTIFIER = "queryIdentifier";

    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    public void query(SessionFactory sessionFactory, Session session, String uuid, String hql, int max, int first,
            List<QueryParameter> queryParameters, ExecutionResult result) {
        long start = System.currentTimeMillis();
        QueryTranslator queryTranslator = new QueryTranslator(QUERY_IDENTIFIER, hql, new HashMap<>(), sessionFactory);
        String sql = queryTranslator.getSQLString();
        logger.info("sql={}", sql);
        result.setSql(sql);
        boolean isUpdateStatement = hql.trim().toLowerCase().startsWith("update");
		org.hibernate.query.Query createQuery = session.createQuery(hql);
        int index = 0;
        if (queryParameters != null) {
            for (QueryParameter value : queryParameters) {
                try {
                    Object valueCompiled = value.getValue();
                    if (value.getName() != null) {
                        if (valueCompiled instanceof Collection) {
                            Object[] l = new ArrayList((Collection) valueCompiled).toArray();
                            createQuery.setParameterList(value.getName(), l);
                        } else {
                            createQuery.setParameter(value.getName(), valueCompiled);
                        }
                    } else {
                        createQuery.setParameter(index++, valueCompiled);
                    }
                } catch (IllegalArgumentException ex) {
                    // java.lang.IllegalArgumentException: Unknown parameter name
                    logger.debug("{}", String.valueOf(ex)); // => whatever
                } catch (org.hibernate.QueryParameterException ex) {
                    // org.hibernate.QueryParameterException: could not locate
                    // named parameter [nummer]
                    logger.debug("{}", String.valueOf(ex)); // => whatever
                } catch (java.lang.IndexOutOfBoundsException ex) {
                    // java.lang.IndexOutOfBoundsException: Remember that
                    // ordinal parameters are 1-based!
                    logger.debug("{}", String.valueOf(ex)); // => whatever
                }
            }
        }
        if (isUpdateStatement) {
            result.setSize(createQuery.executeUpdate());
            return;
        }
        queryTranslator.compile(new HashMap<>(), false);
        if (sql == null) {
            sql = queryTranslator.getSQLString();
            if (sql == null) {
                String tmp = new ObjectWrapper(queryTranslator).get(QUERY_LOADER).toString();
                sql = tmp.substring(tmp.indexOf("(") + 1, tmp.length() - 1);
            }
            logger.info("sql={}", sql);
            result.setSql(sql);
        }
        if (max > 0) {
            createQuery.setMaxResults(max);
        }
        if (first > 0) {
            createQuery.setFirstResult(first);
        }
        String QLD = QUERY_LOADER + DOT;
        Type[] queryReturnTypes = get(queryTranslator, QLD + QUERY_RETURN_TYPES, Type[].class);
        String[] queryReturnAliases = get(queryTranslator, QLD + QUERY_RETURN_ALIASES, String[].class);
        result.setQueryReturnAliases(queryReturnAliases);
        String[][] scalarColumnNames = get(queryTranslator, QLD + SCALAR_COLUMN_NAMES, String[][].class);
        result.setScalarColumnNames(scalarColumnNames);
        String[] entityAliases = get(queryTranslator, QLD + ENTITY_ALIASES, String[].class);
        Queryable[] entityPersisters = get(queryTranslator, QLD + ENTITY_PERSISTERS, Queryable[].class);
        String[] sqlAliases = get(queryTranslator, QLD + SQL_ALIASES, String[].class);
        result.setSqlAliases(sqlAliases);

        @SuppressWarnings("unused")
        Map<String, String> sqlAliasByEntityAlias = get(queryTranslator, QLD + SQL_ALIAS_BY_ENTITY_ALIAS, Map.class);
        Map<String, String> from_aliases = new HashMap<>();
        result.setFromAliases(from_aliases);
        for (int i = 0; i < entityAliases.length; i++) {
            String alias = entityAliases[i];
            if (alias != null) {
                from_aliases.put(alias, entityPersisters[i].getClassMetadata().getEntityName());
            }
        }
        long startTime = System.currentTimeMillis();
        List<Serializable> list = createQuery.list();
        long endTime = System.currentTimeMillis();
        result.setSimpleResults(list);
        result.setSize(list.size());
        String[] queryReturnTypeNames = new String[queryReturnTypes.length];
        for (int i = 0; i < queryReturnTypes.length; i++) {
            queryReturnTypeNames[i] = queryReturnTypes[i].getReturnedClass().getName();
        }
        result.setQueryReturnTypeNames(queryReturnTypeNames);
        long duration = System.currentTimeMillis() - start;
        result.setDuration(endTime - startTime);
        result.setOverhead(duration - result.getDuration());
        logger.info("DONE: " + uuid);
    }

    protected <T> T get(Object o, String path, Class<T> t) {
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("path");
        }
        return t.cast(new ObjectWrapper(o).get(path));
    }
}
