package com.gnd.parking.Controllers;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Auth.Models.Token;
import com.gnd.parking.Contracts.Services.JMS.NotificationReceiverServiceInterface;
import com.gnd.parking.Models.Role;

import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("notifications")
public class NotificationsController {
    @Context
    SecurityContext securityContext;

    @EJB(lookup = "java:global/parking-jms-1.0/NotificationReceiverService")
    NotificationReceiverServiceInterface notificationReceiverService;

    @GET
    @Secured({Role.EMPLOYEE})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        Token user = ((Token) securityContext.getUserPrincipal());

        int regionId = user.getRegionId();

        try {
            List<String> notifications = notificationReceiverService
                .receiveNotificationsForRegion(regionId);

            return Response.ok(notifications).build();
        } catch (JMSException e) {
            return Response.status(500).build();
        }
    }
}
