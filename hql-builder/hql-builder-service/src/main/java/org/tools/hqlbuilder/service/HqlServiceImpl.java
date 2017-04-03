package org.tools.hqlbuilder.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.type.CollectionType;
import org.hibernate.type.IdentifierType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.OneToOneType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.jhaws.common.lang.ObjectWrapper;
import org.jhaws.common.lang.Value;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.GroovyCompiler;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.HibernateWebResolver.ClassNode;
import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.exceptions.ServiceException;
import org.tools.hqlbuilder.common.exceptions.SqlException;
import org.tools.hqlbuilder.common.exceptions.SyntaxException;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.common.interfaces.Information;
import org.tools.hqlbuilder.common.interfaces.ValidationExceptionConverter;

public class HqlServiceImpl implements HqlService {
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

    protected Properties properties;

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
            hibernateVersions = new LinkedHashMap<>();
            {
                String[][] deps = {
                        { "Hibernate", "org.hibernate.Hibernate" },
                        { "Annotations", "org.hibernate.AnnotationException" },
                        { "Validator", "org.hibernate.validator.InvalidStateException" } };
                for (String[] dep : deps) {
                    try {
                        hibernateVersions.put(dep[0], CommonUtils.readManifestVersion(dep[1]).toString());
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
                        { "JPA", "javax.persistence:persistence-api" },
                        { "JPA", "org.hibernate.javax.persistence:hibernate-jpa-2.0-api" },
                        { "JPA", "org.hibernate.javax.persistence:hibernate-jpa-2.1-api" } };
                for (String[] dep : deps) {
                    try {
                        hibernateVersions.put(dep[0], CommonUtils.readMavenVersion(dep[1]).toString());
                    } catch (Exception ex) {
                        //
                    }
                }
            }
        }
        return hibernateVersions;
    }

    @Override
    public String getVersion() {
        String version = "";
        try {
            Properties p = new Properties();
            p.load(getClass().getClassLoader().getResourceAsStream("META-INF/maven/org.tools.hql-builder/hql-builder-service/pom.properties"));
            if (p.containsKey("version")) {
                version = p.getProperty("version");
            }
        } catch (Exception ex) {
            try {
                version = org.w3c.dom.Node.class
                        .cast(CommonUtils.getFromXml(new FileInputStream("pom.xml"), "project", "/default:project/default:version/text()"))
                        .getNodeValue();
            } catch (Exception ex2) {
                try {
                    version = org.w3c.dom.Node.class.cast(CommonUtils.getFromXml(new FileInputStream("pom.xml"), "project",
                            "/default:project/default:parent/default:version/text()")).getNodeValue();
                } catch (Exception ex3) {
                    //
                }
            }
        }
        return version;
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
                    zf.close();
                }
                if (modelVersion == null) {
                    modelVersion = "";
                }
            } catch (Exception ex) {
                modelVersion = "";
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
        return getHibernateWebResolver().getClasses()
                .stream()
                .map(HibernateWebResolver.ClassNode::getId)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getSqlForHql(java.lang.String)
     */
    @Override
    public String getSqlForHql(String hql) {
        if (StringUtils.isBlank(hql)) {
            throw new IllegalArgumentException("hql");
        }
        QueryTranslator createQueryTranslator = new QueryTranslator(QUERY_IDENTIFIER, hql, new HashMap<>(), sessionFactory);
        createQueryTranslator.compile(new HashMap<>(), false);
        return createQueryTranslator.getSQLString();
    }

    @Override
    public ExecutionResult execute(QueryParameters obj) {
        logger.debug("start query");
        String hql = obj.getHql();
        int max = obj.getMax();
        int first = obj.getFirst();
        List<QueryParameter> queryParameters = obj.getParameters();
        if (StringUtils.isBlank(hql)) {
            throw new IllegalArgumentException("hql");
        }
        Value<ExecutionResult> result = new Value<>();
        try {
            if (hql.contains("$$")) {
                result.reset();
                @SuppressWarnings("unchecked")
                Class<?> c = queryParameters.stream()
                        .filter(qp -> "$$".equals(qp.getName()))
                        .map(QueryParameter::getValueClass)
                        .findAny()
                        .orElse(Class.class.cast(Object.class));
                getHibernateWebResolver().getClasses()
                        .stream()
                        .filter(cn -> c.isAssignableFrom(cn.getType()))
                        .map(cn -> execute(new QueryParameters(hql.replace("$$", cn.getType().getSimpleName()), first, max, queryParameters)))
                        .forEach(it -> result.setOr(it, r -> r.getResults().getValue().addAll(it.getResults().getValue())));
                return result.get();
            }
            innerExecute(result.set(new ExecutionResult()), hql, max, first, queryParameters);
            return result.get();
        } catch (QueryException ex) {
            logger.error("{}", ex);
            String msg = ex.getLocalizedMessage();

            if (ex.getClass().getSimpleName().equals("QuerySyntaxException")) {
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
                throw new ServiceException(ex.getMessage(), result.get());
            }

            {
                Matcher m = Pattern.compile("could not resolve property: ([^ ]+) of: ([^ ]+) ").matcher(msg);
                if (m.find()) {
                    throw new SyntaxException(SyntaxException.SyntaxExceptionType.could_not_resolve_property, msg, m.group(2) + "#" + m.group(1));
                }
            }

            {
                Matcher m = Pattern.compile("Unable to resolve path \\[([^]]+)\\]").matcher(msg);
                if (m.find()) {
                    throw new SyntaxException(SyntaxException.SyntaxExceptionType.unable_to_resolve_path, msg, m.group(1));
                }
            }

            {
                // illegal attempt to dereference collection [{synthetic-alias}{non-qualified-property-ref}afstudeerRichtingI18N] with element
                // property
                // reference [naam]
                Matcher m = Pattern
                        .compile("illegal attempt to dereference collection \\[([^\\]]+)\\] with element property reference \\[([^\\]]+)\\]")
                        .matcher(msg);
                if (m.find()) {
                    throw new SyntaxException(SyntaxException.SyntaxExceptionType.illegal_attempt_to_dereference_collection, msg,
                            m.group(1) + "#" + m.group(2));
                }
            }

            throw new ServiceException(concat(ex), result.get());
        } catch (SQLGrammarException ex) {
            logger.error("{}", ex);
            throw new SqlException(concat(ex), result.get(), ex.getSQL(), String.valueOf(ex.getSQLException()), ex.getSQLState());
        } catch (HibernateException ex) {
            logger.error("{}", ex);
            throw new ServiceException(concat(ex), result.get());
        } catch (NullPointerException ex) {
            logger.error("{}", ex);
            throw new ServiceException("NullPointerException", result.get());
        } catch (Exception ex) {
            throw new ServiceException(concat(ex), result.get());
        } finally {
            logger.debug("end query");
        }
    }

    protected String concat(Exception ex) {
        StringBuilder sb = new StringBuilder();
        Throwable t = ex;
        do {
            sb.append("\n").append(t.getClass().getName()).append(" ").append(t.getMessage());
            if (t.getCause() != null && t.getCause().equals(t)) {
                t = null;
            } else {
                t = t.getCause();
            }
        } while (t != null);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    protected ExecutionResult innerExecute(Value<ExecutionResult> resultValue, String hql, int max, int first, List<QueryParameter> queryParameters) {
        ExecutionResult result = resultValue.get();
        long start = System.currentTimeMillis();
        QueryTranslator queryTranslator = new QueryTranslator(QUERY_IDENTIFIER, hql, new HashMap<>(), sessionFactory);
        String sql = queryTranslator.getSQLString();
        logger.debug("sql={}", sql);
        result.setSql(sql);
        boolean isUpdateStatement = hql.trim().toLowerCase().startsWith("update");
        Session session = newSession();
        Query createQuery = session.createQuery(hql);
        int index = 0;
        if (queryParameters != null) {
            for (QueryParameter value : queryParameters) {
                try {
                    Object valueCompiled = value.getValue();
                    if (valueCompiled == null && StringUtils.isNotBlank(value.getValueText())) {
                        valueCompiled = GroovyCompiler.eval(value.getValueText());
                    }
                    if (value.getName() != null) {
                        if (valueCompiled instanceof Collection) {
                            @SuppressWarnings({ "rawtypes" })
                            Object[] l = new ArrayList((Collection) valueCompiled).toArray();
                            createQuery.setParameterList(value.getName(), l);
                        } else {
                            createQuery.setParameter(value.getName(), valueCompiled);
                        }
                    } else {
                        createQuery.setParameter(index++, valueCompiled);
                    }
                } catch (org.hibernate.QueryParameterException ex) {
                    // org.hibernate.QueryParameterException: could not locate named parameter [nummer]
                    logger.debug("{}", String.valueOf(ex)); // => whatever
                } catch (java.lang.IndexOutOfBoundsException ex) {
                    // java.lang.IndexOutOfBoundsException: Remember that ordinal parameters are 1-based!
                    logger.debug("{}", String.valueOf(ex)); // => whatever
                }
            }
        }
        if (isUpdateStatement) {
            result.setSize(createQuery.executeUpdate());
            return result;
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
        if (max != -1) {
            createQuery.setMaxResults(max);
        }
        if (first != -1) {
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
                        Type elementType = CommonUtils.call(collectionType, "getElementType", Type.class, sessionFactory);
                        String subClassName = elementType.getName();
                        if ("org.hibernate.type.EnumType".equals(subClassName)) {
                            continue;
                        }
                        try {
                            node.addPath(propertyNames, resolver.getOrCreateNode(subClassName)).setCollection(true);
                        } catch (IllegalArgumentException ex) {
                            logger.warn("not a mapped class, ignoring: {}#{} [collection of {}]", className, propertyNames, subClassName);
                        }
                    }
                }
            } catch (IllegalArgumentException ex) {
                logger.error("{} {}", className, ex);
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
     * @see org.tools.hqlbuilder.common.HqlService#search(java.lang.String, java.lang.String, int)
     */
    @Override
    public List<String> search(String text, String typeName, int hitsPerPage) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("text");
        }
        return getInformation().search(text, typeName, hitsPerPage);
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getProperties(java.lang.String)
     */
    @Override
    public List<String> getProperties(String classname) {
        if (StringUtils.isBlank(classname)) {
            throw new IllegalArgumentException("classname");
        }
        Map<String, ?> allClassMetadata = sessionFactory.getAllClassMetadata();
        Object classMeta = allClassMetadata.get(classname);
        if (classMeta == null) {
            return null;
        }
        List<String> propertyNames = new ArrayList<>(Arrays.asList(AbstractEntityPersister.class.cast(classMeta).getPropertyNames()));
        propertyNames.remove("id");
        propertyNames.remove("version");
        return propertyNames;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#save(java.io.Serializable)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable, I extends Serializable> I save(T object) throws ValidationException {
        try {
            Session session = newSession();
            Transaction tx = session.beginTransaction();
            object = (T) session.merge(object);
            session.persist(object);
            tx.commit();
            session.flush();

            ClassMetadata classMetadata = sessionFactory.getAllClassMetadata().get(object.getClass().getName());
            String oid = classMetadata.getIdentifierPropertyName();
            return (I) new ObjectWrapper(object).get(oid);
        } catch (RuntimeException ex) {
            if ("org.hibernate.validator.InvalidStateException".equals(ex.getClass().getName())) {
                try {
                    ValidationExceptionConverter vc = (ValidationExceptionConverter) Class
                            .forName("org.tools.hqlbuilder.common.validation.HibernateValidationConverter").newInstance();
                    throw vc.convert(ex);
                } catch (ValidationException ex2) {
                    throw ex2;
                } catch (Exception ex2) {
                    throw ex;
                }
            } else if ("javax.validation.ConstraintViolationException".equals(ex.getClass().getName())) {
                try {
                    ValidationExceptionConverter vc = (ValidationExceptionConverter) Class
                            .forName("org.tools.hqlbuilder.common.validation.JavaxValidationConverter").newInstance();
                    throw vc.convert(ex);
                } catch (ValidationException ex2) {
                    throw ex2;
                } catch (Exception ex2) {
                    throw ex;
                }
            } else {
                throw ex;
            }
        }
    }

    protected Session newSession() {
        return HibernateUtils.newSession(sessionFactory);
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#delete(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> void delete(T object) {
        Session session = newSession();
        Transaction tx = session.beginTransaction();
        object = (T) session.merge(object);
        session.delete(object);
        tx.commit();
        session.flush();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable, I extends Serializable> T get(Class<T> type, I id) {
        Object idv = id;
        String name = type.getName();
        ClassMetadata classMetadata = sessionFactory.getAllClassMetadata().get(name);
        String oid = classMetadata.getIdentifierPropertyName();
        if (id instanceof String) {
            IdentifierType<?> identifierType = (IdentifierType<?>) classMetadata.getIdentifierType();
            if (!(identifierType instanceof StringType)) {
                try {
                    idv = identifierType.stringToObject((String) id);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        QueryParameters hql = new QueryParameters("from " + name + " where " + oid + "=:" + oid,
                new QueryParameter().setName(oid).setValueTypeText(idv));
        logger.debug("hql={}", hql);
        List<Serializable> value = execute(hql).getResults().getValue();
        return (T) (value.isEmpty() ? null : value.get(0));
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getReservedKeywords()
     */
    @Override
    public Set<String> getReservedKeywords() {
        if (keywords == null) {
            try {
                keywords = new HashSet<>();
                // ansi & transact sql keywords
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        HqlServiceImpl.class.getClassLoader().getResourceAsStream("org/tools/hqlbuilder/service/reserved_keywords.txt")));
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
                logger.error("{}", ex);
            }
        }
        return keywords;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getNamedQueries()
     */
    @Override
    public Map<String, String> getNamedQueries() {
        Map<String, String> namedQueriesRv = new HashMap<>();
        SessionFactory sf = getSessionFactory();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object/* NamedQueryDefinition */> namedQueries = get(sf, "namedQueries", Map.class);
            for (Map.Entry<String, Object/* NamedQueryDefinition */> entry : namedQueries.entrySet()) {
                try {
                    namedQueriesRv.put(entry.getKey(), get(entry.getValue(), "queryString", String.class));
                } catch (Exception x) {
                    logger.error("{}", x);
                }
            }
        } catch (Exception ex1) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object/* ? */> namedQueries = get(sf, "namedQueryRepository.namedQueryDefinitionMap", Map.class);
                for (Map.Entry<String, Object/* ? */> entry : namedQueries.entrySet()) {
                    try {
                        namedQueriesRv.put(entry.getKey(), get(entry.getValue(), "queryString", String.class));
                    } catch (Exception x) {
                        logger.error("{}", x);
                    }
                }
            } catch (Exception ex2) {
                logger.error("{}", ex1);
                logger.error("{}", ex2);
            }
        }

        return namedQueriesRv;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#findParameters(java.lang.String)
     */
    @Override
    public List<QueryParameter> findParameters(String hql) {
        if (StringUtils.isBlank(hql)) {
            throw new IllegalArgumentException("hql");
        }
        List<QueryParameter> parameters = new ArrayList<>();
        Session session = newSession();
        try {
            Query createQuery = session.createQuery(hql);
            Object pminof = get(createQuery, "parameterMetadata", Object.class/* ParameterMetadata */);

            for (String param : createQuery.getNamedParameters()) {
                String simpleName = "?";
                try {
                    // simpleName = pminof.getNamedParameterExpectedType(param).getReturnedClass().getSimpleName();
                    simpleName = CommonUtils.call(pminof, "getNamedParameterExpectedType", Type.class, param).getReturnedClass().getSimpleName();
                } catch (Exception ex) {
                    //
                }
                parameters.add(new QueryParameter().setName(param).setType(simpleName));
            }

            Integer mi = CommonUtils.call(pminof, "getOrdinalParameterCount", Integer.class);
            for (int i = 1; i <= mi; i++) {
                String simpleName = "?";
                try {
                    // simpleName = pminof.getOrdinalParameterExpectedType(i).getReturnedClass().getSimpleName();
                    simpleName = CommonUtils.call(pminof, "getOrdinalParameterExpectedType", Type.class, i).getReturnedClass().getSimpleName();
                } catch (Exception ex) {
                    //
                }
                parameters.add(new QueryParameter().setIndex(i).setType(simpleName));
            }
        } finally {
            session.close();
        }
        return parameters;
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getPropertyNames(java.lang.String, java.lang.String[])
     */
    @Override
    public List<String> getPropertyNames(String classtype, String[] parts) {
        if (StringUtils.isBlank(classtype)) {
            throw new IllegalArgumentException("type");
        }
        Map<?, ?> allClassMetadata = sessionFactory.getAllClassMetadata();
        AbstractEntityPersister persister = (AbstractEntityPersister) allClassMetadata.get(classtype);

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
                List<Information> infos = new ArrayList<>();
                ServiceLoader<Information> sl = ServiceLoader.load(Information.class);
                Iterator<Information> sli = sl.iterator();
                while (sli.hasNext()) {
                    Information next = sli.next();
                    infos.add(next);
                }
                Collections.sort(infos, (o1, o2) -> o1.getOrder() - o2.getOrder());
                information = infos.get(0);
                information.init(getConnectionInfo(), getSessionFactory());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return this.information;
    }

    protected <T> T get(Object o, String path, Class<T> t) {
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("path");
        }
        return t.cast(new ObjectWrapper(o).get(path));
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
                    zf.close();
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
        for (Object key : new TreeSet<>(hibernateProperties.keySet())) {
            logger.debug("{}={}", key, hibernateProperties.get(key));
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
            logger.error("configuration not set");
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
            logger.error("{}", ex);
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
    public void sql(final String[] sql) {
        for (String s : sql) {
            try {
                logger.debug("sql={}", s);
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

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getHibernateHelpURL()
     */
    @Override
    public String getHibernateHelpURL() {
        return properties.getProperty("hibernate.url");
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getHqlHelpURL()
     */
    @Override
    public String getHqlHelpURL() {
        return properties.getProperty("hql.url");
    }

    /**
     * @see org.tools.hqlbuilder.common.HqlService#getLuceneHelpURL()
     */
    @Override
    public String getLuceneHelpURL() {
        return properties.getProperty("lucene.url");
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
