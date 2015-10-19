package org.tools.hqlbuilder.webservice.resteasy.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.GZIP;

@GZIP
@Path("/googlelogin")
public interface GoogleLoginResource {
    @GET
    @Path("/ping")
    @Produces("text/plain")
    public String ping();

    @POST
    @Path("/tokensignin")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/xml")
    public Response tokensignin(@FormParam("idtoken") String id_token);
}
