package com.gnd.parking.Auth;

import com.gnd.parking.Auth.Exceptions.TokenHasExpiredException;
import com.gnd.parking.Auth.Exceptions.TokenIssueException;
import com.gnd.parking.Auth.Exceptions.TokenSignatureVerificationException;
import com.gnd.parking.Auth.Exceptions.TokenValidationException;
import com.gnd.parking.Auth.Models.Token;
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
    final private static String ISSUER = "royal-parking.com";
    final private static int EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    final private MACSigner signer;

    public TokenManager() throws KeyLengthException {
        signer = new MACSigner(key);
    }

    public String issueToken(User user) throws TokenIssueException {
        Date now = new Date();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .issuer(ISSUER)
            .claim("id", user.getId())
            .claim("role", user.getRole())
            .claim("region_id", user.getRegion() != null ? user.getRegion().getId() : null)
            .expirationTime(new Date(now.getTime() + EXPIRATION_TIME))
            .notBeforeTime(now)
            .issueTime(now)
            .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);

        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new TokenIssueException("There was a problem with signing of the token");
        }

        return signedJWT.serialize();
    }

    public Token validateToken(String stringToken) throws TokenValidationException {
        try {
            SignedJWT token = SignedJWT.parse(stringToken);
            MACVerifier verifier = new MACVerifier(key.getBytes());

            if (!token.verify(verifier)) {
                throw new TokenSignatureVerificationException();
            }

            Date referenceTime = new Date();
            JWTClaimsSet claims = token.getJWTClaimsSet();

            Date expirationTime = claims.getExpirationTime();

            if (expirationTime == null || expirationTime.before(referenceTime)) {
                throw new TokenHasExpiredException();
            }

            return new Token(token);
        } catch (JOSEException | ParseException e) {
            throw new TokenValidationException(e.getMessage());
        }
    }
}
