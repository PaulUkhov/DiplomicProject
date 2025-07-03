package org.example.diplomicproject.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Tag(name = "Генерация токена")
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwtsecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${jwtRefreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    @PostConstruct
    public void init() {
        logger.info("JwtUtil инициализирован. Срок access токена: {} мс, refresh токена: {} мс", jwtExpirationMs, jwtRefreshExpirationMs);
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        logger.info("Генерация access токена для пользователя: {}", username);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        logger.debug("Access токен сгенерирован: {}", token);
        return token;
    }

    public String generateRefreshToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        logger.info("Генерация refresh токена для пользователя: {}", username);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        logger.debug("Refresh токен сгенерирован: {}", token);
        return token;
    }

    public String getUsernameFromToken(String token) {
        try {
            String username = parseToken(token).getBody().getSubject();
            logger.info("Извлечён username из токена: {}", username);
            return username;
        } catch (Exception e) {
            logger.warn("Ошибка при извлечении username из токена: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            logger.info("Токен прошёл валидацию");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Невалидный токен: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = parseToken(token).getBody().getExpiration();
            boolean expired = expiration.before(new Date());
            logger.info("Проверка срока действия токена: истёк = {}", expired);
            return expired;
        } catch (Exception e) {
            logger.warn("Не удалось проверить срок действия токена: {}", e.getMessage());
            return true;
        }
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token);
    }
}
