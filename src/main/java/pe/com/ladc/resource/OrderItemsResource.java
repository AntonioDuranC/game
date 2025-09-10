package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.entity.OrderItems;
import pe.com.ladc.entity.Orders;
import pe.com.ladc.services.OrderItemsService;

import java.util.List;
import java.util.Optional;

@Path("/orders/{orderId}/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Order Items")
public class OrderItemsResource {

    private final OrderItemsService service;

    @Inject
    public OrderItemsResource(OrderItemsService orderItemsService)    {
        this.service = orderItemsService;
    }

    @POST
    @Operation(summary = "Add item to order")
    public Response addItem(@PathParam("orderId") Long orderId, OrderItems item) {
        item.setOrder(Orders.builder().id(orderId).build());
        OrderItems orderItems = service.addItem(item);
        return Response.ok(new ResponseDTO<>("Item created",200, orderItems)).build();
    }

    @PATCH
    @Path("/{itemId}/quantity/{quantity}")
    @Operation(summary = "Partially update an item in the order")
    public Response patchItem(@PathParam("orderId") Long orderId,
                              @PathParam("itemId") Long itemId,
                              @PathParam("quantity") Integer quantity) {
        OrderItems orderItems =  service.updateQuantity(orderId, itemId, quantity);
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
        List<OrderItems> orderItemsList = service.findByOrderId(orderId);
        return Response.ok(new ResponseDTO<>("Items retrieved", 200, orderItemsList)).build();
    }

    @GET
    @Path("/{itemId}")
    @Operation(summary = "Get specific item by ID")
    public Response getItem(@PathParam("orderId") Long orderId,
                            @PathParam("itemId") Long itemId) {
        Optional<OrderItems> orderItems = service.findById(orderId, itemId);
        return Response.ok(new ResponseDTO<>("Item retrieved", 200, orderItems)).build();
    }

}


