package com.gnd.parking.Auth;

import com.gnd.parking.Models.Role;
import com.gnd.parking.Models.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.ApplicationScoped;
import java.text.ParseException;
import java.util.Date;

@ApplicationScoped
public class TokenManager {
    final private static String key = "55624f1dd08d1dc71fa2609c79111337";
    private static final String ISSUER = "royal-parking.com";
    private static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    final private MACSigner signer;

    public TokenManager() throws KeyLengthException {
        signer = new MACSigner(key);
    }

    public String issueToken(User user) throws JOSEException {
        Date now = new Date();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .issuer(ISSUER)
            .claim("role", user.getRole())
            .expirationTime(new Date(now.getTime() + EXPIRATION_TIME))
            .notBeforeTime(now)
            .issueTime(now)
            .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public void validateToken(String stringToken) throws JOSEException, ParseException {
        SignedJWT token = SignedJWT.parse(stringToken);
        MACVerifier verifier = new MACVerifier(key.getBytes());

        if (!token.verify(verifier)) {
            throw new JOSEException("Wrong token signature.");
        }

        Date referenceTime = new Date();
        JWTClaimsSet claims = token.getJWTClaimsSet();

        Date expirationTime = claims.getExpirationTime();

        if (expirationTime == null || expirationTime.before(referenceTime)) {
            throw new JOSEException("The token has expired.");
        }
    }

    public String retrieveUsernameFromToken(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
    }

    public Role retrieveRoleFromToken(String token) throws ParseException {
        return Role.valueOf((String) SignedJWT.parse(token).getJWTClaimsSet().getClaim("role"));
    }
}
