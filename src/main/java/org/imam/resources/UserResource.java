package org.imam.resources;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.imam.dao.UserDAO;
import org.imam.dto.user.UserCreateRequest;
import org.imam.dto.user.UserPatchRequest;
import org.imam.services.UserService;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("/users")
public class UserResource {
    @Inject
    UserService userService;

    @GET
    public Uni<List<UserDAO>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    public Uni<UserDAO> getUserById(@PathParam("id") UUID id) {
        return userService.getUserById(id);
    }

    @GET
    @Path("/organization/{organizationId}")
    public Uni<List<UserDAO>> getUsersByOrganizationId(@PathParam("organizationId") UUID organizationId) {
        return userService.getUsersByOrganizationId(organizationId);
    }

    @POST
    @RolesAllowed("admin")
    public Uni<UserDAO> createUser(UserCreateRequest userCreateRequest) {
        return userService.createUser(userCreateRequest);
    }

    @PATCH
    @Path("/{id}")
    @RolesAllowed("admin")
    public Uni<UserDAO> updateUser(@PathParam("id") UUID id, UserPatchRequest userPatchRequest) {
        return userService.updateUser(id,userPatchRequest);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Uni<UserDAO> deleteOrganization(@PathParam("id") UUID id) {
        return userService.deleteUser(id);
    }
}

