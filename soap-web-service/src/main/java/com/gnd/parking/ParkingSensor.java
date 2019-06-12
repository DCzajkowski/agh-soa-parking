package com.gnd.parking;

import com.gnd.parking.Contracts.Services.Auth.AuthenticatorServiceInterface;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotException;
import com.gnd.parking.Contracts.Services.ParkingSpots.ParkingSpotReleaserServiceInterface;
import com.gnd.parking.Contracts.Services.ParkingSpots.ParkingSpotTakerServiceInterface;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.AuthenticationException;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

@WebService
public class ParkingSensor {

    @Resource
    WebServiceContext webServiceContext;

    @EJB(lookup = "java:global/parking-logic-1.0/AuthenticatorService")
    AuthenticatorServiceInterface authenticatorService;

    @EJB(lookup = "java:global/parking-logic-1.0/ParkingSpotTakerService")
    ParkingSpotTakerServiceInterface parkingSpotTaker;

    @EJB(lookup = "java:global/parking-logic-1.0/ParkingSpotReleaserService")
    ParkingSpotReleaserServiceInterface parkingSpotReleaser;

    @WebMethod
    public boolean takeParkingSpot(Integer spotId) throws ParkingSpotException, AuthenticationException {
        authorize();

        parkingSpotTaker.takeParkingSpot(spotId);

        return true;
    }

    @WebMethod
    public boolean releaseParkingSpot(Integer spotId) throws ParkingSpotException, AuthenticationException {
        authorize();

        parkingSpotReleaser.releaseParkingSpot(spotId);

        return true;
    }

    public void authorize() throws AuthenticationException {
        MessageContext messageContext = webServiceContext.getMessageContext();

        Map<?, ?> requestHeaders = (Map<?, ?>) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        List<?> usernameList = (List<?>) requestHeaders.get("username");
        List<?> passwordList = (List<?>) requestHeaders.get("password");

        String username = "";
        String password = "";

        if (usernameList != null) {
            username = usernameList.get(0).toString();
        }

        if (passwordList != null) {
            password = passwordList.get(0).toString();
        }

        if (!authenticatorService.authenticate(username, password)) {
            throw new AuthenticationException("The username password combo is not correct.");
        }
    }
}
