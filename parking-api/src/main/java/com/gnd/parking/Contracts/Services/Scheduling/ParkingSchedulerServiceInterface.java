package com.gnd.parking.Contracts.Services.Scheduling;

import java.util.concurrent.TimeUnit;

public interface ParkingSchedulerServiceInterface {
    void schedule(Runnable job, long delay, TimeUnit timeunit);
}
