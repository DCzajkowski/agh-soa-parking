package com.gnd.parking.Controllers;

import com.gnd.parking.Auth.Exceptions.TokenIssueException;
import com.gnd.parking.Auth.TokenManager;
import com.gnd.parking.Contracts.Repositories.UsersRepositoryInterface;
import com.gnd.parking.Contracts.Services.Auth.AuthenticatorServiceInterface;
import com.gnd.parking.Requests.LoginRequest;
import com.gnd.parking.Responses.LoginResponse;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginController {
    @EJB(lookup = "java:global/parking-implementation-1.0/UsersRepository")
    UsersRepositoryInterface usersRepository;

    @EJB(lookup = "java:global/parking-implementation-1.0/AuthenticatorService")
    AuthenticatorServiceInterface authenticatorService;

    @Inject
    TokenManager tokenManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(
        LoginRequest loginRequest
    ) throws TokenIssueException {
        if (!authenticatorService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        String token = tokenManager.issueToken(usersRepository.find(loginRequest.getUsername()));

        return Response.ok(new LoginResponse(token)).build();
    }
}
