package com.gnd.parking.Controllers;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Contracts.Repositories.TicketsRepositoryInterface;
import com.gnd.parking.Contracts.Services.Tickets.Exceptions.TicketPurchaseException;
import com.gnd.parking.Contracts.Services.Tickets.PurchaseTicketServiceInterface;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.Role;
import com.gnd.parking.Models.Ticket;
import com.gnd.parking.Requests.PurchaseTicketRequest;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("tickets")
public class TicketsController {
    @EJB(lookup = "java:global/parking-implementation-1.0/TicketsRepository")
    TicketsRepositoryInterface ticketsRepository;

    @EJB(lookup = "java:global/parking-logic-1.0/PurchaseTicketService")
    PurchaseTicketServiceInterface purchaseTicketService;

    @GET
    @Secured({Role.PARKING_METER,Role.EMPLOYEE,Role.ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    public Response index(@QueryParam("valid_to_after") Long validToAfter) {
        List<Ticket> tickets = (validToAfter == null)
            ? ticketsRepository.all()
            : ticketsRepository.allWhere("valid_to", ">", new Date(validToAfter));

        return Response.ok(tickets).build();
    }

    @GET
    @Path("/{id}")
    @Secured({Role.PARKING_METER})
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") int id) {
        Ticket ticket = ticketsRepository.find(id);

        if (ticket == null) {
            return Response.status(404).build();
        }

        return Response.ok(ticket).build();
    }

    @POST
    @Path("/buy")
    @Secured({Role.PARKING_METER})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buy(PurchaseTicketRequest ticketRequest) {
        try {
            purchaseTicketService.purchaseTicket(ticketRequest.getValidTo(), ticketRequest.getParkingSpotId());
            return Response.ok().build();
        } catch (TicketPurchaseException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @POST
    @Secured({Role.PARKING_METER})
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
    @Secured({Role.PARKING_METER})
    public Response delete(@PathParam("id") int id) {
        ticketsRepository.delete(id);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    @Secured({Role.PARKING_METER})
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
