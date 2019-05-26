package com.gnd.parking;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class HelloWorld {
    @WebMethod
    public String sayHelloWorldFrom(String from) {
        return "Hello, world, from " + from;
    }
}
