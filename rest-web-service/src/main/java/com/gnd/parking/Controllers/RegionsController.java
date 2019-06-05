package com.gnd.parking.Controllers;

import com.gnd.parking.Contracts.RegionsRepositoryInterface;
import com.gnd.parking.Models.Region;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("regions")
public class RegionsController {

    @EJB(lookup = "java:global/parking-implementation-1.0/RegionsRepository")
    RegionsRepositoryInterface regionsRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index() {
        List<Region> regions = regionsRepository.all();
        return Response.ok(regions).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") int id) {
        Region region = regionsRepository.find(id);

        if (region == null) {
            return Response.status(404).build();
        }

        return Response.ok(region).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Region sourceRegion) {
        Region createdRegion = regionsRepository.create(sourceRegion);
        return Response.ok(createdRegion).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        regionsRepository.delete(id);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Region sourceRegion) {
        Region targetRegion = regionsRepository.find(id);
        if (targetRegion == null) {
            return Response.status(404).build();
        }
        sourceRegion.setId(id);
        Region updatedRegion = regionsRepository.update(sourceRegion);
        return Response.ok(updatedRegion).build();
    }
}
