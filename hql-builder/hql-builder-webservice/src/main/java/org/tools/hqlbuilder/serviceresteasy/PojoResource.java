package org.tools.hqlbuilder.serviceresteasy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/pojo")
public interface PojoResource {
    @GET
    @Path("/hello")
    public Response hello();
}
