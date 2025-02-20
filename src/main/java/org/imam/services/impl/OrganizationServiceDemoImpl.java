package org.imam.services.impl;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.imam.dao.OrganizationDaoDemo;
import org.imam.dto.organization.OrganizationWithUsers;
import org.imam.services.OrganizationServiceDemo;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class OrganizationServiceDemoImpl implements OrganizationServiceDemo {

    @Inject
    OrganizationDaoDemo organizationDaoDemo;

    public Uni<List<OrganizationWithUsers>> getOrganizationsWithUsers(LocalDateTime startDate, LocalDateTime endDate) {
        return organizationDaoDemo.fetchOrganizationsWithUsers(startDate, endDate);
    }
}

