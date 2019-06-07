package com.gnd.parking.Auth.Filters;

import com.gnd.parking.Auth.Annotations.Secured;
import com.gnd.parking.Auth.SecurityContext;
import com.gnd.parking.Auth.TokenManager;
import com.gnd.parking.Models.Role;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final String REALM = "authentication";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Inject
    TokenManager tokenManager;

    @Inject
    SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            tokenManager.validateToken(token);

            String username = tokenManager.retrieveUsernameFromToken(token);
            Role role = tokenManager.retrieveRoleFromToken(token);

            securityContext.initialize(username, role);
        } catch (Exception e) {
            e.printStackTrace();
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
