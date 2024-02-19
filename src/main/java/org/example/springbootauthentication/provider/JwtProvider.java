package org.example.springbootauthentication.provider;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.properties.JwtProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public static final String PRIVATE_CLAIM = "id";

    public String generateToken(Duration expiredAt, UserDTO userDTO) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiredAt.toMillis());
        return makeToken(expiredDate, userDTO);
    }

    public String makeToken(Date expireDate, UserDTO userDTO) {
        Date now = new Date();

        return Jwts.builder()
                // Header
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)

                // Payload
                .setIssuer(jwtProperties.getIssuer())  // registered claim
                .setIssuedAt(now)                      // registered claim
                .setExpiration(expireDate)             // registered claim
                .setSubject(userDTO.getEmail())        // registered claim
                .claim(PRIVATE_CLAIM, userDTO.getId()) // private    claim

                // Signature
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }// makeToken

    public void validToken(String token) throws Exception{
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

    public Long getPrivateClaim(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token).getBody();

        return claims.get(PRIVATE_CLAIM, Long.class);
    }// getAuthentication

}// JwtProvider

















