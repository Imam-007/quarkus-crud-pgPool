package org.imam.utils;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class JwtUtil {

    public String generateToken(){
        Set<String> roles = new HashSet<>(List.of("admin"));
        return Jwt.issuer("http://localhost:8080")
                .subject("mail")
                .groups(roles)
                .expiresAt(System.currentTimeMillis() + 3600)
                .sign();
    }
}

