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
import org.jhaws.common.web.xml.XmlWrapper;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;

@Path("/builder")
@Pretty
@GZIP
public interface PojoResource extends RestResource {
    /**
     * @see [get] /ping.txt
     */
    @GET
    @Path("/ping.txt")
    @Produces({ TEXT })
    public String ping();

    /**
     * @see [get] /builder/sqlforhql.txt?hql=...
     */
    @GET
    @Path("/sqlforhql.txt")
    @Produces({ TEXT })
    String getSqlForHql(@QueryParam("hql") String hql);

    /**
     * @see [get] /builder/classes.xml
     */
    @GET
    @Path("/classes.xml")
    @Produces({ XML })
    XmlWrapper<SortedSet<String>> getClasses();

    /**
     * @see [get] /builder/properties.xml?classname=...
     */
    @GET
    @Path("/properties.xml")
    @Produces({ XML })
    XmlWrapper<List<String>> getProperties(@QueryParam("classname") String classname);

    /**
     * @see [get] /builder/connectioninfo.txt
     */
    @GET
    @Path("/connectioninfo.txt")
    @Produces({ TEXT })
    String getConnectionInfo();

    /**
     * @see [get] /builder/project.txt
     */
    @GET
    @Path("/project.txt")
    @Produces({ TEXT })
    String getProject();

    /**
     * @see [get] /builder/search.xml?text=...&typeName =...&hitsPerPage=...
     */
    @GET
    @Path("/search.xml")
    @Produces({ XML })
    XmlWrapper<List<String>> search(@QueryParam("text") String text, @QueryParam("typename") String typeName,
            @QueryParam("hitsperpage") int hitsPerPage);

    /**
     * @see [get] /builder/reservedkeywords.xml
     */
    @GET
    @Path("/reservedkeywords.xml")
    @Produces({ XML })
    XmlWrapper<Set<String>> getReservedKeywords();

    /**
     * @see [get] /builder/namedqueries.xml
     */
    @GET
    @Path("/namedqueries.xml")
    @Produces({ XML })
    XmlWrapper<Map<String, String>> getNamedQueries();

    /**
     * @see [get] /builder/createscript.txt
     */
    @GET
    @Path("/createscript.txt")
    @Produces({ TEXT })
    String createScript();

    /**
     * @see [get] /builder/hibernateinfo.xml
     */
    @GET
    @Path("/hibernateinfo.xml")
    @Produces({ XML })
    XmlWrapper<Map<String, String>> getHibernateInfo();

    /**
     * @see [get] /builder/hibernatehelp.url
     */
    @GET
    @Path("/hibernatehelp.url")
    @Produces({ TEXT })
    String getHibernateHelpURL();

    /**
     * @see [get] /builder/hqlhelp.url
     */
    @GET
    @Path("/hqlhelp.url")
    @Produces({ TEXT })
    String getHqlHelpURL();

    /**
     * @see [get] /builder/lucenehelp.url
     */
    @GET
    @Path("/lucenehelp.url")
    @Produces({ TEXT })
    String getLuceneHelpURL();

    /**
     * @see [get] /builder/propertynames.txt?key=...& parts=...&parts=...
     */
    @GET
    @Path("/propertynames.txt")
    @Produces({ TEXT })
    XmlWrapper<List<String>> getPropertyNames(@QueryParam("key") String key, @QueryParam("parts") String[] parts);

    /**
     * @see [get] /builder/sql.txt?sql=...&sql=...
     */
    @GET
    @Path("/sql.txt")
    @Produces({ TEXT })
    void sql(@QueryParam("sql") String[] sql);

    /**
     * @see [get] /builder/findparameters.xml?hql=...
     */
    @GET
    @Path("/findparameters.xml")
    @Produces({ XML })
    @Wrapped
    XmlWrapper<List<QueryParameter>> findParameters(@QueryParam("hql") String hql);

    /**
     * @see [put] /builder/save/{...}.xml [body]
     */
    @PUT
    @Path("/save/{pojo}.xml")
    @Consumes({ XML })
    @Produces({ XML })
    <T extends Serializable, I extends Serializable> XmlWrapper<I> save(@PathParam("pojo") String pojo, XmlWrapper<T> object);

    /**
     * @see [delete] /builder/delete/{...}.xml [body]
     */
    @DELETE
    @Path("/delete/{pojo}.xml")
    @Consumes({ XML })
    <T extends Serializable> void delete(@PathParam("pojo") String pojo, XmlWrapper<T> object);

    /**
     * @see [get] /builder/hibernatewebresolver.bin
     */
    @GET
    @Path("/hibernatewebresolver/bin/hibernatewebresolver.bin")
    @Produces({ BINARY })
    StreamingOutput getSerializedHibernateWebResolver();

    /**
     * @see [get] /builder/hibernatewebresolver.xml
     */
    @GET
    @Path("/hibernatewebresolver/xml/hibernatewebresolver.xml")
    @Produces({ XML })
    HibernateWebResolver getHibernateWebResolver();

    /**
     * @see [put] /builder/get.xml [type,id]
     */
    @Path("/get.xml")
    @POST
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<T> get(@FormParam("type") String type, @FormParam("id") String id);

    /**
     * @see [get] /builder/executehql.xml?hql=...
     */
    @GET
    @Path("/executehql.xml")
    @Produces({ XML })
    ExecutionResult execute(@QueryParam("hql") String hql);

    /**
     * @see [put] /builder/execute.xml [body]
     */
    @PUT
    @Path("/execute.xml")
    @Consumes({ XML })
    @Produces({ XML })
    ExecutionResult execute(QueryParameters queryParameters);

    /**
     * @see [get] /builder/executehqlplain.xml?hql=...
     */
    @GET
    @Path("/executehqlplain.xml")
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<List<T>> executePlainResult(@QueryParam("hql") String hql);

    /**
     * @see [put] /builder/executeplain.xml [body]
     */
    @PUT
    @Path("/executeplain.xml")
    @Consumes({ XML })
    @Produces({ XML })
    <T extends Serializable> XmlWrapper<List<T>> executePlainResult(QueryParameters queryParameters);
}
