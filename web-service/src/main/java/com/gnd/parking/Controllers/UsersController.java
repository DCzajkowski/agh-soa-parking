package com.gnd.parking.Controllers;

import com.gnd.parking.Contracts.UsersRepositoryInterface;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("users")
public class UsersController {
    @EJB(lookup = "java:global/parking-implementation-1.0/UsersRepository")
    UsersRepositoryInterface usersRepository;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "Hello, World! - " + usersRepository.all().size();
    }
}
