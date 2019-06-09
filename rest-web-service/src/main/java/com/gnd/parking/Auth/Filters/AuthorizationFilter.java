package com.gnd.parking.Auth.Filters;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Auth.Exceptions.LackingPermissionException;
import com.gnd.parking.Models.Role;
import com.gnd.parking.Responses.ErrorResponse;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    @Context
    SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<Role> classRoles = extractRoles(resourceClass);

        Method resourceMethod = resourceInfo.getResourceMethod();
        List<Role> methodRoles = extractRoles(resourceMethod);

        try {
            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles);
            } else {
                checkPermissions(methodRoles);
            }
        } catch (LackingPermissionException e) {
            Response response = Response.status(Response.Status.FORBIDDEN)
                .entity(new ErrorResponse("You do not have permission to access this resource"))
                .build();

            requestContext.abortWith(response);
        }
    }

    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();
        }

        Secured secured = annotatedElement.getAnnotation(Secured.class);

        if (secured == null) {
            return new ArrayList<>();
        }

        Role[] allowedRoles = secured.value();

        return Arrays.asList(allowedRoles);
    }

    private void checkPermissions(List<Role> allowedRoles) throws LackingPermissionException {
        if (allowedRoles.size() == 0) {
            return;
        }

        boolean hasRole = allowedRoles.stream()
            .anyMatch(allowedRole -> securityContext.isUserInRole(allowedRole.toString()));

        if (!hasRole) {
            throw new LackingPermissionException(allowedRoles);
        }
    }
}
