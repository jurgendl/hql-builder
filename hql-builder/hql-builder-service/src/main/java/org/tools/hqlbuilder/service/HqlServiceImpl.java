package org.tools.hqlbuilder.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.ast.QueryTranslatorImpl;
import org.hibernate.impl.QueryImpl;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.OneToOneType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.HibernateWebResolver.ClassNode;
import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.common.ObjectWrapper;
import org.tools.hqlbuilder.common.QueryParameter;

public class HqlServiceImpl implements HqlService {
    @Autowired
    private SessionFactory sessionFactory;

    public HqlServiceImpl() {
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#getClasses()
     */
    @Override
    public List<String> getClasses() {
        List<String> classes = new ArrayList<String>();
        // TODO Auto-generated method stub
        return classes;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#getSqlForHql(java.lang.String)
     */
    @Override
    public String getSqlForHql(String hql) {
        QueryTranslatorImpl createQueryTranslator = new QueryTranslatorImpl("queryIdentifier", hql, new HashMap<Object, Object>(),
                (SessionFactoryImplementor) sessionFactory);
        createQueryTranslator.compile(new HashMap<Object, Object>(), false);
        return createQueryTranslator.getSQLString();
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#execute(java.lang.String, java.util.List)
     */
    @Override
    public ExecutionResult execute(String hql, List<QueryParameter> queryParameters) {
        QueryTranslatorImpl createQueryTranslator = new QueryTranslatorImpl("queryIdentifier", hql, new HashMap<Object, Object>(),
                (SessionFactoryImplementor) sessionFactory);
        createQueryTranslator.compile(new HashMap<Object, Object>(), false);

        Session session = sessionFactory.openSession();
        QueryImpl createQuery = (QueryImpl) session.createQuery(hql);
        int index = 0;

        for (QueryParameter value : queryParameters) {
            try {
                if (value.getName() != null) {
                    if (value.getValue() instanceof Collection) {
                        @SuppressWarnings({ "rawtypes", "unchecked" })
                        Object[] l = new ArrayList((Collection) value.getValue()).toArray();
                        createQuery.setParameterList(value.getName(), l);
                    } else {
                        createQuery.setParameter(value.getName(), value.getValue());
                    }
                } else {
                    createQuery.setParameter(index++, value.getValue());
                }
            } catch (org.hibernate.QueryParameterException ex) {
                // org.hibernate.QueryParameterException: could not locate named parameter [nummer]
                System.out.println(ex); // => whatever
            } catch (java.lang.IndexOutOfBoundsException ex) {
                // java.lang.IndexOutOfBoundsException: Remember that ordinal parameters are 1-based!
                System.out.println(ex); // => whatever
            }
        }

        Type[] queryReturnTypes = get(createQueryTranslator, "queryLoader.queryReturnTypes", Type[].class);
        String[] queryReturnAliases = get(createQueryTranslator, "queryLoader.queryReturnAliases", String[].class);
        String[][] scalarColumnNames = get(createQueryTranslator, "queryLoader.scalarColumnNames", String[][].class);
        String[] entityAliases = get(createQueryTranslator, "queryLoader.entityAliases", String[].class);
        Queryable[] entityPersisters = get(createQueryTranslator, "queryLoader.entityPersisters", Queryable[].class);
        String[] sqlAliases = get(createQueryTranslator, "queryLoader.sqlAliases", String[].class);

        @SuppressWarnings({ "unused", "unchecked" })
        Map<String, String> sqlAliasByEntityAlias = get(createQueryTranslator, "queryLoader.sqlAliasByEntityAlias", Map.class);

        Map<String, String> from_aliases = new HashMap<String, String>();

        for (int i = 0; i < entityAliases.length; i++) {
            String alias = entityAliases[i];

            if (alias != null) {
                from_aliases.put(alias, entityPersisters[i].getClassMetadata().getEntityName());
            }
        }

        @SuppressWarnings("unchecked")
        List<Object> list = createQuery.list();

        return new ExecutionResult(from_aliases, list);
    }

    protected <T> T get(Object o, String path, Class<T> t) {
        return t.cast(new ObjectWrapper(o).get(path));
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#getHibernateWebResolver()
     */
    @Override
    public HibernateWebResolver getHibernateWebResolver() {
        HibernateWebResolver resolver = new HibernateWebResolver();
        for (Object o : sessionFactory.getAllClassMetadata().entrySet()) {
            Entry<?, ?> entry = (Entry<?, ?>) o;
            String className = (String) entry.getKey();
            try {
                ClassNode node = resolver.getOrCreateNode(className);
                AbstractEntityPersister abstractEntityPersister = (AbstractEntityPersister) entry.getValue();
                String superclass = abstractEntityPersister.getEntityMetamodel().getSuperclass();
                if (superclass != null) {
                    resolver.getOrCreateNode(superclass).addPath("SUB(" + className + ")", node);
                }
                for (String propertyNames : abstractEntityPersister.getPropertyNames()) {
                    Type propertyType = abstractEntityPersister.getPropertyType(propertyNames);
                    if (propertyType instanceof OneToOneType || propertyType instanceof ManyToOneType) {
                        String subClassName = propertyType.getName();
                        node.addPath(propertyNames, resolver.getOrCreateNode(subClassName));
                    } else if (propertyType instanceof CollectionType) {
                        CollectionType collectionType = CollectionType.class.cast(propertyType);
                        String subClassName = collectionType.getElementType((SessionFactoryImplementor) sessionFactory).getName();
                        if ("org.hibernate.type.EnumType".equals(subClassName)) {
                            continue;
                        }
                        node.addPath(propertyNames, resolver.getOrCreateNode(subClassName)).setCollection(true);
                    }
                }
            } catch (IllegalArgumentException ex) {
                System.err.println(className);
            }
        }
        return resolver;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#getConnectionInfo()
     */
    @Override
    public String getConnectionInfo() {
        return username + "@" + url;
    }

    private String url;

    private String username;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
