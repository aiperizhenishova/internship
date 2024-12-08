package kg.alatoo.taskmanagementsystem.controllers;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kg.alatoo.taskmanagementsystem.Dto.LoginRequest;
import kg.alatoo.taskmanagementsystem.Dto.LoginResponse;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.repositories.UserRepository;
import kg.alatoo.taskmanagementsystem.services.JwtTokenService;
import kg.alatoo.taskmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenService jwtTokenService;


    // Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        // Проверка на существующего пользователя
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken");
        }
        userRepository.save(user);  // сохраняет пользователя в базу
        return ResponseEntity.ok("User registered successfully");
    }


    // Вход пользователя
    private final UserService userService;  // Это ваш сервис для работы с пользователями

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        // Здесь должна быть проверка пользователя (например, через базу данных)

        // Генерация токенов
        String accessToken = jwtTokenService.createAccessToken(loginRequest.getEmail());
        String refreshToken = jwtTokenService.createRefreshToken(loginRequest.getEmail());

        return new LoginResponse("Success", accessToken, refreshToken, 200);
    }





    private final String secretKey = "your-secret-key-here";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }


}
