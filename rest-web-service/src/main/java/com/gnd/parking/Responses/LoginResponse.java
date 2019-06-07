package com.gnd.parking.Responses;

import com.fasterxml.jackson.annotation.JsonGetter;

public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    @JsonGetter("token")
    public String getToken() {
        return token;
    }
}
