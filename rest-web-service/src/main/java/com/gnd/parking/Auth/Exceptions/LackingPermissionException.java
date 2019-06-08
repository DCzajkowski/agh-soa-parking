package com.gnd.parking.Auth.Exceptions;

import com.gnd.parking.Models.Role;

import java.util.List;
import java.util.stream.Collectors;

public class LackingPermissionException extends Exception {
    public LackingPermissionException(List<Role> allowedRoles) {
        super(
            "Currently authenticated user does not have the required role (any of: "
                + allowedRoles.stream().map(Enum::toString).collect(Collectors.joining(", "))
                + ")"
        );
    }
}
