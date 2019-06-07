package com.gnd.parking.Controllers;

import com.gnd.parking.Contracts.Repositories.TicketsRepositoryInterface;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.Ticket;

@Path("tickets")
public class TicketsController {
    @EJB(lookup = "java:global/parking-implementation-1.0/TicketsRepository")
    TicketsRepositoryInterface ticketsRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index() {
        List<Ticket> tickets = ticketsRepository.all();
        return Response.ok(tickets).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") int id) {
        Ticket ticket = ticketsRepository.find(id);

        if (ticket == null) {
            return Response.status(404).build();
        }

        return Response.ok(ticket).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Ticket sourceTicket) {
        try {
            Ticket createdTicket = ticketsRepository.create(sourceTicket);
            return Response.ok(createdTicket).build();
        } catch (NestedObjectNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        ticketsRepository.delete(id);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Ticket sourceTicket) {
        Ticket targetTicket = ticketsRepository.find(id);
        if (targetTicket == null) {
            return Response.status(404).build();
        }
        sourceTicket.setId(id);
        try {
            Ticket updatedTicket = ticketsRepository.update(sourceTicket);
            return Response.ok(updatedTicket).build();
        } catch (NestedObjectNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }
}
