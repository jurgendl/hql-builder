package org.tools.hqlbuilder.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.lucene.queryParser.ParseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.NamedQueryDefinition;
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
        super();
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
    public SortedSet<String> getClasses() {
        SortedSet<String> options = new TreeSet<String>();

        for (HibernateWebResolver.ClassNode node : getHibernateWebResolver().getClasses()) {
            String clazz = node.getId();
            options.add(clazz);
        }

        return options;
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
     * @see org.tools.hqlbuilder.common.HqlService#execute(java.lang.String, int, java.util.List)
     */
    @Override
    public ExecutionResult execute(String hql, int max, List<QueryParameter> queryParameters) {
        return execute(hql, max, queryParameters.toArray(new QueryParameter[queryParameters.size()]));
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#execute(java.lang.String, int, org.tools.hqlbuilder.common.QueryParameter[])
     */
    @Override
    public ExecutionResult execute(String hql, int max, QueryParameter... queryParameters) {
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

        String[] queryReturnTypeNames = new String[queryReturnTypes.length];
        for (int i = 0; i < queryReturnTypes.length; i++) {
            queryReturnTypeNames[i] = queryReturnTypes[i].getReturnedClass().getSimpleName();
        }

        return new ExecutionResult(createQueryTranslator.getSQLString(), from_aliases, list, queryReturnAliases, scalarColumnNames, sqlAliases,
                queryReturnTypeNames);
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

    private Information information;

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#search(java.lang.String, java.lang.String)
     */
    @Override
    public List<String> search(String text, String typeName) {
        try {
            return getInformation().search(text, typeName);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected Information getInformation() {
        if (information == null) {
            try {
                information = new Information(getSessionFactory());
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.information;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#getProperties(java.lang.String)
     */
    @Override
    public List<String> getProperties(String classname) {
        @SuppressWarnings("unchecked")
        Map<String, AbstractEntityPersister> allClassMetadata = sessionFactory.getAllClassMetadata();
        AbstractEntityPersister classMeta = allClassMetadata.get(classname);
        if (classMeta == null) {
            return null;
        }
        List<String> propertyNames = new ArrayList<String>(Arrays.asList(classMeta.getPropertyNames()));
        propertyNames.remove("id");
        propertyNames.remove("version");
        return propertyNames;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#save(java.lang.Object)
     */
    @Override
    public void save(Object object) {
        org.hibernate.classic.Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(object);
        tx.commit();
        session.flush();
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#delete(java.lang.Object)
     */
    @Override
    public void delete(Object object) {
        org.hibernate.classic.Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(object);
        tx.commit();
        session.flush();
    }

    private Set<String> kws;

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#getReservedKeywords()
     */
    @Override
    public Set<String> getReservedKeywords() {
        if (kws == null) {
            try {
                kws = new HashSet<String>();
                // ansi & transact sql keywords
                BufferedReader in = new BufferedReader(new InputStreamReader(HqlServiceImpl.class.getClassLoader().getResourceAsStream(
                        "reserved_keywords.txt")));
                String line;
                while ((line = in.readLine()) != null) {
                    for (String kw : line.split(" ")) {
                        if (kw.length() < 2) {
                            continue;
                        }
                        kws.add(kw.trim());
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return kws;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#getNamedQueries()
     */
    @Override
    public Map<String, String> getNamedQueries() {
        Map<String, String> namedQueriesRv = new HashMap<String, String>();
        @SuppressWarnings("unchecked")
        Map<String, NamedQueryDefinition> namedQueries = get(getSessionFactory(), "namedQueries", Map.class);
        for (Map.Entry<String, NamedQueryDefinition> entry : namedQueries.entrySet()) {
            namedQueriesRv.put(entry.getKey(), entry.getValue().getQueryString());
        }
        return namedQueriesRv;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#findParameters(java.lang.String)
     */
    @Override
    public List<QueryParameter> findParameters(String hql) {
        List<QueryParameter> parameters = new ArrayList<QueryParameter>();
        Session session = sessionFactory.openSession();
        try {
            QueryImpl createQuery = (QueryImpl) session.createQuery(hql);
            org.hibernate.engine.query.ParameterMetadata pminof = get(createQuery, "parameterMetadata",
                    org.hibernate.engine.query.ParameterMetadata.class);

            for (String param : createQuery.getNamedParameters()) {
                String simpleName = "?";
                try {
                    simpleName = pminof.getNamedParameterExpectedType(param).getReturnedClass().getSimpleName();
                } catch (Exception ex) {
                    //
                }
                QueryParameter p = new QueryParameter(null, param, simpleName, "groovy");
                parameters.add(p);
            }
            for (int i = 1; i <= pminof.getOrdinalParameterCount(); i++) {
                String simpleName = "?";
                try {
                    simpleName = pminof.getOrdinalParameterExpectedType(i).getReturnedClass().getSimpleName();
                } catch (Exception ex) {
                    //
                }
                QueryParameter p = new QueryParameter(null, null, i + ":" + simpleName, "groovy");
                parameters.add(p);
            }
        } finally {
            session.close();
        }
        return parameters;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.common.HqlService#getPropertyNames(java.lang.Object, java.lang.String[])
     */
    @Override
    public List<String> getPropertyNames(Object key, String[] parts) {
        Map<?, ?> allClassMetadata = sessionFactory.getAllClassMetadata();
        AbstractEntityPersister persister = (AbstractEntityPersister) allClassMetadata.get(key);

        for (int i = 1; i < parts.length; i++) {
            int index = -1;
            String[] propertyNames = persister.getClassMetadata().getPropertyNames();

            for (int j = 0; (j < propertyNames.length) && (index == -1); j++) {
                if (propertyNames[j].equals(parts[i])) {
                    index = j;
                }
            }

            Type type = persister.getClassMetadata().getPropertyTypes()[index];

            if (type instanceof ManyToOneType) {
                String nextClass = ManyToOneType.class.cast(type).getAssociatedEntityName();
                persister = (AbstractEntityPersister) allClassMetadata.get(nextClass);
            } else if (type instanceof OneToOneType) {
                String nextClass = OneToOneType.class.cast(type).getAssociatedEntityName();
                persister = (AbstractEntityPersister) allClassMetadata.get(nextClass);
            } else {
                throw new RuntimeException(type.toString());
            }
        }

        List<String> propertyNames = Arrays.asList(persister.getPropertyNames());
        return propertyNames;
    }
}
