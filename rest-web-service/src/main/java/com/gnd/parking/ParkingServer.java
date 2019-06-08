package com.gnd.parking;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class ParkingServer extends Application {
    private Set<Object> singletons = new HashSet<>();

    ParkingServer() {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");

        singletons.add(corsFilter);
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
