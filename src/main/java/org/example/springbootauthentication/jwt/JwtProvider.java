package org.example.springbootauthentication.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.domain.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private String makeToken(Date expireDate, User user) {
        Date now = new Date();

        return Jwts.builder()
                // Header
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)

                // Payload
                .setIssuer(jwtProperties.getIssuer()) // registered claim
                .setIssuedAt(now)                     // registered claim
                .setExpiration(expireDate)            // registered claim
                .setSubject(user.getEmail())          // registered claim
                .claim("id", user.getId())         // private    claim

                // Signature
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }// makeToken

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJwt(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }// validToken

}// JwtProvider
