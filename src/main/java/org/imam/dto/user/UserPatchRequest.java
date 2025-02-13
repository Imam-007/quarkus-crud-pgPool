package org.imam.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserPatchRequest(
        @NotNull
        @Size(min = 4, message = "Name must have at least 4 characters")
        String name,

        @NotNull
        @Email(message = "Email should be a valid email address")
        String email,

        @NotNull(message = "Role cannot be null")
        String role,

        @NotNull(message = "Number not be null")
        String num,

        @NotNull(message = "Organization ID cannot be null")
        UUID organizationId
) {}
