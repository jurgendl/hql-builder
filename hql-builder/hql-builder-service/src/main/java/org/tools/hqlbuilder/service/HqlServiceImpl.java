package org.tools.hqlbuilder.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.sql.DataSource;

import org.apache.lucene.queryParser.ParseException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.OneToOneType;
import org.hibernate.type.Type;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.HibernateWebResolver.ClassNode;
import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.common.MethodInvoker;
import org.tools.hqlbuilder.common.ObjectWrapper;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.exceptions.ServiceException;
import org.tools.hqlbuilder.common.exceptions.SqlException;
import org.tools.hqlbuilder.common.exceptions.SyntaxException;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.common.interfaces.ValidationExceptionConverter;

public class HqlServiceImpl implements HqlService {
    private static final long serialVersionUID = 3856142589306194609L;

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlServiceImpl.class);

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

    protected SessionFactory sessionFactory;

    protected String modelVersion;

    protected String url;

    protected String username;

    protected Information information;

    protected Set<String> keywords;

    protected ConfigurationBean configurationBean;

    protected LinkedHashMap<String, String> hibernateVersions;

    protected String project;

    protected Properties hibernateProperties;

    protected DataSource dataSource;

    public HqlServiceImpl() {
        super();
    }

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

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getHibernateInfo()
     */
    @Override
    public Map<String, String> getHibernateInfo() {
        if (hibernateVersions == null) {
            hibernateVersions = new LinkedHashMap<String, String>();
            {
                String[][] deps = {
                        { "Hibernate", "org.hibernate.Hibernate" },
                        { "Annotations", "org.hibernate.AnnotationException" },
                        { "Validator", "org.hibernate.validator.InvalidStateException" } };
                for (String[] dep : deps) {
                    try {
                        hibernateVersions.put(dep[0], readManifestVersion(dep[1]).toString());
                    } catch (Exception ex) {
                        //
                    }
                }
            }
            {
                String[][] deps = {
                        { "Hibernate", "org.hibernate:hibernate" },
                        { "Hibernate", "org.hibernate:hibernate-core" },
                        { "Annotations", "org.hibernate:hibernate-annotations" },
                        { "Annotations", "org.hibernate:hibernate-commons-annotations" },
                        { "Annotations", "org.hibernate.commons:hibernate-commons-annotations" },
                        { "Validator", "org.hibernate:hibernate-validator" },
                        { "JPA", "org.hibernate.javax.persistence:hibernate-jpa-2.0-api" } };
                for (String[] dep : deps) {
                    try {
                        hibernateVersions.put(dep[0], readMavenVersion(dep[1]).toString());
                    } catch (Exception ex) {
                        //
                    }
                }
            }
        }
        return hibernateVersions;
    }

    private String readMavenVersion(String pack) throws Exception {
        String[] dp = pack.split(":");
        Properties p = new Properties();
        p.load(getClass().getClassLoader().getResourceAsStream("META-INF/maven/" + dp[0] + "/" + dp[1] + "/pom.properties"));
        String value = p.getProperty("version").toString();
        return value.toString();
    }

    private String readManifestVersion(String cn) throws Exception {
        Class<?> clazz = Class.forName(cn);
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            // Class not from JAR
            return null;
        }
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
        Manifest manifest = new Manifest(new URL(manifestPath).openStream());
        Attributes attr = manifest.getMainAttributes();
        String value = attr.getValue("Version");
        if (value == null) {
            value = attr.getValue("Implementation-Version");
        }
        return value.toString();
    }

    public String getModelVersion() {
        if (modelVersion == null) {
            try {
                Class<?> modelClass = Class.forName(getClasses().iterator().next());
                URL classLocation = modelClass.getProtectionDomain().getCodeSource().getLocation();
                if (new File(classLocation.getFile()).isFile()) {
                    ZipFile zf = new ZipFile(new File(classLocation.getFile()));
                    Enumeration<? extends ZipEntry> enu = zf.entries();
                    ZipEntry manifest = null;
                    ZipEntry pomprops = null;
                    while (enu.hasMoreElements()) {
                        ZipEntry ze = enu.nextElement();
                        try {
                            if (ze.getName().startsWith("META-INF") && ze.getName().endsWith("MANIFEST.MF")) {
                                manifest = ze;
                            }
                            if (ze.getName().startsWith("META-INF") && ze.getName().endsWith("pom.properties")) {
                                pomprops = ze;
                            }
                        } catch (Exception ex) {
                            //
                        }
                    }
                    if (pomprops != null && modelVersion == null) {
                        try {
                            Properties p = new Properties();
                            p.load(modelClass.getResourceAsStream(pomprops.getName()));
                            modelVersion = p.get("version").toString();
                        } catch (Exception ex) {
                            //
                        }
                    }
                    if (manifest != null && modelVersion == null) {
                        try {
                            Properties p = new Properties();
                            p.load(modelClass.getResourceAsStream(manifest.getName()));
                            modelVersion = p.get("Implementation-Version:").toString();
                        } catch (Exception ex) {
                            //
                        }
                    }
                }
                modelVersion.toString();
            } catch (Exception ex) {
                modelVersion = "?";
            }
        }
        return this.modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
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
     * @see org.tools.hqlbuilder.common.HqlService#getSqlForHql(java.lang.String)
     */
    @Override
    public String getSqlForHql(String hql) {
        QueryTranslator createQueryTranslator = new QueryTranslator(QUERY_IDENTIFIER, hql, new HashMap<Object, Object>(), sessionFactory);
        createQueryTranslator.compile(new HashMap<Object, Object>(), false);
        return createQueryTranslator.getSQLString();
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#execute(java.lang.String, int, java.util.List)
     */
    @Override
    public ExecutionResult execute(String hql, int max, List<QueryParameter> queryParameters) {
        return execute(hql, max, queryParameters.toArray(new QueryParameter[queryParameters.size()]));
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#execute(java.lang.String, int, org.tools.hqlbuilder.common.QueryParameter[])
     */
    @Override
    public ExecutionResult execute(String hql, int max, QueryParameter... queryParameters) {
        logger.debug("start query");
        ExecutionResult result = new ExecutionResult();
        try {
            return innerExecute(result, hql, max, queryParameters);
        } catch (QueryException ex) {
            logger.error("execute(String, int, QueryParameter)", ex);
            String msg = ex.getLocalizedMessage();
            Matcher m = Pattern.compile("could not resolve property: ([^ ]+) of: ([^ ]+) ").matcher(msg);
            if (m.find()) {
                throw new SyntaxException(SyntaxException.SyntaxExceptionType.could_not_resolve_property, msg, m.group(2) + "#" + m.group(1));
            }
            m = Pattern.compile("Unable to resolve path \\[([^]]+)\\]").matcher(msg);
            if (m.find()) {
                throw new SyntaxException(SyntaxException.SyntaxExceptionType.unable_to_resolve_path, msg, m.group(1));
            }
            throw new ServiceException(ex.getMessage(), result);
        } catch (SQLGrammarException ex) {
            logger.error("execute(String, int, QueryParameter)", ex);
            throw new SqlException(ex.getMessage(), result, ex.getSQL(), String.valueOf(ex.getSQLException()), ex.getSQLState());
        } catch (HibernateException ex) {
            logger.error("execute(String, int, QueryParameter)", ex);
            throw new ServiceException(ex.getMessage(), result);
        } catch (Exception ex) {
            if (ex.getClass().getSimpleName().equals("QuerySyntaxException")) {
                logger.error("execute(String, int, QueryParameter)", ex);
                String msg = ex.getLocalizedMessage();
                Matcher m = Pattern.compile("unexpected token: ([^ ]+) near line (\\d+), column (\\d+) ").matcher(msg);
                if (m.find()) {
                    throw new SyntaxException(SyntaxException.SyntaxExceptionType.unexpected_token, msg, m.group(1), Integer.parseInt(m.group(2)),
                            Integer.parseInt(m.group(3)));
                }
                m = Pattern.compile("Invalid path: '([^']+)' ").matcher(msg);
                if (m.find()) {
                    throw new SyntaxException(SyntaxException.SyntaxExceptionType.invalid_path, msg, m.group(1));
                }
                m = Pattern.compile("([^']+) is not mapped ").matcher(msg);
                if (m.find()) {
                    throw new SyntaxException(SyntaxException.SyntaxExceptionType.not_mapped, msg, m.group(1));
                }
                throw new ServiceException(ex.getMessage(), result);
            }
            throw new RuntimeException(ex);
        } finally {
            logger.debug("end query");
        }
    }

    @SuppressWarnings("unchecked")
    protected ExecutionResult innerExecute(ExecutionResult result, String hql, int max, QueryParameter... queryParameters) {
        long start = System.currentTimeMillis();
        QueryTranslator queryTranslator = new QueryTranslator(QUERY_IDENTIFIER, hql, new HashMap<Object, Object>(), sessionFactory);
        String sql = queryTranslator.getSQLString();
        logger.debug(sql);
        result.setSql(sql);
        boolean isUpdateStatement = hql.trim().toLowerCase().startsWith("update");
        Session session = sessionFactory.openSession();
        org.hibernate.Query createQuery = session.createQuery(hql);
        int index = 0;
        for (QueryParameter value : queryParameters) {
            try {
                if (value.getName() != null) {
                    if (value.getValue() instanceof Collection) {
                        @SuppressWarnings({ "rawtypes" })
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
                logger.debug("innerExecute(String, int, QueryParameter) - " + ex); // => whatever
            } catch (java.lang.IndexOutOfBoundsException ex) {
                // java.lang.IndexOutOfBoundsException: Remember that ordinal parameters are 1-based!
                logger.debug("innerExecute(String, int, QueryParameter) - " + ex); // => whatever
            }
        }
        if (isUpdateStatement) {
            result.setSize(createQuery.executeUpdate());
            return result;
        }
        queryTranslator.compile(new HashMap<Object, Object>(), false);
        if (sql == null) {
            sql = queryTranslator.getSQLString();
            if (sql == null) {
                String tmp = new ObjectWrapper(queryTranslator).get(QUERY_LOADER).toString();
                sql = tmp.substring(tmp.indexOf("(") + 1, tmp.length() - 1);
            }
            logger.debug(sql);
            result.setSql(sql);
        }
        createQuery.setMaxResults(max);
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
        Map<String, String> from_aliases = new HashMap<String, String>();
        result.setFromAliases(from_aliases);
        for (int i = 0; i < entityAliases.length; i++) {
            String alias = entityAliases[i];
            if (alias != null) {
                from_aliases.put(alias, entityPersisters[i].getClassMetadata().getEntityName());
            }
        }
        long startTime = System.currentTimeMillis();
        List<Object> list = createQuery.list();
        long endTime = System.currentTimeMillis();
        result.setResults(list);
        result.setSize(list.size());
        String[] queryReturnTypeNames = new String[queryReturnTypes.length];
        for (int i = 0; i < queryReturnTypes.length; i++) {
            queryReturnTypeNames[i] = queryReturnTypes[i].getReturnedClass().getName();
        }
        result.setQueryReturnTypeNames(queryReturnTypeNames);
        long duration = System.currentTimeMillis() - start;
        result.setDuration(endTime - startTime);
        result.setOverhead(duration - result.getDuration());
        return result;
    }

    /**
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
                        Type elementType = MethodInvoker.call(collectionType, "getElementType", Type.class, sessionFactory);
                        String subClassName = elementType.getName();
                        if ("org.hibernate.type.EnumType".equals(subClassName)) {
                            continue;
                        }
                        try {
                            node.addPath(propertyNames, resolver.getOrCreateNode(subClassName)).setCollection(true);
                        } catch (IllegalArgumentException ex) {
                            logger.warn("not a mapped class, ignoring: " + className + "#" + propertyNames + " [collection of " + subClassName + "]");
                        }
                    }
                }
            } catch (IllegalArgumentException ex) {
                logger.error("getHibernateWebResolver() - " + className, ex);
            }
        }
        return resolver;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getConnectionInfo()
     */
    @Override
    public String getConnectionInfo() {
        return username + "@" + url + "@" + getModelVersion();
    }

    /**
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

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getProperties(java.lang.String)
     */
    @Override
    public List<String> getProperties(String classname) {
        Map<String, ?> allClassMetadata = sessionFactory.getAllClassMetadata();
        Object classMeta = allClassMetadata.get(classname);
        if (classMeta == null) {
            return null;
        }
        List<String> propertyNames = new ArrayList<String>(Arrays.asList(AbstractEntityPersister.class.cast(classMeta).getPropertyNames()));
        propertyNames.remove("id");
        propertyNames.remove("version");
        return propertyNames;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#save(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T save(T object) throws ValidationException {
        try {
            org.hibernate.Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            object = (T) session.merge(object);
            session.persist(object);
            tx.commit();
            session.flush();
            return object;
        } catch (RuntimeException ex) {
            if ("org.hibernate.validator.InvalidStateException".equals(ex.getClass().getName())) {
                try {
                    ValidationExceptionConverter vc = (ValidationExceptionConverter) Class.forName(
                            "org.tools.hqlbuilder.common.validation.HibernateValidationConverter").newInstance();
                    throw vc.convert(ex);
                } catch (Exception ex2) {
                    throw ex;
                }
            } else if ("javax.validation.ConstraintViolationException".equals(ex.getClass().getName())) {
                try {
                    ValidationExceptionConverter vc = (ValidationExceptionConverter) Class.forName(
                            "org.tools.hqlbuilder.common.validation.JavaxValidationConverter").newInstance();
                    throw vc.convert(ex);
                } catch (Exception ex2) {
                    throw ex;
                }
            } else {
                throw ex;
            }
        }
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#delete(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> void delete(T object) {
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        object = (T) session.merge(object);
        session.delete(object);
        tx.commit();
        session.flush();
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getReservedKeywords()
     */
    @Override
    public Set<String> getReservedKeywords() {
        if (keywords == null) {
            try {
                keywords = new HashSet<String>();
                // ansi & transact sql keywords
                BufferedReader in = new BufferedReader(new InputStreamReader(HqlServiceImpl.class.getClassLoader().getResourceAsStream(
                        "org/tools/hqlbuilder/service/reserved_keywords.txt")));
                String line;
                while ((line = in.readLine()) != null) {
                    for (String kw : line.split(" ")) {
                        if (kw.length() < 2) {
                            continue;
                        }
                        keywords.add(kw.trim());
                    }
                }
            } catch (IOException ex) {
                logger.error("getReservedKeywords()", ex);
            }
        }
        return keywords;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getNamedQueries()
     */
    @Override
    public Map<String, String> getNamedQueries() {
        Map<String, String> namedQueriesRv = new HashMap<String, String>();
        @SuppressWarnings("unchecked")
        Map<String, Object/* NamedQueryDefinition */> namedQueries = get(getSessionFactory(), "namedQueries", Map.class);
        for (Map.Entry<String, Object/* NamedQueryDefinition */> entry : namedQueries.entrySet()) {
            namedQueriesRv.put(entry.getKey(), get(entry.getValue(), "queryString", String.class));
        }
        return namedQueriesRv;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#findParameters(java.lang.String)
     */
    @Override
    public List<QueryParameter> findParameters(String hql) {
        List<QueryParameter> parameters = new ArrayList<QueryParameter>();
        Session session = sessionFactory.openSession();
        try {
            Query createQuery = session.createQuery(hql);
            Object pminof = get(createQuery, "parameterMetadata", Object.class/* ParameterMetadata */);

            for (String param : createQuery.getNamedParameters()) {
                String simpleName = "?";
                try {
                    // simpleName = pminof.getNamedParameterExpectedType(param).getReturnedClass().getSimpleName();
                    simpleName = MethodInvoker.call(pminof, "getNamedParameterExpectedType", Type.class, param).getReturnedClass().getSimpleName();
                } catch (Exception ex) {
                    //
                }
                QueryParameter p = new QueryParameter(null, param, simpleName);
                parameters.add(p);
            }
            Integer mi = MethodInvoker.call(pminof, "getOrdinalParameterCount", Integer.class);
            for (int i = 1; i <= mi; i++) {
                String simpleName = "?";
                try {
                    simpleName = MethodInvoker.call(pminof, "getOrdinalParameterExpectedType", Type.class, i).getReturnedClass().getSimpleName();
                    // simpleName = pminof.getOrdinalParameterExpectedType(i).getReturnedClass().getSimpleName();
                } catch (Exception ex) {
                    //
                }
                QueryParameter p = new QueryParameter(null, null, i + ":" + simpleName);
                parameters.add(p);
            }
        } finally {
            session.close();
        }
        return parameters;
    }

    /**
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

    protected Information getInformation() {
        if (information == null) {
            try {
                information = new InformationImpl(getSessionFactory());
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

    protected <T> T get(Object o, String path, Class<T> t) {
        return t.cast(new ObjectWrapper(o).get(path));
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#execute(java.lang.String, java.util.List)
     */
    @Override
    public ExecutionResult execute(String hql, List<QueryParameter> queryParameters) {
        return execute(hql, Integer.MAX_VALUE, queryParameters);
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#execute(java.lang.String, org.tools.hqlbuilder.common.QueryParameter[])
     */
    @Override
    public ExecutionResult execute(String hql, QueryParameter... queryParameters) {
        return execute(hql, Integer.MAX_VALUE, queryParameters);
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getProject()
     */
    @Override
    public String getProject() {
        if (project == null) {
            try {
                Class<?> modelClass = Class.forName(getClasses().iterator().next());
                URL classLocation = modelClass.getProtectionDomain().getCodeSource().getLocation();
                if (new File(classLocation.getFile()).isFile()) {
                    ZipFile zf = new ZipFile(new File(classLocation.getFile()));
                    Enumeration<? extends ZipEntry> enu = zf.entries();
                    ZipEntry manifest = null;
                    ZipEntry pomprops = null;
                    while (enu.hasMoreElements()) {
                        ZipEntry ze = enu.nextElement();
                        try {
                            if (ze.getName().startsWith("META-INF") && ze.getName().endsWith("MANIFEST.MF")) {
                                manifest = ze;
                            }
                            if (ze.getName().startsWith("META-INF") && ze.getName().endsWith("pom.properties")) {
                                pomprops = ze;
                            }
                        } catch (Exception ex) {
                            //
                        }
                    }
                    if (pomprops != null && project == null) {
                        try {
                            Properties p = new Properties();
                            p.load(modelClass.getResourceAsStream(pomprops.getName()));
                            project = p.get("artifactId").toString();
                        } catch (Exception ex) {
                            //
                        }
                    }
                    if (manifest != null && project == null) {
                        try {
                            Properties p = new Properties();
                            p.load(modelClass.getResourceAsStream(manifest.getName()));
                            project = p.get("Implementation-Title:").toString();
                        } catch (Exception ex) {
                            //
                        }
                    }
                }
                project.toString();
            } catch (Exception ex) {
                project = "unknown";
            }
        }
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#log()
     */
    @Override
    public void log() {
        for (Object key : new TreeSet<Object>(hibernateProperties.keySet())) {
            logger.debug(key + "=" + hibernateProperties.get(key));
        }
    }

    public Properties getHibernateProperties() {
        return this.hibernateProperties;
    }

    public void setHibernateProperties(Properties hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#createScript()
     */
    @Override
    public String createScript() {
        if (configurationBean == null || configurationBean.getConfiguration() == null) {
            return null;
        }
        try {
            SchemaExport export = new SchemaExport(configurationBean.getConfiguration());
            export.setDelimiter(";");
            export.setFormat(true);
            export.setHaltOnError(false);
            try {
                File tmp = File.createTempFile("create", "sql");
                export.setOutputFile(tmp.getAbsolutePath());
                export.execute(true, false, false, true);
                return new String(CommonUtils.read(new FileInputStream(tmp)));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (RuntimeException ex) {
            logger.error("org.tools.hqlbuilder.service.HqlServiceImpl.createScript()");
            logger.error(ex.getClass().getName());
            logger.error(String.valueOf(ex));
            return null;
        }
    }

    public ConfigurationBean getConfigurationBean() {
        return this.configurationBean;
    }

    public void setConfigurationBean(ConfigurationBean configurationBean) {
        this.configurationBean = configurationBean;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#sql(java.lang.String[])
     */
    @Override
    public void sql(final String... sql) {
        for (String s : sql) {
            try {
                logger.debug(s);
                dataSource.getConnection().prepareStatement(s).execute();
            } catch (SQLException ex) {
                throw new ServiceException(ex.getMessage());
            }
        }
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
