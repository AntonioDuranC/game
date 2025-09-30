package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.dto.OrderItemRequestDTO;
import pe.com.ladc.dto.OrderItemResponseDTO;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.service.OrderItemService;

import java.util.List;
import java.util.Optional;

@Path("/orders/{orderId}/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Order Items")
public class OrderItemResource {

    private final OrderItemService service;

    @Inject
    public OrderItemResource(OrderItemService orderItemsService) {
        this.service = orderItemsService;
    }

    @POST
    @Operation(summary = "Add item to order")
    public Response addItem(@PathParam("orderId") Long orderId, OrderItemRequestDTO item) {
        OrderItemResponseDTO orderItem = service.addItem(orderId, item);
        return Response.status(Response.Status.CREATED)
                .entity(new ResponseDTO<>("Item created", 201, orderItem))
                .build();
    }

    @PATCH
    @Path("/{itemId}/quantity/{quantity}")
    @Operation(summary = "Update quantity of an item (only if order is PENDING)")
    public Response updateQuantity(@PathParam("orderId") Long orderId,
                                  @PathParam("itemId") Long itemId,
                                  @PathParam("quantity") Integer quantity) {
        OrderItemResponseDTO updated = service.updateQuantity(orderId, itemId, quantity);
        return Response.ok(new ResponseDTO<>("Item quantity updated", 200, updated)).build();
    }

    @PATCH
    @Path("/{itemId}/game/{newGameId}")
    @Operation(summary = "Change the game of an existing item (business rules applied)")
    public Response changeGame(@PathParam("orderId") Long orderId,
                               @PathParam("itemId") Long itemId,
                               @PathParam("newGameId") Long newGameId) {
        OrderItemResponseDTO updated = service.changeGame(orderId, itemId, newGameId);
        return Response.ok(new ResponseDTO<>("Game changed successfully", 200, updated)).build();
    }

    @DELETE
    @Path("/{itemId}")
    @Operation(summary = "Delete an item from the order if allowed by business rules")
    public Response deleteItem(@PathParam("orderId") Long orderId,
                               @PathParam("itemId") Long itemId) {
        service.deleteItem(orderId, itemId);
        return Response.noContent()
                .entity(new ResponseDTO<>("Item deleted", 204))
                .build();
    }

    @GET
    @Operation(summary = "List all items of an order")
    public Response listItems(@PathParam("orderId") Long orderId) {
        List<OrderItemResponseDTO> items = service.findByOrderId(orderId);
        return Response.ok(new ResponseDTO<>("Items retrieved", 200, items)).build();
    }

    @GET
    @Path("/{itemId}")
    @Operation(summary = "Get specific item by ID")
    public Response getItem(@PathParam("orderId") Long orderId,
                            @PathParam("itemId") Long itemId) {
        Optional<OrderItemResponseDTO> item = service.findById(orderId, itemId);
        return item.map(i -> Response.ok(new ResponseDTO<>("Item retrieved", 200, i)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ResponseDTO<>("Item not found", 404))
                        .build());
    }
}
