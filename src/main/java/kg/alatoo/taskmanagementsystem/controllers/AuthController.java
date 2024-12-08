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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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


    /*
    // Вход пользователя
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {

        // Генерация токенов
        String accessToken = jwtTokenService.createAccessToken(loginRequest.getEmail());
        String refreshToken = jwtTokenService.createRefreshToken(loginRequest.getEmail());

        return new LoginResponse("Success", accessToken, refreshToken, 200);
    }

     */




    private final String secretKey = "your-secret-key-here";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }




    private final PasswordEncoder passwordEncoder;
    private final String SECRET_KEY = "secret123"; // Секретный ключ для JWT

    public AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Генерация токенов
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 час
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 часа
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        // Ответ
        Map<String, Object> response = new HashMap<>();
        response.put("message", "success");
        response.put("token", token);
        response.put("refreshToken", refreshToken);
        response.put("expiresIn", 3600); // В секундах

        return response;
    }





}
