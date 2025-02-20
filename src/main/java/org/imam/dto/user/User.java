package org.imam.dto.user;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    public UUID id;
    public String name;
    public String email;
    public LocalDateTime createdAt;
    public String role;  // Added role field

    public User(UUID id, String name, String email, LocalDateTime createdAt, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.role = role;
    }
}
