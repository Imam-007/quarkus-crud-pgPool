package org.imam.dto.organization;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrganizationPatchRequest(
        @NotNull
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotNull
        @Size(min = 2, max = 50, message = "Location must be between 2 and 50 characters")
        String location
        ) {}
