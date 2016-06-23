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
     * @see [get] /builder/
     */
    @GET
    @Path("/ping/")
    @Produces({ TEXT })
    public String ping();

    /**
     * @see [get] /builder/sqlforhql?hql=...
     */
    @GET
    @Path("/sqlforhql")
    @Produces({ TEXT })
    String getSqlForHql(@QueryParam("hql") String hql);

    /**
     * @see [get] /builder/classes
     */
    @GET
    @Path("/classes")
    @Produces({ XML })
    XmlWrapper<SortedSet<String>> getClasses();

    /**
     * @see [get] /builder/properties?classname=...
     */
    @GET
    @Path("/properties")
    @Produces({ XML })
    XmlWrapper<List<String>> getProperties(@QueryParam("classname") String classname);

    /**
     * @see [get] /builder/connectioninfo
     */
    @GET
    @Path("/connectioninfo")
    @Produces({ TEXT })
    String getConnectionInfo();

    /**
     * @see [get] /builder/project
     */
    @GET
    @Path("/project")
    @Produces({ TEXT })
    String getProject();

    /**
     * @see [get] /builder/search?text=...&typeName =...&hitsPerPage=...
     */
    @GET
    @Path("/search")
    @Produces({ XML })
    XmlWrapper<List<String>> search(@QueryParam("text") String text, @QueryParam("typename") String typeName,
            @QueryParam("hitsperpage") int hitsPerPage);

    /**
     * @see [get] /builder/reservedkeywords
     */
    @GET
    @Path("/reservedkeywords")
    @Produces({ XML })
    XmlWrapper<Set<String>> getReservedKeywords();

    /**
     * @see [get] /builder/namedqueries
     */
    @GET
    @Path("/namedqueries")
    @Produces({ XML })
    XmlWrapper<Map<String, String>> getNamedQueries();

    /**
     * @see [get] /builder/createscript
     */
    @GET
    @Path("/createscript")
    @Produces({ TEXT })
    String createScript();

    /**
     * @see [get] /builder/hibernateinfo
     */
    @GET
    @Path("/hibernateinfo")
    @Produces({ XML })
    XmlWrapper<Map<String, String>> getHibernateInfo();

    /**
     * @see [get] /builder/hibernatehelpurl
     */
    @GET
    @Path("/hibernatehelpurl")
    @Produces({ TEXT })
    String getHibernateHelpURL();

    /**
     * @see [get] /builder/hqlhelpurl
     */
    @GET
    @Path("/hqlhelpurl")
    @Produces({ TEXT })
    String getHqlHelpURL();

    /**
     * @see [get] /builder/lucenehelpurl
     */
    @GET
    @Path("/lucenehelpurl")
    @Produces({ TEXT })
    String getLuceneHelpURL();

    /**
     * @see [get] /builder/propertynames?key=...& parts=...&parts=...
     */
    @GET
    @Path("/propertynames")
    @Produces({ TEXT })
    XmlWrapper<List<String>> getPropertyNames(@QueryParam("key") String key, @QueryParam("parts") String[] parts);

    /**
     * @see [get] /builder/sql?sql=...&sql=...
     */
    @GET
    @Path("/sql")
    @Produces({ TEXT })
    void sql(@QueryParam("sql") String[] sql);

    /**
     * @see [get] /builder/findparameters?hql=...
     */
    @GET
    @Path("/findparameters")
    @Produces({ XML })
    @Wrapped
    XmlWrapper<List<QueryParameter>> findParameters(@QueryParam("hql") String hql);

    /**
     * @see [put] /builder/save/{...} [body]
     */
    @PUT
    @Path("/save/{pojo}")
    @Consumes({ XML })
    @Produces({ XML })
    <T extends Serializable, I extends Serializable> XmlWrapper<I> save(@PathParam("pojo") String pojo, XmlWrapper<T> object);

    /**
     * @see [delete] /builder/delete/{...} [body]
     */
    @DELETE
    @Path("/delete/{pojo}")
    @Consumes({ XML })
    <T extends Serializable> void delete(@PathParam("pojo") String pojo, XmlWrapper<T> object);

    /**
     * @see [get] /builder/hibernatewebresolver
     */
    @GET
    @Path("/hibernatewebresolver")
    @Produces({ BINARY })
    StreamingOutput getHibernateWebResolver();

    /**
     * @see [put] /builder/get [type,id]
     */
	@Path("/get")
    @POST
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<T> get(@FormParam("type") String type, @FormParam("id") String id);

    /**
     * @see [get] /builder/executehql?hql=...
     */
    @GET
    @Path("/executehql")
    @Produces({ XML })
    ExecutionResult execute(@QueryParam("hql") String hql);

    /**
     * @see [put] /builder/execute [body]
     */
    @PUT
    @Path("/execute")
    @Consumes({ XML })
    @Produces({ XML })
    ExecutionResult execute(QueryParameters queryParameters);

    /**
     * @see [get] /builder/executehqlplain?hql=...
     */
    @GET
    @Path("/executehqlplain")
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<List<T>> executePlainResult(@QueryParam("hql") String hql);

    /**
     * @see [put] /builder/executeplain [body]
     */
    @PUT
    @Path("/executeplain")
    @Consumes({ XML })
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<List<T>> executePlainResult(QueryParameters queryParameters);
}
