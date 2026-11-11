package com.pasaporte.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clave secreta de firma (debe tener al menos 256 bits / 32 caracteres)
    private static final String CLAVE_SECRETA = "clave_secreta_super_segura_para_el_pasaporte_digital_tfg_2026";

    // Tiempo de validez del token: 24 horas (en milisegundos)
    private static final long TIEMPO_EXPIRACION = 86400000;

    private SecretKey obtenerLlave() {
        return Keys.hmacShaKeyFor(CLAVE_SECRETA.getBytes(StandardCharsets.UTF_8));
    }

    // Genera un token JWT firmado usando el email del usuario
    public String generarToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TIEMPO_EXPIRACION))
                .signWith(obtenerLlave())
                .compact();
    }

    // Extrae el email del usuario desde el token recibido
    public String obtenerEmailDelToken(String token) {
        return obtenerClaim(token, Claims::getSubject);
    }

    // Valida si el token pertenece al usuario y no ha expirado
    public boolean validarToken(String token, String emailUsuario) {
        final String email = obtenerEmailDelToken(token);
        return (email.equals(emailUsuario) && !estaExpirado(token));
    }

    private boolean estaExpirado(String token) {
        return obtenerClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T obtenerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(obtenerLlave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}