package org.tools.hqlbuilder.serviceresteasy;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

@Component
public class PojoResourceImpl implements PojoResource {
    @Override
    public Response hello() {
        return Response.status(200).entity("hello from rest easy").build();
    }
}
