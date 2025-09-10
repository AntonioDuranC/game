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
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.entity.Payments;
import pe.com.ladc.services.PaymentsService;

import java.util.List;

@Path("/payments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Payments")
public class PaymentsResource {

    private final PaymentsService service;

    @Inject
    public PaymentsResource(PaymentsService paymentService) {
        this.service = paymentService;
    }

    @POST
    @Operation(summary = "Create a new payment")
    @APIResponse(responseCode = "201", description = "Payment created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payments.class)))
    public Response createPayment(Payments payment) {
        Payments createdPayment = service.create(payment);
        return Response.status(Response.Status.CREATED).entity(createdPayment).build();
    }

    @PATCH
    @Path("/{id}/status/{status}")
    @Operation(summary = "Update payment status")
    public Response updatePaymentStatus(@PathParam("id") Long id, @PathParam("status") String status) {
        Payments updatedPayment = service.updateStatus(id, status);
        return Response.ok(updatedPayment).build();
    }

    @GET
    @Operation(summary = "List all payments")
    public Response listPayments() {
        List<Payments> paymentsList = service.findAll();
        return Response.ok(new ResponseDTO<>("Payments retrieved", 200, paymentsList)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a payment by ID")
    public Response getPayment(@PathParam("id") Long id) {
        Payments payment = service.findById(id);
        return Response.ok(new ResponseDTO<>("Payment retrieved", 204, payment)).build();
    }

}

