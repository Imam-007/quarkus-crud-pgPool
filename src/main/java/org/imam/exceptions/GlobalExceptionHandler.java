package org.imam.exceptions;

import io.vertx.pgclient.PgException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        Map<String, Object> errorResponse = new HashMap<>();

        if (exception instanceof OrganizationNotFoundException || exception instanceof UserNotFoundException) {
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }

        if (exception instanceof BadRequestException) {
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        if (exception instanceof PgException) {
            errorResponse.put("error", "Database Error");
            errorResponse.put("message", exception.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }

        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
    }
}
