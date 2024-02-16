package org.example.springbootauthentication.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.domain.User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private static final String PRIVATE_CLAIM = "id";

    public String generateToken(Duration expiredAt, User user) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiredAt.toMillis());
        return makeToken(expiredDate, user);
    }

    public String makeToken(Date expireDate, User user) {
        Date now = new Date();

        return Jwts.builder()
                // Header
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)

                // Payload
                .setIssuer(jwtProperties.getIssuer()) // registered claim
                .setIssuedAt(now)                     // registered claim
                .setExpiration(expireDate)            // registered claim
                .setSubject(user.getEmail())          // registered claim
                .claim(PRIVATE_CLAIM, user.getId())   // private    claim

                // Signature
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }// makeToken

    public void validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }// validToken

}// JwtProvider
