package com.gnd.parking.Controllers;

import com.gnd.parking.Contracts.UsersRepositoryInterface;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.User;

@Path("users")
public class UsersController {
    @EJB(lookup = "java:global/parking-implementation-1.0/UsersRepository")
    UsersRepositoryInterface usersRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index() {
        List<User> users = usersRepository.all();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") int id) {
        User user = usersRepository.find(id);

        if (user == null) {
            return Response.status(404).build();
        }

        return Response.ok(user).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(User sourceUser) {
        try {
            User createdUser = usersRepository.create(sourceUser);
            return Response.ok(createdUser).build();
        } catch (NestedObjectNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        usersRepository.delete(id);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, User sourceUser) {
        User targetUser = usersRepository.find(id);
        if (targetUser == null) {
            return Response.status(404).build();
        }
        sourceUser.setId(id);
        try {
            User updatedUser = usersRepository.update(sourceUser);
            return Response.ok(updatedUser).build();
        } catch (NestedObjectNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }
}
