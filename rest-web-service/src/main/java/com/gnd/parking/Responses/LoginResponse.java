package com.gnd.parking.Responses;

import com.fasterxml.jackson.annotation.JsonGetter;

public class LoginResponse {
    @JsonGetter("token")
    public String token;
}
