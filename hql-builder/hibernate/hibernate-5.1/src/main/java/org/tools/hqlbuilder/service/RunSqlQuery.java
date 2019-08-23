package org.tools.hqlbuilder.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.QueryParameter;

public class RunSqlQuery extends RunQuery {
    @Override
    public void query(SessionFactory sessionFactory, Session session, String uuid, String sqlString, int max, int first,
            List<QueryParameter> queryParameters, ExecutionResult result) {
        long start = System.currentTimeMillis();
        String sql = sqlString;
        logger.info("sql={}", sql);
        result.setSql(sql);
        boolean isUpdateStatement = false;
        SQLQuery createQuery = session.createSQLQuery(sql);
        int index = 0;
        if (queryParameters != null) {
            for (QueryParameter value : queryParameters) {
                try {
                    Object valueCompiled = value.getValue();
                    if (value.getName() != null) {
                        if (valueCompiled instanceof Collection) {
							Object[] l = new ArrayList<>((Collection<?>) valueCompiled).toArray();
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
        if (max > 0) {
            createQuery.setMaxResults(max);
        }
        if (first > 0) {
            createQuery.setFirstResult(first);
        }
        // result.setQueryReturnAliases(queryReturnAliases);
        // result.setScalarColumnNames(scalarColumnNames);
        // result.setSqlAliases(sqlAliases);
        long startTime = System.currentTimeMillis();
        List<?> list = createQuery.list();
        long endTime = System.currentTimeMillis();
        result.setSize(list.size());
        String[] queryReturnTypeNames = new String[0];
        List<Serializable> serializableList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object record = list.get(i);
            if (i == 0) {
                if (record instanceof Object[]) {
                    Object[] row = (Object[]) record;
                    queryReturnTypeNames = new String[row.length];
                    for (int j = 0; j < row.length; j++) {
                        queryReturnTypeNames[j] = row[j].getClass().getName();
                    }
                } else {
                    queryReturnTypeNames = new String[1];
                    queryReturnTypeNames[i] = record.getClass().getName();
                }
            }
            if (record instanceof Object[]) {
                Object[] row = (Object[]) record;
                Serializable[] serializables = new Serializable[row.length];
                for (int j = 0; j < row.length; j++) {
                    serializables[j] = serialize(row[j]);
                }
                serializableList.add(serializables);
            } else {
                serializableList.add(serialize(record));
            }
        }
        result.setSimpleResults(serializableList);
        result.setQueryReturnTypeNames(queryReturnTypeNames);
        long duration = System.currentTimeMillis() - start;
        result.setDuration(endTime - startTime);
        result.setOverhead(duration - result.getDuration());
        logger.info("DONE: " + uuid);
    }

    private Serializable serialize(Object object) {
        if (object == null) return null;
        if (object instanceof Serializable) return Serializable.class.cast(object);
        return String.valueOf(object);
    }
}
