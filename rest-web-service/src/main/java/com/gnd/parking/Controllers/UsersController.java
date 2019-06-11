package com.gnd.parking.Controllers;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Auth.Models.Token;
import com.gnd.parking.Contracts.Repositories.UsersRepositoryInterface;
import com.gnd.parking.Contracts.Services.Auth.AuthenticatorServiceInterface;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.Role;
import com.gnd.parking.Models.User;
import com.gnd.parking.Requests.UserPatchRequest;
import com.gnd.parking.Responses.ErrorResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("users")
public class UsersController {
    @EJB(lookup = "java:global/parking-implementation-1.0/UsersRepository")
    UsersRepositoryInterface usersRepository;

    @EJB(lookup = "java:global/parking-logic-1.0/AuthenticatorService")
    AuthenticatorServiceInterface authenticatorService;

    @Context
    SecurityContext securityContext;

    @GET
    @Secured({Role.ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    public Response index() {
        List<User> users = usersRepository.all();

        return Response.ok(users).build();
    }

    @GET
    @Secured
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") int id) {
        if (
            !securityContext.isUserInRole(Role.ADMIN.toString())
                && id != ((Token) securityContext.getUserPrincipal()).getId()
        ) {
            return Response.status(403)
                .entity(new ErrorResponse("You are not allowed to edit other users."))
                .build();
        }

        User user = usersRepository.find(id);

        if (user == null) {
            return Response.status(404).build();
        }

        return Response.ok(user).build();
    }

    @POST
    @Secured({Role.ADMIN})
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
    @Secured({Role.ADMIN})
    public Response delete(@PathParam("id") int id) {
        usersRepository.delete(id);
        return Response.ok().build();
    }

    @PATCH
    @Secured
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
        @PathParam("id") int id,
        UserPatchRequest userRequest
    ) throws NestedObjectNotFoundException {
        if (
            !securityContext.isUserInRole(Role.ADMIN.toString())
                && id != ((Token) securityContext.getUserPrincipal()).getId()
        ) {
            return Response.status(403)
                .entity(new ErrorResponse("You are not allowed to edit other users."))
                .build();
        }

        User targetUser = usersRepository.find(id);

        if (targetUser == null) {
            return Response.status(404)
                .entity(new ErrorResponse("The user with given id does not exist"))
                .build();
        }

        if (userRequest.getNewPassword().length() > 0) {
            if (userRequest.getNewPassword().length() < 8) {
                return Response.status(400)
                    .entity(new ErrorResponse("The new password cannot be shorter than 8 characters."))
                    .build();
            }

            boolean successfulPasswordChange;

            if (
                securityContext.isUserInRole(Role.ADMIN.toString())
                    && ((Token) securityContext.getUserPrincipal()).getId() != id
            ) {
                successfulPasswordChange = authenticatorService.changePassword(
                    targetUser,
                    userRequest.getNewPassword()
                );
            } else {
                successfulPasswordChange = authenticatorService.changePassword(
                    targetUser,
                    userRequest.getOldPassword(),
                    userRequest.getNewPassword()
                );
            }

            if (!successfulPasswordChange) {
                return Response.status(403)
                    .entity(new ErrorResponse("Provided password is incorrect."))
                    .build();
            }
        }

        if (userRequest.getRole() != targetUser.getRole()) {
            if (!securityContext.isUserInRole(Role.ADMIN.toString())) {
                return Response.status(403)
                    .entity(new ErrorResponse("You are not allowed to change roles."))
                    .build();
            }

            targetUser.setRole(userRequest.getRole());

            usersRepository.update(targetUser);
        }

        return Response.ok().build();
    }
}
