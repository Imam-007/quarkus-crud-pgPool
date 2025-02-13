package org.imam.services;

import io.smallrye.mutiny.Uni;
import org.imam.dao.UserDAO;
import org.imam.dto.user.UserCreateRequest;
import org.imam.dto.user.UserPatchRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    Uni<List<UserDAO>> getAllUsers();

    public Uni<UserDAO> getUserById(UUID id);

    public Uni<List<UserDAO>> getUsersByOrganizationId(UUID organizationId);

    Uni<UserDAO> createUser(UserCreateRequest userCreateRequest);

    Uni<UserDAO> updateUser(UUID id, UserPatchRequest userPatchRequest);

    Uni<UserDAO> deleteUser(UUID id);
}

