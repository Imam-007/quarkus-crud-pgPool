package org.imam.services;

import io.smallrye.mutiny.Uni;
import org.imam.dao.OrganizationDAO;
import org.imam.dto.organization.OrganizationCreateRequest;
import org.imam.dto.organization.OrganizationPatchRequest;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    Uni<List<OrganizationDAO>> getAllOrganizations();

    public Uni<OrganizationDAO> getOrganizationById(UUID id);

    Uni<OrganizationDAO> createOrganization(OrganizationCreateRequest organizationCreateRequest);

    Uni<OrganizationDAO> updateOrganization(UUID id, OrganizationPatchRequest organizationPatchRequest);

    Uni<OrganizationDAO> deleteOrganization(UUID id);
}

