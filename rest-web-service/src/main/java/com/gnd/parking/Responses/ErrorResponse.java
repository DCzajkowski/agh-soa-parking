package com.gnd.parking.Responses;

import com.fasterxml.jackson.annotation.JsonGetter;

public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    @JsonGetter("error")
    public String getMessage() {
        return message;
    }
}
