package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import pe.com.ladc.util.ResponseModel;
import pe.com.ladc.entity.Games;
import pe.com.ladc.services.GameService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;


@Path("/games")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@APIResponse(
        responseCode = "200",
        description = "Return operation data.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseModel.class)))
@Tag(name = "Game controller")
public class GameResource {

    @Inject
    private GameService gameService;


    @POST
    @Operation(
            summary = "Create game",
            description = "Create new game.")
    @RolesAllowed("admin")
    public Response createGame(Games game){
        gameService.createGame(game);
        return Response.ok(new ResponseModel("Game created",200)).build();
    }


    @PUT
    @Operation(
            summary = "Replace game",
            description = "Replace game.")
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModel.class)))
    @RolesAllowed("admin")
    public Response replaceGame(Games game){
        gameService.replaceGame(game.getId(),game.getTitle(),game.getCategory().name());
        return Response.ok(new ResponseModel("Game updated",200)).build();
    }


    @PATCH
    @Operation(
            summary = " game",
            description = "Update specified game fields.")
    @APIResponse(
            responseCode = "200",
            description = "Game updated successfully.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModel.class)))
    @RolesAllowed("admin")
    public Response updateGame(Games game) {
        gameService.updateGame(game);
        return Response.ok(new ResponseModel("Game updated", 200)).build();
    }


    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Delete game",
            description = "delete game by id.")
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModel.class)))
    @RolesAllowed("admin")
    public Response deleteGame(@PathParam("id") int id) {
        gameService.deleteGame(id);
        return Response.ok(new ResponseModel("Game deleted",200)).build();
    }


    @GET
    @Operation(
            summary = "Get all games",
            description = "Retrieves a paginated list of games. The results can be filtered by title."
    )
    @APIResponse(
            responseCode = "200",
            description = "Paginated list of games",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Games.class, type = SchemaType.ARRAY)
            )
    )
    public Response getGameList(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("title") String title
    ) {
        if (page < 0 || size <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Page and size must be greater than 0")
                    .build();
        }

        List<Games> pagedGames = gameService.findPaginated(page, size, title);
        long totalGames = gameService.count(title);
        int start = (page) * size;

        if (start >= totalGames && totalGames > 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(pagedGames)
                .header("X-Total-Count", totalGames)
                .build();
    }


    @GET
    @Path("/{id}")
    @Operation(
            summary = "Get game by id",
            description = "Retrieves specified game by id." )
    @APIResponse(
            responseCode = "200",
            description = "Return game.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Games.class)))
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModel.class)))
    public Response getGame(@PathParam("id") int id){
        return Response.ok(gameService.findById(id))
                .build();
    }

}

