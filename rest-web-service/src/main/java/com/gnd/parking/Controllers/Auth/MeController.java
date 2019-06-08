package com.gnd.parking.Controllers.Auth;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Auth.Models.Token;
import com.gnd.parking.Responses.MeResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/me")
@Secured
public class MeController {
    @Context
    SecurityContext securityContext;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response me() {
        return Response.ok(new MeResponse((Token) securityContext.getUserPrincipal())).build();
    }
}
