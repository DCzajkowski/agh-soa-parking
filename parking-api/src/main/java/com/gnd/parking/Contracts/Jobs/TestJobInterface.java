package com.gnd.parking.Contracts.Jobs;

import com.gnd.parking.Models.ParkingSpot;

public interface TestJobInterface extends Runnable {
    void setParam(String lol);
    void run();
}
