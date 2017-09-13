package org.tools.hqlbuilder.webcommon.resteasy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.StreamingOutput;

import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jhaws.common.jaxb.XmlWrapper;
import org.jhaws.common.web.resteasy.Pretty;
import org.jhaws.common.web.resteasy.RestResource;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;

@Path(PojoResource.PATH)
@Pretty
@GZIP
public interface PojoResource extends RestResource {
    public static final String PATH = "/builder";

    public static final String PATH_STOP = "/stop";

    public static final String PATH_PLAIN = "/executeplain" + D + XML_EXTENSION;

    public static final String PATH_HQL_PLAIN = "/executehqlplain" + D + XML_EXTENSION;

    public static final String PATH_QUERY = "/execute" + D + XML_EXTENSION;

    public static final String PATH_HQL = "/executehql" + D + XML_EXTENSION;

    public static final String PATH_GET = "/get" + D + XML_EXTENSION;

    public static final String PATH_RESOLVER = "/hibernatewebresolver/" + XML_EXTENSION + "/hibernatewebresolver" + D + XML_EXTENSION;

    public static final String PATH_RESOLVER_SERIALIZED = "/hibernatewebresolver/" + BINARY_EXTENSION + "/hibernatewebresolver" + D
            + BINARY_EXTENSION;

    public static final String PATH_FINDPARAMETERS = "/findparameters" + D + XML_EXTENSION;

    public static final String PATH_SQL = "/sql" + D + TEXT_EXTENSION;

    public static final String PATH_PROPERTYNAMES = "/propertynames" + D + TEXT_EXTENSION;

    public static final String PATH_LUCENE_HELP = "/lucenehelp" + D + URL_EXTENSION;

    public static final String PATH_HQL_HELP = "/hqlhelp" + D + URL_EXTENSION;

    public static final String PATH_HIBERNATE_HELP = "/hibernatehelp" + D + URL_EXTENSION;

    public static final String PATH_HIBERNATE_INFO = "/hibernateinfo" + D + XML_EXTENSION;

    public static final String PATH_DDL = "/createscript" + D + TEXT_EXTENSION;

    public static final String PATH_NAMED_QUERIES = "/namedqueries" + D + XML_EXTENSION;

    public static final String PATH_KEYWORDS = "/reservedkeywords" + D + XML_EXTENSION;

    public static final String PATH_SEARCH = "/search" + D + XML_EXTENSION;

    public static final String PATH_PROJECT_INFO = "/project" + D + TEXT_EXTENSION;

    public static final String PATH_CONNECTION_INFO = "/connectioninfo" + D + TEXT_EXTENSION;

    public static final String PATH_PROPERTIES = "/properties" + D + XML_EXTENSION;

    public static final String PATH_CLASSES = "/classes" + D + XML_EXTENSION;

    public static final String PATH_HQL_TO_SQL = "/sqlforhql" + D + TEXT_EXTENSION;

    public static final String PARAM_SQL = "sql";

    public static final String PARAM_CLASSNAME = "classname";

    public static final String PARAM_HITSPERPAGE = "hitsperpage";

    public static final String PARAM_TYPENAME = "typename";

    public static final String PARAM_TEXT = "text";

    public static final String PARAM_POJO = "pojo";

    public static final String PATH_DELETE = "/delete/{" + PARAM_POJO + "}" + D + XML_EXTENSION;

    public static final String PATH_SAVE = "/save/{" + PARAM_POJO + "}" + D + XML_EXTENSION;

    public static final String PARAM_PARTS = "parts";

    public static final String PARAM_KEY = "key";

    public static final String PARAM_HQL = "hql";

    public static final String PARAM_UUID = "uuid";

    public static final String PARAM_ID = "id";

    public static final String PARAM_TYPE = "type";

    /**
     * @see [get] /ping.txt
     */
    @GET
    @Path("/ping" + D + TEXT_EXTENSION)
    @Produces({ TEXT })
    public String ping();

    /**
     * @see [get] /builder/sqlforhql.txt?hql=...
     */
    @GET
    @Path(PATH_HQL_TO_SQL)
    @Produces({ TEXT })
    String getSqlForHql(@QueryParam(PARAM_HQL) String hql);

    /**
     * @see [get] /builder/classes.xml
     */
    @GET
    @Path(PATH_CLASSES)
    @Produces({ XML })
    XmlWrapper<SortedSet<String>> getClasses();

    /**
     * @see [get] /builder/properties.xml?classname=...
     */
    @GET
    @Path(PATH_PROPERTIES)
    @Produces({ XML })
    XmlWrapper<List<String>> getProperties(@QueryParam(PARAM_CLASSNAME) String classname);

    /**
     * @see [get] /builder/connectioninfo.txt
     */
    @GET
    @Path(PATH_CONNECTION_INFO)
    @Produces({ TEXT })
    String getConnectionInfo();

    /**
     * @see [get] /builder/project.txt
     */
    @GET
    @Path(PATH_PROJECT_INFO)
    @Produces({ TEXT })
    String getProject();

