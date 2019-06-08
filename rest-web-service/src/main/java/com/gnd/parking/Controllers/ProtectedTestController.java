package com.gnd.parking.Controllers;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Models.Role;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/protected")
public class ProtectedTestController {
    @Context
    SecurityContext securityContext;

    @GET
    @Path("/test1")
    @Secured
    @Produces(MediaType.TEXT_PLAIN)
    public Response test() {
        return Response.ok(
            "username is " + securityContext.getUserPrincipal().getName()
                + " and role is " + securityContext.isUserInRole(Role.ADMIN.toString())
        ).build();
    }

    @GET
    @Path("/test2")
    @Secured({Role.EMPLOYEE})
    @Produces(MediaType.TEXT_PLAIN)
    public Response test2() {
        return Response.ok(
            "username is " + securityContext.getUserPrincipal().getName()
                + " and role is " + securityContext.isUserInRole(Role.ADMIN.toString())
        ).build();
    }

    @GET
    @Path("/test3")
    @Secured({Role.ADMIN})
    @Produces(MediaType.TEXT_PLAIN)
    public Response test3() {
        return Response.ok(
            "username is " + securityContext.getUserPrincipal().getName()
                + " and role is " + securityContext.isUserInRole(Role.ADMIN.toString())
        ).build();
    }

    @GET
    @Path("/test4")
    @Secured({Role.ADMIN, Role.EMPLOYEE})
    @Produces(MediaType.TEXT_PLAIN)
    public Response test4() {
        return Response.ok(
            "username is " + securityContext.getUserPrincipal().getName()
                + " and role is " + securityContext.isUserInRole(Role.ADMIN.toString())
        ).build();
    }
}
