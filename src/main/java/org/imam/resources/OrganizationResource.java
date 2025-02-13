package org.imam.resources;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import org.imam.dao.OrganizationDAO;
import org.imam.dto.organization.OrganizationCreateRequest;
import org.imam.dto.organization.OrganizationPatchRequest;
import org.imam.services.OrganizationService;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("/organizations")
public class OrganizationResource {
    @Inject
    OrganizationService organizationService;

    @GET
    public Uni<List<OrganizationDAO>> getAllOrganizations(){
        return organizationService.getAllOrganizations();
    }

    @GET
    @Path("/{id}")
    public Uni<OrganizationDAO> getOrganizationById(@PathParam("id") UUID id) {
        return organizationService.getOrganizationById(id);
    }

    @POST
    @RolesAllowed("admin")
    public Uni<OrganizationDAO> createOrganization(OrganizationCreateRequest organizationCreateRequest){
        return organizationService.createOrganization(organizationCreateRequest);
    }

    @PATCH
    @Path("/{id}")
    @RolesAllowed("admin")
    public Uni<OrganizationDAO> updateOrganization(@PathParam("id") UUID id, @Valid OrganizationPatchRequest organizationPatchRequest){
        return organizationService.updateOrganization(id, organizationPatchRequest);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Uni<OrganizationDAO> deleteOrganization(@PathParam("id") UUID id){
        return organizationService.deleteOrganization(id);
    }
}

