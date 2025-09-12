package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.entity.Order;
import pe.com.ladc.service.OrderService;

import java.util.List;
import java.util.Optional;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Orders")
public class OrderResource {

    private final OrderService service;

    @Inject
    public OrderResource(OrderService orderItemsService) {
        this.service = orderItemsService;
    }


    @POST
    public Response create(Order order) {
        Order createdOrder = service.createOrder(order);
        return Response.ok(new ResponseDTO<>("Order created", 200, createdOrder)).build();
    }

    @PATCH
    @Path("/{id}/status/{status}")
    public Response update(@PathParam("id") Long id, @PathParam("status") String status) {
        Order updatedOrder = service.updateStatus(id, status);
        return Response.ok(new ResponseDTO<>("Orders updated", 200, updatedOrder)).build();
    }

    @PATCH
    @Path("/{id}/cancel")
    public Response cancel(@PathParam("id") Long id) {
        Order updatedOrder = service.cancelOrder(id);
        return Response.ok(new ResponseDTO<>("Orders updated", 200, updatedOrder)).build();
    }

    @GET
    public Response listAll() {
        List<Order> orderList = service.findAll();
        return Response.ok(new ResponseDTO<>("Orders retrieved", 200, orderList)).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Optional<Order> order = service.findById(id);
        return Response.ok(new ResponseDTO<>("Orders retrieved", 200, order)).build();
    }

}
