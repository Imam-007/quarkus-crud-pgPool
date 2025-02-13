package org.imam.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCreateRequest(
        @NotNull
        @Min(4)
        String name,

        @NotNull
        @Email(message = "Email should be a valid email address")
        String email,

        @NotNull(message = "User Mobile number not null")
        String num,

        @NotNull(message = "Role cannot be null")
        String role,

        @NotNull(message = "Organization ID cannot be null")
        UUID organization_id
) {}
