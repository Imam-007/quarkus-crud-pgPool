package org.imam.services.impl;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.imam.dao.UserDAO;
import org.imam.dto.user.UserCreateRequest;
import org.imam.dto.user.UserPatchRequest;
import org.imam.exceptions.UserNotFoundException;
import org.imam.services.UserService;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    @Inject
    PgPool pgPool;

    @Override
    public Uni<List<UserDAO>> getAllUsers() {
        return UserDAO.getAllUsers(pgPool)
                .onItem().ifNull().failWith(new RuntimeException("No users found"));
    }

    @Override
    public Uni<UserDAO> getUserById(UUID id) {
        return pgPool.preparedQuery("SELECT * FROM users WHERE id = $1")
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> {
                    if (iterator.hasNext()) {
                        return UserDAO.project(iterator.next());
                    } else {
                        throw new UserNotFoundException("User with ID " + id + " not found.");
                    }
                });
    }

    @Override
    public Uni<List<UserDAO>> getUsersByOrganizationId(UUID organizationId) {
        return UserDAO.getUsersByOrganizationId(pgPool, organizationId)
                .onItem().ifNull().failWith(new RuntimeException("No users found for this organization"));
    }

    @Override
    public Uni<UserDAO> createUser(UserCreateRequest userCreateRequest) {
        return pgPool.withTransaction(conn ->
                UserDAO.createUser(conn, userCreateRequest.name(), userCreateRequest.email(), String.valueOf(userCreateRequest.role()), userCreateRequest.num(), userCreateRequest.organization_id()));
    }

    @Override
    public Uni<UserDAO> updateUser(UUID id, UserPatchRequest userPatchRequest) {
        return pgPool.withTransaction(conn ->
                UserDAO.updateUser(conn, id, userPatchRequest.name(),  userPatchRequest.email(), String.valueOf(userPatchRequest.role()), userPatchRequest.num(), userPatchRequest.organizationId()));
    }

    @Override
    public Uni<UserDAO> deleteUser(UUID id) {
        return pgPool.withTransaction(conn ->
                UserDAO.deleteUser(conn, id));
    }
}

