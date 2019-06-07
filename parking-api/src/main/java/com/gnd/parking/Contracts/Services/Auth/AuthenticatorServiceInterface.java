package com.gnd.parking.Contracts.Services.Auth;

public interface AuthenticatorServiceInterface {
    boolean authenticate(String username, String password);
}
