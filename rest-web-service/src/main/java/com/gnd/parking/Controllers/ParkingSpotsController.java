package com.gnd.parking.Controllers;

import com.gnd.parking.Contracts.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.ParkingSpot;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("parking_spots")
public class ParkingSpotsController {
    @EJB(lookup = "java:global/parking-implementation-1.0/ParkingSpotsRepository")
    ParkingSpotsRepositoryInterface parkingSpotsRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index() {
        List<ParkingSpot> parkingSpots = parkingSpotsRepository.all();
        return Response.ok(parkingSpots).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") int id) {
        ParkingSpot parkingSpot = parkingSpotsRepository.find(id);

        if (parkingSpot == null) {
            return Response.status(404).build();
        }

        return Response.ok(parkingSpot).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ParkingSpot sourceParkingSpot) {
        try {
            ParkingSpot createdParkingSpot = parkingSpotsRepository.create(sourceParkingSpot);
            return Response.ok(createdParkingSpot).build();
        } catch (NestedObjectNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        parkingSpotsRepository.delete(id);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, ParkingSpot sourceParkingSpot) {
        ParkingSpot targetParkingSpot = parkingSpotsRepository.find(id);
        if (targetParkingSpot == null) {
            return Response.status(404).build();
        }
        sourceParkingSpot.setId(id);
        try {
            ParkingSpot updatedParkingSpot = parkingSpotsRepository.update(sourceParkingSpot);
            return Response.ok(updatedParkingSpot).build();
        } catch (NestedObjectNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }
}
