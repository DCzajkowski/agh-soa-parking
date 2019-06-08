package com.gnd.parking;

import com.gnd.parking.Contracts.Services.Scheduling.ParkingSchedulerServiceInterface;

import javax.ejb.Remote;
import javax.ejb.Singleton;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Remote(ParkingSchedulerServiceInterface.class)
public class ParkingSchedulerService implements ParkingSchedulerServiceInterface {

    private ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(5);

    @Override
    public void schedule(Runnable job, long delay, TimeUnit timeunit) {
        scheduledExecutorService.schedule(job,delay,timeunit);
    }
}
