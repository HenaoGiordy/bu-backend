package com.univalle.bubackend.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${secret.jwt.key}")
    private String privateKey;

    @Value("${issuer.jwt}")
    private String issuer;

    public String createToken(Authentication auth){

        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            String username = auth.getName();
            String authorities = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 8);
            Date expirationDate = calendar.getTime();

            return JWT.create()
                    .withIssuer(issuer)
                    .withExpiresAt(expirationDate)
                    .withSubject(username)
                    .withClaim("authorities", authorities)
                    .sign(algorithm);

        } catch (JWTCreationException exception){
            // Invalid Signing configuration / Couldn't convert Claims.
            throw new JWTCreationException("Error", exception);
        }

    }

    public DecodedJWT validateToken(String token){

        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Invalid JWT, not authorized");
        }
    }

}