    /**
     * @see [get] /builder/search.xml?text=...&typeName =...&hitsPerPage=...
     */
    @GET
    @Path(PATH_SEARCH)
    @Produces({ XML })
    XmlWrapper<List<String>> search(@QueryParam(PARAM_TEXT) String text, @QueryParam(PARAM_TYPENAME) String typeName,
            @QueryParam(PARAM_HITSPERPAGE) int hitsPerPage);

    /**
     * @see [get] /builder/reservedkeywords.xml
     */
    @GET
    @Path(PATH_KEYWORDS)
    @Produces({ XML })
    XmlWrapper<Set<String>> getReservedKeywords();

    /**
     * @see [get] /builder/namedqueries.xml
     */
    @GET
    @Path(PATH_NAMED_QUERIES)
    @Produces({ XML })
    XmlWrapper<Map<String, String>> getNamedQueries();

    /**
     * @see [get] /builder/createscript.txt
     */
    @GET
    @Path(PATH_DDL)
    @Produces({ TEXT })
    String createScript();

    /**
     * @see [get] /builder/hibernateinfo.xml
     */
    @GET
    @Path(PATH_HIBERNATE_INFO)
    @Produces({ XML })
    XmlWrapper<Map<String, String>> getHibernateInfo();

    /**
     * @see [get] /builder/hibernatehelp.url
     */
    @GET
    @Path(PATH_HIBERNATE_HELP)
    @Produces({ TEXT })
    String getHibernateHelpURL();

    /**
     * @see [get] /builder/hqlhelp.url
     */
    @GET
    @Path(PATH_HQL_HELP)
    @Produces({ TEXT })
    String getHqlHelpURL();

    /**
     * @see [get] /builder/lucenehelp.url
     */
    @GET
    @Path(PATH_LUCENE_HELP)
    @Produces({ TEXT })
    String getLuceneHelpURL();

    /**
     * @see [get] /builder/propertynames.txt?key=...& parts=...&parts=...
     */
    @GET
    @Path(PATH_PROPERTYNAMES)
    @Produces({ TEXT })
    XmlWrapper<List<String>> getPropertyNames(@QueryParam(PARAM_KEY) String key, @QueryParam(PARAM_PARTS) String[] parts);

    /**
     * @see [get] /builder/sql.txt?sql=...&sql=...
     */
    @GET
    @Path(PATH_SQL)
    @Produces({ TEXT })
    void sql(@QueryParam(PARAM_SQL) String[] sql);

    /**
     * @see [get] /builder/findparameters.xml?hql=...
     */
    @GET
    @Path(PATH_FINDPARAMETERS)
    @Produces({ XML })
    @Wrapped
    XmlWrapper<List<QueryParameter>> findParameters(@QueryParam(PARAM_HQL) String hql);

    /**
     * @see [put] /builder/save/{...}.xml [body]
     */
    @PUT
    @Path(PATH_SAVE)
    @Consumes({ XML })
    @Produces({ XML })
    <T extends Serializable, I extends Serializable> XmlWrapper<I> save(@PathParam(PARAM_POJO) String pojo, XmlWrapper<T> object);

    /**
     * @see [delete] /builder/delete/{...}.xml [body]
     */
    @DELETE
    @Path(PATH_DELETE)
    @Consumes({ XML })
    <T extends Serializable> void delete(@PathParam(PARAM_POJO) String pojo, XmlWrapper<T> object);

    /**
     * @see [get] /builder/hibernatewebresolver.bin
     */
    @GET
    @Path(PATH_RESOLVER_SERIALIZED)
    @Produces({ BINARY })
    StreamingOutput getSerializedHibernateWebResolver();

    /**
     * @see [get] /builder/hibernatewebresolver.xml
     */
    @GET
    @Path(PATH_RESOLVER)
    @Produces({ XML })
    HibernateWebResolver getHibernateWebResolver();

    /**
     * @see [put] /builder/get.xml [type,id]
     */
    @Path(PATH_GET)
    @POST
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<T> get(@FormParam(PARAM_TYPE) String type, @FormParam(PARAM_ID) String id);

    /**
     * @see [get] /builder/executehql.xml?hql=...
     */
    @GET
    @Path(PATH_HQL)
    @Produces({ XML })
    ExecutionResult execute(@QueryParam(PARAM_HQL) String hql);

    /**
     * @see [put] /builder/execute.xml [body]
     */
    @PUT
    @Path(PATH_QUERY)
    @Consumes({ XML })
    @Produces({ XML })
    ExecutionResult execute(QueryParameters queryParameters);

    /**
     * @see [get] /builder/executehqlplain.xml?hql=...
     */
    @GET
    @Path(PATH_HQL_PLAIN)
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<List<T>> executePlainResult(@QueryParam(PARAM_HQL) String hql);

    /**
     * @see [put] /builder/executeplain.xml [body]
     */
    @PUT
    @Path(PATH_PLAIN)
    @Consumes({ XML })
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<List<T>> executePlainResult(QueryParameters queryParameters);

    @GET
    @Path(PATH_STOP)
    @Produces({ TEXT })
    String stopQuery(@QueryParam(PARAM_UUID) String uuid);
}
