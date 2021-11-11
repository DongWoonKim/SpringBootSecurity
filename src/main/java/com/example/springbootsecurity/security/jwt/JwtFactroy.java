package com.example.springbootsecurity.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtFactroy {

    private static final Logger log = LoggerFactory.getLogger(JwtFactroy.class);

    public String generateToken(UserDetails userDetails) {
        String token = null;

        try {
            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(r -> r.getAuthority()).collect(Collectors.toSet());
            String role = roles.iterator().next();

            token = JWT.create()
                    .withIssuer("Kim")
                    .withClaim("USERNAME", userDetails.getUsername())
                    .withClaim("USER_ROLE", role)
                    .withClaim("EXP", new Date(System.currentTimeMillis()+ 864000000))
                    .sign(generateAlgorithm());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        return token;
    }

    public Algorithm generateAlgorithm() throws UnsupportedEncodingException {
        String signingKey = "jwttest";
        return Algorithm.HMAC256(signingKey);
    }

}
