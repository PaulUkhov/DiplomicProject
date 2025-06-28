package org.example.diplomicproject.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
@Tag(name = "Генерация токена")
@Component
public class JwtUtil {

    @Value("${jwtsecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private long jwtExpirationMs; // access token срок (например, 15 минут)

    @Value("${jwtRefreshExpirationMs}")
    private long jwtRefreshExpirationMs; // refresh token срок (например, 7 дней)

    // 🔐 Генерация access токена (короткоживущий)
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // 🔄 Генерация refresh токена (долгоживущий, без ролей)
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // 📤 Получение username (subject) из токена
    public String getUsernameFromToken(String token) {
        return parseToken(token).getBody().getSubject();
    }

    // ✅ Проверка токена (на валидность и срок)
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ⌛ Проверка — просрочен ли токен
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = parseToken(token).getBody().getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true; // если парсинг не удался — считаем недействительным
        }
    }

    // 🔍 Вспомогательный метод — парсинг токена
    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token);
    }
}

