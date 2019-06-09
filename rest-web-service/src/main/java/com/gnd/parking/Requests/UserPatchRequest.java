package com.gnd.parking.Requests;

import com.gnd.parking.Models.Role;

public class UserPatchRequest {
    private String oldPassword = "";
    private String newPassword = "";
    private Role role;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
