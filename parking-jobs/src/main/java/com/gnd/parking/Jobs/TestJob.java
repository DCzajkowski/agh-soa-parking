package com.gnd.parking.Jobs;

import com.gnd.parking.Contracts.Jobs.TestJobInterface;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(TestJobInterface.class)
public class TestJob implements TestJobInterface {

    private String param;

    @Override
    public void setParam(String lol) {
        this.param = lol;
    }

    @Override
    public void run() {
        System.out.println(this.param);
    }
}
