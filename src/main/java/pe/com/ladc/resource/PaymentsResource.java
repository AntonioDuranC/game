package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.entity.Payments;
import pe.com.ladc.services.PaymentsService;

import java.util.List;

@Path("/payments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Payments")
public class PaymentsResource {

    @Inject
    PaymentsService paymentService;

    @GET
    @Operation(summary = "List all payments")
    public List<Payments> listPayments() {
        return paymentService.findAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a payment by ID")
    public Response getPayment(@PathParam("id") Long id) {
        return Response.ok(paymentService.findById(id)).build();
    }

    @POST
    @Operation(summary = "Create a new payment")
    @APIResponse(responseCode = "201", description = "Payment created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payments.class)))
    public Response createPayment(Payments payment) {
        Payments created = paymentService.create(payment);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Replace payment information")
    public Response replacePayment(@PathParam("id") Long id, Payments payment) {
        Payments updated = paymentService.replace(id, payment);
        return Response.ok(updated).build();
    }

    @PATCH
    @Path("/{id}/status/{status}")
    @Operation(summary = "Update payment status")
    public Response updatePaymentStatus(@PathParam("id") Long id, @PathParam("status") String status) {
        Payments updated = paymentService.updateStatus(id, status);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a payment")
    public Response deletePayment(@PathParam("id") Long id) {
        paymentService.delete(id);
        return Response.noContent().build();
    }
}

