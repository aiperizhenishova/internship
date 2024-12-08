package kg.alatoo.taskmanagementsystem.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenService {


    private String secretKey = Base64.getEncoder().encodeToString("MyComplexSecretKey_123".getBytes());
    private long expirationTime = 900000L; // Время жизни токена 1 час (в миллисекундах)

    // Метод для создания Access токена
    public String createAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Метод для создания Refresh токена
    public String createRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L)) // 1 день
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Метод для верификации токена и извлечения данных
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Метод для проверки, не истек ли токен
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }


}
