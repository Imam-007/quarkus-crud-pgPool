package org.imam.dto.organization;

import org.imam.dto.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrganizationWithUsers {
    public UUID id;
    public String name;
    public LocalDateTime createdAt;
    public List<User> users;

    public OrganizationWithUsers(UUID id, String name, LocalDateTime createdAt, List<User> users) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.users = users;
    }
}

