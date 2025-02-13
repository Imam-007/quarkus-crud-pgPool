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

public class UserDAO {
    public UUID id;

    public String name;

    public String email;

    public String role;

    public String num;

    public UUID organizationId;

    public static UserDAO project(Row row) {
        var user = new UserDAO();
        user.id = row.getUUID("id");
        user.name = row.getString("name");
        user.email = row.getString("email");
        user.role = row.getString("role");
        user.num = row.getString("num");

        user.organizationId = row.getUUID("organization_id");

        return user;
    }

    public static Uni<List<UserDAO>> getAllUsers(PgPool pgPool) {
        return pgPool.preparedQuery("SELECT * FROM users")
                .execute()
                .onFailure().invoke(msg -> Log.error("Sql (SqlConnection) : Failed to fetch organizations", msg))
                .map(RowSet::iterator)
                .map(iterator -> {
                    List<UserDAO> users = new ArrayList<>();
                    while (iterator.hasNext()) {
                        users.add(project(iterator.next()));
                    }
                    return users;
                });
    }

    public static Uni<UserDAO> createUser(SqlConnection pgpool, String name, String email, String role, String no, UUID organizationId) {
        return pgpool.preparedQuery(
                        "INSERT INTO users (name, email, num, role, organization_id) VALUES ($1, $2, $3, $4, $5) RETURNING *"
                )
                .execute(Tuple.of(name, email, no, role, organizationId))
                .onFailure().invoke(msg -> Log.error("Sql (SqlConnection) : Failed to create user", msg))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? project(iterator.next()) : null);
    }

    public static Uni<UserDAO> getUserById(PgPool pgPool, UUID id) {
        return pgPool.preparedQuery("SELECT * FROM users WHERE id = $1")
                .execute(Tuple.of(id))
                .onFailure().invoke(msg -> Log.error("SQL: Failed to fetch user by ID", msg))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? project(iterator.next()) : null);
    }

    public static Uni<UserDAO> updateUser(SqlConnection pgpool, UUID id, String name, String email, String role, String num, UUID organizationId) {
        return pgpool.preparedQuery("UPDATE users SET name = $1, email = $2, role = $3, num = $4, organization_id = $5 WHERE id = $6 RETURNING *")
                .execute(Tuple.of(name, email, role, num, organizationId, id))
                .onFailure().invoke(msg -> Log.error("Sql (SqlConnection) : Failed to update user", msg))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? project(iterator.next()) : null);
    }

    public static Uni<UserDAO> deleteUser(SqlConnection pgpool, UUID id) {
        return pgpool.preparedQuery("DELETE FROM users WHERE id = $1 RETURNING *")
                .execute(Tuple.of(id))
                .onFailure().invoke(msg -> Log.error("Sql (SqlConnection) : Failed to delete user", msg))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? project(iterator.next()) : null);
    }

    public static Uni<List<UserDAO>> getUsersByOrganizationId(PgPool pgPool, UUID organizationId) {
        return pgPool.preparedQuery("SELECT * FROM users WHERE organization_id = $1")
                .execute(Tuple.of(organizationId))
                .onFailure().invoke(msg -> Log.error("SQL: Failed to fetch users by organization ID", msg))
                .map(RowSet::iterator)
                .map(iterator -> {
                    List<UserDAO> users = new ArrayList<>();
                    while (iterator.hasNext()) {
                        users.add(project(iterator.next()));
                    }
                    return users;
                });
    }
}

