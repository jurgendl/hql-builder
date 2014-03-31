package org.tools.hqlbuilder.resteasy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/pojo")
public interface PojoResource {
    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/pojo/ping
     */
    @GET
    @Path("/ping")
    @Produces({ MediaType.TEXT_PLAIN })
    public String ping();
}
