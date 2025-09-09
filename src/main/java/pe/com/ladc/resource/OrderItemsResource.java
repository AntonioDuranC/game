package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.entity.OrderItems;
import pe.com.ladc.entity.Orders;
import pe.com.ladc.services.OrderItemsService;

import java.util.List;

@Path("/orders/{orderId}/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Order Items")
public class OrderItemsResource {

    @Inject
    OrderItemsService orderItemsService;

    @POST
    @Operation(summary = "Add item to order")
    public Response addItem(@PathParam("orderId") Long orderId, OrderItems item) {
        item.setOrder(Orders.builder().id(orderId).build());
        return Response.status(Response.Status.CREATED)
                .entity(orderItemsService.addItem(item))
                .build();
    }

    @GET
    @Operation(summary = "List all items of an order")
    public List<OrderItems> listItems(@PathParam("orderId") Long orderId) {
        return orderItemsService.findByOrderId(orderId);
    }

    @GET
    @Path("/{itemId}")
    @Operation(summary = "Get specific item by ID")
    public Response getItem(@PathParam("orderId") Long orderId,
                            @PathParam("itemId") Long itemId) {
        return orderItemsService.findById(orderId, itemId)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @PUT
    @Path("/{itemId}")
    @Operation(summary = "Update an item in the order")
    public Response updateItem(@PathParam("orderId") Long orderId,
                               @PathParam("itemId") Long itemId,
                               OrderItems item) {
        item.setOrder(Orders.builder().id(orderId).build());
        return Response.ok(orderItemsService.updateItem(orderId, itemId, item)).build();
    }

    @PATCH
    @Path("/{itemId}")
    @Operation(summary = "Partially update an item in the order")
    public Response patchItem(@PathParam("orderId") Long orderId,
                              @PathParam("itemId") Long itemId,
                              OrderItems item) {
        return Response.ok(orderItemsService.patchItem(orderId, itemId, item)).build();
    }

    @DELETE
    @Path("/{itemId}")
    @Operation(summary = "Delete an item from the order")
    public Response deleteItem(@PathParam("orderId") Long orderId,
                               @PathParam("itemId") Long itemId) {
        orderItemsService.deleteItem(orderId, itemId);
        return Response.noContent().build();
    }
}


