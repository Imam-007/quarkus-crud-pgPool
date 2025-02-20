package org.imam.resources;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.imam.services.OrganizationServiceDemo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Path("/organizations")
public class OrganizationResourceDemo {

    @Inject
    OrganizationServiceDemo organizationServiceDemo;

    @GET
    @Path("/filter")
    public Uni<Response> getOrganizationsWithUsers(@QueryParam("startDate") String startDate,
                                                   @QueryParam("endDate") String endDate) {
        try {
            if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
                return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                        .entity("Both startDate and endDate are required in format dd-MM-yyyy.")
                        .build());
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
            LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

            if (startLocalDate.isAfter(endLocalDate)) {
                return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                        .entity("startDate cannot be after endDate.")
                        .build());
            }

            LocalDateTime start = startLocalDate.atStartOfDay();
            LocalDateTime end = endLocalDate.atTime(23, 59, 59);

            return organizationServiceDemo.getOrganizationsWithUsers(start, end)
                    .onItem().transform(list -> Response.ok(list).build())
                    .onFailure().recoverWithItem(err -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(err.getMessage()).build());
        } catch (DateTimeParseException e) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Please use dd-MM-yyyy.")
                    .build());
        }
    }
}
