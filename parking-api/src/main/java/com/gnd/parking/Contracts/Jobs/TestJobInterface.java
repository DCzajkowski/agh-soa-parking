package com.gnd.parking.Contracts.Jobs;

public interface TestJobInterface extends Runnable {
    void setParam(String lol);

    void run();
}
