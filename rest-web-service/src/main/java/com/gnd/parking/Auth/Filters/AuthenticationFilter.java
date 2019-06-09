package com.gnd.parking.Auth.Filters;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Auth.Exceptions.TokenValidationException;
import com.gnd.parking.Auth.Models.Token;
import com.gnd.parking.Auth.TokenManager;
import com.gnd.parking.Responses.ErrorResponse;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final String REALM = "authentication";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Inject
    TokenManager tokenManager;

    @Context
    SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String stringToken = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            Token token = tokenManager.validateToken(stringToken);

            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Token getUserPrincipal() {
                    return token;
                }

                @Override
                public boolean isUserInRole(String role_) {
                    return getUserPrincipal().getRole().toString().equals(role_);
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return AUTHENTICATION_SCHEME;
                }
            });
        } catch (TokenValidationException e) {
            abortWithUnauthorized(requestContext);
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null
            && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        Response response = Response.status(Response.Status.UNAUTHORIZED)
            .header(
                HttpHeaders.WWW_AUTHENTICATE,
                String.format("%s realm=\"%s\"", AUTHENTICATION_SCHEME, REALM)
            )
            .entity(new ErrorResponse("You must be authenticated to access this resource."))
            .build();

        requestContext.abortWith(response);
    }
}
