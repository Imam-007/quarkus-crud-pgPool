package org.imam.services.impl;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.imam.dao.OrganizationDAO;
import org.imam.dto.organization.OrganizationCreateRequest;
import org.imam.dto.organization.OrganizationPatchRequest;
import org.imam.exceptions.OrganizationNotFoundException;
import org.imam.services.OrganizationService;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrganizationServiceImpl implements OrganizationService {
    @Inject
    PgPool pgPool;

    @Override
    public Uni<List<OrganizationDAO>> getAllOrganizations() {
        return OrganizationDAO.getAllOrganizations(pgPool)
                .onItem().ifNull().failWith(new RuntimeException("No organizations found"));
    }

    @Override
    public Uni<OrganizationDAO> getOrganizationById(UUID id) {
        return pgPool.preparedQuery("SELECT * FROM organizations WHERE id = $1")
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> {
                    if (iterator.hasNext()) {
                        return OrganizationDAO.project(iterator.next());
                    } else {
                        throw new OrganizationNotFoundException("Organization with ID " + id + " not found.");
                    }
                });
    }

    @Override
    public Uni<OrganizationDAO> createOrganization(OrganizationCreateRequest organizationCreateRequest) {
        return OrganizationDAO.createOrganization(pgPool, organizationCreateRequest.name(), organizationCreateRequest.location());
    }

    @Override
    public Uni<OrganizationDAO> updateOrganization(UUID id, OrganizationPatchRequest organizationPatchRequest) {
        return pgPool.withTransaction(conn ->
                OrganizationDAO.updateOrganization(conn, id,  organizationPatchRequest.name(),  organizationPatchRequest.location()));
    }

    @Override
    public Uni<OrganizationDAO> deleteOrganization(UUID id) {
        return pgPool.withTransaction(conn ->
                OrganizationDAO.deleteOrganization(pgPool, id));
    }
}


