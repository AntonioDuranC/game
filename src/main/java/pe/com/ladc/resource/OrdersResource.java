package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.entity.Orders;
import pe.com.ladc.services.OrdersService;

import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Orders")
public class OrdersResource {

    @Inject
    OrdersService ordersService;

    @POST
    public Response create(Orders order) {
        return Response.status(Response.Status.CREATED).entity(ordersService.createOrder(order)).build();
    }

    @GET
    public List<Orders> listAll() {
        return ordersService.findAll();
    }

    @GET
    @Path("/{id}")
    public Orders get(@PathParam("id") Long id) {
        return ordersService.findById(id).orElseThrow(() -> new WebApplicationException("Order not found", 404));
    }

    @PUT
    @Path("/{id}")
    public Orders update(@PathParam("id") Long id, Orders order) {
        return ordersService.updateOrder(id, order);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        ordersService.deleteOrder(id);
    }
}
