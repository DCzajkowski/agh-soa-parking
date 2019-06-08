package com.gnd.parking.Auth.Filters;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Auth.Exceptions.TokenParseException;
import com.gnd.parking.Auth.Exceptions.TokenValidationException;
import com.gnd.parking.Auth.Models.Token;
import com.gnd.parking.Auth.TokenManager;
import com.gnd.parking.Models.Role;

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
import java.security.Principal;

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
            String username;
            Role role;

            try {
                username = token.getUsername();
                role = token.getRole();
            } catch (TokenParseException e) {
                throw new RuntimeException(e);
            }

            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> username;
                }

                @Override
                public boolean isUserInRole(String role_) {
                    return role.toString().equals(role_);
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

    /**
     * Check if the Authorization header is valid
     * It must not be null and must be prefixed with "Bearer" plus a whitespace
     * The authentication scheme comparison must be case-insensitive
     */
    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null
            && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    /**
     * Abort the filter chain with a 401 status code response
     * The WWW-Authenticate header is sent along with the response
     */
    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                .header(
                    HttpHeaders.WWW_AUTHENTICATE,
                    String.format("%s realm=\"%s\"", AUTHENTICATION_SCHEME, REALM)
                )
                .build()
        );
    }
}
