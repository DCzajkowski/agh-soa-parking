package com.gnd.parking.Jobs;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(Runnable.class)
public class TestJob implements Runnable {
    @Override
    public void run() {
        System.out.println("test run");
    }
}
