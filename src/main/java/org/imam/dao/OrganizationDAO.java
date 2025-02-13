package org.imam.dao;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrganizationDAO {
    public UUID id;

    public String name;

    public String location;

    public static OrganizationDAO project(Row row) {
        var organization = new OrganizationDAO();
        organization.id = row.getUUID("id");
        organization.name = row.getString("name");
        organization.location = row.getString("location");

        return organization;
    }

    public static Uni<List<OrganizationDAO>> getAllOrganizations(PgPool pgPool) {
        return pgPool.preparedQuery("SELECT * FROM organizations")
                .execute()
                .onFailure().invoke(msg -> Log.error("Sql (SqlConnection) : Failed to fetch organizations", msg))
                .map(RowSet::iterator)
                .map(iterator -> {
                    List<OrganizationDAO> organizations = new ArrayList<>();
                    while (iterator.hasNext()) {
                        organizations.add(project(iterator.next()));
                    }
                    return organizations;
                });
    }

    public static Uni<OrganizationDAO> getOrganizationById(PgPool pgPool, UUID id) {
        return pgPool.preparedQuery("SELECT * FROM organizations WHERE id = $1")
                .execute(Tuple.of(id))
                .onFailure().invoke(msg -> Log.error("SQL: Failed to fetch organization by ID", msg))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? project(iterator.next()) : null);
    }

    public static Uni<OrganizationDAO> createOrganization(PgPool pgpool, String name, String location){
        return pgpool.preparedQuery("INSERT INTO organizations (name, location) VALUES ($1, $2) RETURNING *")
                .execute(Tuple.of(name, location))
                .onFailure().invoke(msg -> Log.error("Sql (SqlConnection) : Failed to create organization", msg))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? project(iterator.next()) : null);
    }

    public static Uni<OrganizationDAO> updateOrganization(SqlConnection pgpool, UUID id, String name, String location){
        return pgpool.preparedQuery("UPDATE organizations SET name = $1, location = $2 WHERE id = $3 RETURNING *")
                .execute(Tuple.of(name, location, id))
                .onFailure().invoke(msg -> Log.error("Sql (SqlConnection) : Failed to update organization", msg))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? project(iterator.next()) : null);
    }

    public static Uni<OrganizationDAO> deleteOrganization(PgPool pgpool, UUID id){
        return pgpool.preparedQuery("DELETE FROM organizations WHERE id = $1 RETURNING *")
                .execute(Tuple.of(id))
                .onFailure().invoke(msg -> Log.error("Sql (SqlConnection) : Failed to delete organization", msg))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? project(iterator.next()) : null);
    }
}

