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
import pe.com.ladc.dto.OrderRequestDTO;
import pe.com.ladc.dto.OrderResponseDTO;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.service.OrderService;

import java.util.List;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Orders")
public class OrderResource {

    private final OrderService service;

    @Inject
    public OrderResource(OrderService service) {
        this.service = service;
    }

    @POST
    @Operation(summary = "Create a new order")
    @APIResponse(responseCode = "201", description = "Order created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class)))
    public Response createOrder(OrderRequestDTO request) {
        OrderResponseDTO created = service.createOrder(request);
        return Response.status(Response.Status.CREATED)
                .entity(new ResponseDTO<>("Order created successfully", 201, created))
                .build();
    }

    @PATCH
    @Path("/{id}/cancel")
    @Operation(summary = "Cancel an order")
    @APIResponse(responseCode = "200", description = "Order cancelled successfully")
    public Response cancelOrder(@PathParam("id") Long id) {
        OrderResponseDTO cancelled = service.cancelOrder(id);
        return Response.ok(new ResponseDTO<>("Order cancelled successfully", 200, cancelled)).build();
    }

    @PATCH
    @Path("/{id}/status/{status}")
    @Operation(summary = "Update order status")
    @APIResponse(responseCode = "200", description = "Order status updated successfully")
    public Response updateOrderStatus(@PathParam("id") Long id, @PathParam("status") String status) {
        OrderResponseDTO updated = service.updateStatus(id, status);
        return Response.ok(new ResponseDTO<>("Order status updated successfully", 200, updated)).build();
    }

    @GET
    @Operation(summary = "List all orders")
    @APIResponse(responseCode = "200", description = "Orders retrieved successfully")
    public Response listOrders() {
        List<OrderResponseDTO> orders = service.findAll();
        return Response.ok(new ResponseDTO<>("Orders retrieved successfully", 200, orders)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get order by ID")
    @APIResponse(responseCode = "200", description = "Order retrieved successfully")
    public Response getOrder(@PathParam("id") Long id) {
        return service.findById(id)
                .map(order -> Response.ok(new ResponseDTO<>("Order retrieved successfully", 200, order)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ResponseDTO<>("Order not found", 404)).build());
    }
}
