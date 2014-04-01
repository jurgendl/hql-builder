package org.tools.hqlbuilder.resteasy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.GZIP;
import org.tools.hqlbuilder.jaxb.XmlWrapper;

@Path("/pojo")
@Pretty
@GZIP
public interface PojoResource {
    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/ping
     */
    @GET
    @Path("/ping")
    @Produces({ MediaType.TEXT_PLAIN })
    public String ping();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/sqlforhql?hql=...
     */
    @GET
    @Path("/sqlforhql")
    @Produces({ MediaType.TEXT_PLAIN })
    public abstract String getSqlForHql(@QueryParam("hql") String hql);

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/classes
     */
    @GET
    @Path("/classes")
    @Produces({ MediaType.TEXT_XML })
    public abstract XmlWrapper<SortedSet<String>> getClasses();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/properties?classname=...
     */
    @GET
    @Path("/properties")
    @Produces({ MediaType.TEXT_XML })
    public abstract XmlWrapper<List<String>> getProperties(@QueryParam("classname") String classname);

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/connectionInfo
     */
    @GET
    @Path("/connectionInfo")
    @Produces({ MediaType.TEXT_PLAIN })
    public abstract String getConnectionInfo();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/project
     */
    @GET
    @Path("/project")
    @Produces({ MediaType.TEXT_PLAIN })
    public abstract String getProject();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/search?text=...&typeName=...&hitsPerPage=...
     */
    @GET
    @Path("/search")
    @Produces({ MediaType.TEXT_XML })
    public abstract XmlWrapper<List<String>> search(@QueryParam("text") String text, @QueryParam("typeName") String typeName,
            @QueryParam("hitsPerPage") int hitsPerPage);

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/reservedkeywords
     */
    @GET
    @Path("/reservedkeywords")
    @Produces({ MediaType.TEXT_XML })
    public abstract XmlWrapper<Set<String>> getReservedKeywords();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/namedqueries
     */
    @GET
    @Path("/namedqueries")
    @Produces({ MediaType.TEXT_XML })
    public abstract XmlWrapper<Map<String, String>> getNamedQueries();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/createscript
     */
    @GET
    @Path("/createscript")
    @Produces({ MediaType.TEXT_PLAIN })
    public abstract String createScript();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/hibernateinfo
     */
    @GET
    @Path("/hibernateinfo")
    @Produces({ MediaType.TEXT_XML })
    public abstract XmlWrapper<Map<String, String>> getHibernateInfo();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/hibernatehelpurl
     */
    @GET
    @Path("/hibernatehelpurl")
    @Produces({ MediaType.TEXT_PLAIN })
    public abstract String getHibernateHelpURL();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/hqlhelpurl
     */
    @GET
    @Path("/hqlhelpurl")
    @Produces({ MediaType.TEXT_PLAIN })
    public abstract String getHqlHelpURL();

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/lucenehelpurl
     */
    @GET
    @Path("/lucenehelpurl")
    @Produces({ MediaType.TEXT_PLAIN })
    public abstract String getLuceneHelpURL();

    // public abstract ExecutionResult execute(String string, QueryParameter... queryParameters);

    // public abstract ExecutionResult execute(String string, List<QueryParameter> queryParameters);

    // public abstract ExecutionResult execute(String string, int max, QueryParameter... queryParameters);

    // public abstract ExecutionResult execute(String string, int max, List<QueryParameter> queryParameters);

    // public abstract HibernateWebResolver getHibernateWebResolver();

    // public abstract <T> T save(T object) throws ValidationException;

    // public abstract <T> void delete(T object);

    // public abstract List<QueryParameter> findParameters(String hql);

    // public abstract List<String> getPropertyNames(Object key, String[] parts);

    // public abstract void sql(String... sql);
}
