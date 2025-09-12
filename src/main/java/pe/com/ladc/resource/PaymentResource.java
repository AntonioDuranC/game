package pe.com.ladc.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.dto.PaymentRequestDTO;
import pe.com.ladc.dto.PaymentResponseDTO;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.service.PaymentService;

import java.util.List;

@Path("/payments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Payments")
public class PaymentResource {

    private final PaymentService service;

    @Inject
    public PaymentResource(PaymentService paymentService) {
        this.service = paymentService;
    }

    @POST
    @Operation(summary = "Create a new payment")
    @APIResponse(
            responseCode = "201",
            description = "Payment created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))
    )
    public Response createPayment(PaymentRequestDTO request) {
        PaymentResponseDTO createdPayment = service.create(request);
        return Response.status(Response.Status.CREATED)
                .entity(new ResponseDTO<>("Payment created successfully", 201, createdPayment))
                .build();
    }

    @PATCH
    @Path("/{id}/status/{status}")
    @Operation(summary = "Update payment status")
    @APIResponse(
            responseCode = "200",
            description = "Payment status updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))
    )
    public Response updatePaymentStatus(@PathParam("id") Long id, @PathParam("status") String status) {
        PaymentResponseDTO updatedPayment = service.updateStatus(id, status);
        return Response.ok(new ResponseDTO<>("Payment status updated successfully", 200, updatedPayment)).build();
    }

    @GET
    @Operation(summary = "List all payments")
    @APIResponse(
            responseCode = "200",
            description = "Payments retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))
    )
    public Response listPayments() {
        List<PaymentResponseDTO> paymentsList = service.findAll();
        return Response.ok(new ResponseDTO<>("Payments retrieved", 200, paymentsList)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a payment by ID")
    @APIResponse(
            responseCode = "200",
            description = "Payment retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))
    )
    public Response getPayment(@PathParam("id") Long id) {
        PaymentResponseDTO payment = service.findById(id);
        return Response.ok(new ResponseDTO<>("Payment retrieved", 200, payment)).build();
    }
}
