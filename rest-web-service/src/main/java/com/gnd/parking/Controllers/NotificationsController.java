package com.gnd.parking.Controllers;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Auth.Models.Token;
import com.gnd.parking.Models.Role;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("notifications")
public class NotificationsController {
    @Context
    SecurityContext securityContext;

    @GET
    @Secured({Role.EMPLOYEE})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        Token user = ((Token) securityContext.getUserPrincipal());

        int userId = user.getId();
        int regionId = user.getRegionId();

        System.out.println(String.format("user id: %s; region id: %s", userId, regionId));

        return Response.ok().build();
    }
}
