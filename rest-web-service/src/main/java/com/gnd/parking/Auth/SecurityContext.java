package com.gnd.parking.Auth;

import com.gnd.parking.Models.Role;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class SecurityContext {
    private String username;
    private Role role;

    public void initialize(String username, Role role) {
        if (this.username != null || this.role != null) {
            throw new RuntimeException("Do not initialize the SecurityContext twice.");
        }

        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}
