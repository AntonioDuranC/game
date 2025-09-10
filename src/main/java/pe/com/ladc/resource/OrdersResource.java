package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.entity.Orders;
import pe.com.ladc.services.OrdersService;

import java.util.List;
import java.util.Optional;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Orders")
public class OrdersResource {

    private final OrdersService service;

    @Inject
    public OrdersResource(OrdersService orderItemsService) {
        this.service = orderItemsService;
    }


    @POST
    public Response create(Orders order) {
        Orders createdOrder = service.createOrder(order);
        return Response.ok(new ResponseDTO<>("Order created", 200, createdOrder)).build();
    }

    @PATCH
    @Path("/{id}/status/{status}")
    public Response update(@PathParam("id") Long id, @PathParam("status") String status) {
        Orders updatedOrder = service.updateStatus(id, status);
        return Response.ok(new ResponseDTO<>("Orders updated", 200, updatedOrder)).build();
    }

    @PATCH
    @Path("/{id}/cancel")
    public Response cancel(@PathParam("id") Long id) {
        Orders updatedOrder = service.cancelOrder(id);
        return Response.ok(new ResponseDTO<>("Orders updated", 200, updatedOrder)).build();
    }

    @GET
    public Response listAll() {
        List<Orders> orderList = service.findAll();
        return Response.ok(new ResponseDTO<>("Orders retrieved", 200, orderList)).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Optional<Orders> order = service.findById(id);
        return Response.ok(new ResponseDTO<>("Orders retrieved", 200, order)).build();
    }

}
