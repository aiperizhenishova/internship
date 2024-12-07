package kg.alatoo.taskmanagementsystem.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String secretKey = "mySecretKey"; // Секретный ключ для подписи токенов

    private static final long expirationTime = 900000; // Время жизни токена

    // Метод для создания JWT токена
    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);  // добавляем только email в токен

        return Jwts.builder()
                .setClaims(claims)  // Устанавливаем claims с email
                .setIssuedAt(new Date())  // Время выдачи токена
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // Время истечения токена
                .signWith(SignatureAlgorithm.HS512, secretKey)  // Подписываем токен секретом
                .compact();
    }
}
