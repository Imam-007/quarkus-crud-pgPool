package org.imam.dao;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.imam.dto.organization.OrganizationWithUsers;
import org.imam.dto.user.User;

import java.time.LocalDateTime;
import java.util.*;

@ApplicationScoped
public class OrganizationDaoDemo {

    @Inject
    PgPool client;

    public Uni<List<OrganizationWithUsers>> fetchOrganizationsWithUsers(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = """
            SELECT
               o.id AS org_id,
               o.name AS org_name,
               o.created_at AS org_created,
               u.id AS user_id,
               u.name AS user_name,
               u.email AS user_email,
               u.created_at AS user_created,
               x.role AS user_role
            FROM organizations o
            LEFT JOIN user_organization_xref x ON o.id = x.organization_id
            LEFT JOIN users u ON x.user_id = u.id
            WHERE o.created_at BETWEEN $1 AND $2
        """;

        return client.preparedQuery(sql).execute(Tuple.of(startDate, endDate))
                .onItem().transform(this::mapRowSetToOrganizations);
    }

    private List<OrganizationWithUsers> mapRowSetToOrganizations(RowSet<Row> rows) {
        List<OrganizationWithUsers> result = new ArrayList<>();
        Map<UUID, OrganizationWithUsers> orgMap = new HashMap<>();

        for (Row row : rows) {
            UUID orgId = row.getUUID("org_id");

            OrganizationWithUsers org = orgMap.computeIfAbsent(orgId, id -> {
                OrganizationWithUsers newOrg = new OrganizationWithUsers(
                        id, row.getString("org_name"), row.getLocalDateTime("org_created"), new ArrayList<>());
                result.add(newOrg);
                return newOrg;
            });

            if (row.getUUID("user_id") != null) {
                org.users.add(new User(
                        row.getUUID("user_id"),
                        row.getString("user_name"),
                        row.getString("user_email"),
                        row.getLocalDateTime("user_created"),
                        row.getString("user_role")
                ));
            }
        }
        return result;
    }
}
