package com.gnd.parking.Contracts.Services.Auth;

import com.gnd.parking.Models.User;

public interface AuthenticatorServiceInterface {
    boolean authenticate(String username, String password);

    boolean changePassword(User user, String newPassword);

    boolean changePassword(User user, String oldPassword, String newPassword);
}
