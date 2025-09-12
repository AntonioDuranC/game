package pe.com.ladc.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.entity.Stock;
import pe.com.ladc.service.StockService;


@Path("/stock")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StockResource {

    private final StockService service;

    @Inject
    public StockResource(StockService service) {
        this.service = service;
    }

    @PATCH
    @Path("/{gameId}/add")
    @RolesAllowed("admin")
    @Operation(summary = "Add stock", description = "Increase total stock for a game")
    @APIResponse(
            responseCode = "200",
            description = "Stock added successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
    )
    public Response addStock(@PathParam("gameId") Long gameId, @QueryParam("quantity") int quantity) {
        Stock updatedStock = service.addStock(gameId, quantity);
        return Response.ok(new ResponseDTO<>("Stock added successfully", 200, updatedStock)).build();
    }

    @PATCH
    @Path("/{gameId}/reserve")
    @RolesAllowed("admin")
    @Operation(summary = "Reserve stock", description = "Reserve stock for a pending order")
    @APIResponse(
            responseCode = "200",
            description = "Stock reserved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
    )
    public Response reserveStock(@PathParam("gameId") Long gameId, @QueryParam("quantity") int quantity) {
        Stock updatedStock = service.reserveStock(gameId, quantity);
        return Response.ok(new ResponseDTO<>("Stock reserved successfully", 200, updatedStock)).build();
    }

    @PATCH
    @Path("/{gameId}/release")
    @RolesAllowed("admin")
    @Operation(summary = "Release stock", description = "Release reserved stock when order is cancelled")
    @APIResponse(
            responseCode = "200",
            description = "Stock released successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
    )
    public Response releaseStock(@PathParam("gameId") Long gameId, @QueryParam("quantity") int quantity) {
        Stock updatedStock = service.releaseStock(gameId, quantity);
        return Response.ok(new ResponseDTO<>("Stock released successfully", 200, updatedStock)).build();
    }

    @PATCH
    @Path("/{gameId}/confirm")
    @RolesAllowed("admin")
    @Operation(summary = "Confirm stock", description = "Confirm reserved stock when order is completed")
    @APIResponse(
            responseCode = "200",
            description = "Stock confirmed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
    )
    public Response confirmStock(@PathParam("gameId") Long gameId, @QueryParam("quantity") int quantity) {
        Stock updatedStock = service.confirmStock(gameId, quantity);
        return Response.ok(new ResponseDTO<>("Stock confirmed successfully", 200, updatedStock)).build();
    }
}
