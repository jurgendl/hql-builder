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
import org.jhaws.common.web.resteasy.RestResource;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.XmlWrapper;

@Path("/builder")
@Pretty
@GZIP
public interface PojoResource extends RestResource {
    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/
     */
    @GET
    @Path("/ping/")
    @Produces({ TEXT })
    public String ping();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/sqlforhql?hql=...
     */
    @GET
    @Path("/sqlforhql")
    @Produces({ TEXT })
    String getSqlForHql(@QueryParam("hql") String hql);

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/classes
     */
    @GET
    @Path("/classes")
    @Produces({ XML })
    XmlWrapper<SortedSet<String>> getClasses();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/properties?classname=...
     */
    @GET
    @Path("/properties")
    @Produces({ XML })
    XmlWrapper<List<String>> getProperties(@QueryParam("classname") String classname);

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/connectioninfo
     */
    @GET
    @Path("/connectioninfo")
    @Produces({ TEXT })
    String getConnectionInfo();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/project
     */
    @GET
    @Path("/project")
    @Produces({ TEXT })
    String getProject();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/search?text=...&typeName =...&hitsPerPage=...
     */
    @GET
    @Path("/search")
    @Produces({ XML })
    XmlWrapper<List<String>> search(@QueryParam("text") String text, @QueryParam("typename") String typeName,
            @QueryParam("hitsperpage") int hitsPerPage);

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/reservedkeywords
     */
    @GET
    @Path("/reservedkeywords")
    @Produces({ XML })
    XmlWrapper<Set<String>> getReservedKeywords();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/namedqueries
     */
    @GET
    @Path("/namedqueries")
    @Produces({ XML })
    XmlWrapper<Map<String, String>> getNamedQueries();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/createscript
     */
    @GET
    @Path("/createscript")
    @Produces({ TEXT })
    String createScript();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/hibernateinfo
     */
    @GET
    @Path("/hibernateinfo")
    @Produces({ XML })
    XmlWrapper<Map<String, String>> getHibernateInfo();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/hibernatehelpurl
     */
    @GET
    @Path("/hibernatehelpurl")
    @Produces({ TEXT })
    String getHibernateHelpURL();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/hqlhelpurl
     */
    @GET
    @Path("/hqlhelpurl")
    @Produces({ TEXT })
    String getHqlHelpURL();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/lucenehelpurl
     */
    @GET
    @Path("/lucenehelpurl")
    @Produces({ TEXT })
    String getLuceneHelpURL();

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/propertynames?key=...& parts=...&parts=...
     */
    @GET
    @Path("/propertynames")
    @Produces({ TEXT })
    XmlWrapper<List<String>> getPropertyNames(@QueryParam("key") String key, @QueryParam("parts") String[] parts);

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/sql?sql=...&sql=...
     */
    @GET
    @Path("/sql")
    @Produces({ TEXT })
    void sql(@QueryParam("sql") String[] sql);

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/findparameters?hql=...
     */
    @GET
    @Path("/findparameters")
    @Produces({ XML })
    @Wrapped
    XmlWrapper<List<QueryParameter>> findParameters(@QueryParam("hql") String hql);

    /**
     * @see [put] http://localhost:80/hqlbuilder/xml/builder/save/{...} [body]
     */
    @PUT
    @Path("/save/{pojo}")
    @Consumes({ XML })
    @Produces({ XML })
    <T extends Serializable, I extends Serializable> XmlWrapper<I> save(@PathParam("pojo") String pojo, XmlWrapper<T> object);

    /**
     * @see [delete] http://localhost:80/hqlbuilder/xml/builder/delete/{...} [body]
     */
    @DELETE
    @Path("/delete/{pojo}")
    @Consumes({ XML })
    <T extends Serializable> void delete(@PathParam("pojo") String pojo, XmlWrapper<T> object);

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/hibernatewebresolver
     */
    @GET
    @Path("/hibernatewebresolver")
    @Produces({ BINARY })
    StreamingOutput getHibernateWebResolver();

    /**
     * @see [put] http://localhost:80/hqlbuilder/xml/builder/get [type,id]
     */
	@Path("/get")
    @POST
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<T> get(@FormParam("type") String type, @FormParam("id") String id);

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/executehql?hql=...
     */
    @GET
    @Path("/executehql")
    @Produces({ XML })
    ExecutionResult execute(@QueryParam("hql") String hql);

    /**
     * @see [put] http://localhost:80/hqlbuilder/xml/builder/execute [body]
     */
    @PUT
    @Path("/execute")
    @Consumes({ XML })
    @Produces({ XML })
    ExecutionResult execute(QueryParameters queryParameters);

    /**
     * @see [get] http://localhost:80/hqlbuilder/xml/builder/executehqlplain?hql=...
     */
    @GET
    @Path("/executehqlplain")
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<List<T>> executePlainResult(@QueryParam("hql") String hql);

    /**
     * @see [put] http://localhost:80/hqlbuilder/xml/builder/executeplain [body]
     */
    @PUT
    @Path("/executeplain")
    @Consumes({ XML })
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<List<T>> executePlainResult(QueryParameters queryParameters);
}
