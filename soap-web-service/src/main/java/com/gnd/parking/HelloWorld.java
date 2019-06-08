package com.gnd.parking;

import com.gnd.parking.Contracts.Services.Scheduling.ParkingSchedulerServiceInterface;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.concurrent.TimeUnit;

@WebService
public class HelloWorld {

    @EJB(lookup = "java:global/parking-jobs-1.0/TestJob")
    Runnable testJob;

    @EJB(lookup = "java:global/parking-jobs-1.0/ParkingSchedulerService")
    ParkingSchedulerServiceInterface parkingSchedulerService;

    @WebMethod
    public String sayHelloWorldFrom(String from) {
        parkingSchedulerService.schedule(testJob, 5, TimeUnit.SECONDS);
        return "Hello, world, from " + from;
    }
}
