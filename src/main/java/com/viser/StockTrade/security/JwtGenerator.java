package com.viser.StockTrade.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtGenerator {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * Generates a JWT token based on the provided authentication information.
     *
     * This method creates a JWT token that includes the username as the subject, sets the issued date to the current date,
     * and specifies an expiration date based on a predefined expiration period. The token is signed with the HS512
     * algorithm using a secret key.
     *
     * @param authentication the {@link Authentication} object containing the authentication details of the user
     * @return a {@link String} representing the generated JWT token
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.jwtExpiration);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        System.out.println("New token :");
        System.out.println(token);
        return token;
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * This method parses the provided JWT token to retrieve the claims and then extracts the subject, which is
     * assumed to be the username of the authenticated user. The method verifies the token's signature using the
     * specified signing key to ensure its validity before parsing.
     *
     * @param token the JWT token from which the username is to be extracted
     * @return the username contained in the JWT token, or {@code null} if the token is invalid or does not contain a subject
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Validates the given JWT token by checking its signature and claims.
     *
     * This method parses the provided JWT token using the specified signing key. If the token's signature is valid
     * and the token can be successfully parsed, the method returns {@code true}. If there are any issues with the token,
     * such as an invalid signature or an expired token, the method returns {@code false}.
     *
     * @param token the JWT token to be validated
     * @return {@code true} if the token is valid and can be parsed successfully, {@code false} otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
