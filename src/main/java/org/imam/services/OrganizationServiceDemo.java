package org.imam.services;

import io.smallrye.mutiny.Uni;
import org.imam.dto.organization.OrganizationWithUsers;

import java.time.LocalDateTime;
import java.util.List;

public interface OrganizationServiceDemo {
    public Uni<List<OrganizationWithUsers>> getOrganizationsWithUsers(LocalDateTime startDate, LocalDateTime endDate);
}
