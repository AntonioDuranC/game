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
    public OrderItemResource(OrderItemService orderItemsService)    {
        this.service = orderItemsService;
    }


    @POST
    @Operation(summary = "Add item to order")
    public Response addItem(@PathParam("orderId") Long orderId, OrderItemRequestDTO item) {
        OrderItemResponseDTO orderItem = service.addItem(orderId, item);
        return Response.ok(new ResponseDTO<>("Item created",200, orderItem)).build();
    }

    @PATCH
    @Path("/{itemId}/quantity/{quantity}")
    @Operation(summary = "Partially update an item in the order")
    public Response patchItem(@PathParam("orderId") Long orderId,
                              @PathParam("itemId") Long itemId,
                              @PathParam("quantity") Integer quantity) {
        OrderItemResponseDTO orderItems =  service.updateQuantity(orderId, itemId, quantity);
        return Response.ok(new ResponseDTO<>("Item updated", 200, orderItems)).build();
    }

    @DELETE
    @Path("/{itemId}")
    @Operation(summary = "Delete an item from the order")
    public Response deleteItem(@PathParam("orderId") Long orderId,
                               @PathParam("itemId") Long itemId) {
        service.deleteItem(orderId, itemId);
        return Response.ok(new ResponseDTO<>("Item deleted", 204)).build();
    }

    @GET
    @Operation(summary = "List all items of an order")
    public Response listItems(@PathParam("orderId") Long orderId) {
        List<OrderItemResponseDTO> orderItemsList = service.findByOrderId(orderId);
        return Response.ok(new ResponseDTO<>("Items retrieved", 200, orderItemsList)).build();
    }

    @GET
    @Path("/{itemId}")
    @Operation(summary = "Get specific item by ID")
    public Response getItem(@PathParam("orderId") Long orderId,
                            @PathParam("itemId") Long itemId) {
        Optional<OrderItemResponseDTO> orderItems = service.findById(orderId, itemId);
        return Response.ok(new ResponseDTO<>("Item retrieved", 200, orderItems)).build();
    }

}


