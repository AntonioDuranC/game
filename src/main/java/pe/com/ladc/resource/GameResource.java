package pe.com.ladc.resource;

import pe.com.ladc.entity.Games;
import pe.com.ladc.entity.ResponseModel;
import pe.com.ladc.services.GameService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
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
                schema = @Schema(implementation = ResponseModel.class)
        )
)
@Tag(name = "Game controller")
public class GameResource {

    @Inject
    private GameService gameService;

    @GET
    @Operation(
            summary = "Get all games",
            description = "Retrieves a paginated list of games. The results can be filtered by game name and sorted by the game category specified in the cookies."
    )
    @APIResponse(
            responseCode = "200",
            description = "Return games list.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Games.class,type = SchemaType.ARRAY)
            )
    )
    @RolesAllowed("user")
    public Response getGameList(
            @HeaderParam("page") int page,
            @HeaderParam("size") int size,
            @QueryParam("name") String name,
            @CookieParam("gameCategory") String gameCategory
    ) {
        List<Games> pagedGames = gameService.findPaginated(page,size,gameCategory,name);
        long totalGames = gameService.count(name);
        int start = (page - 1) * size;

        if (start >= totalGames) {
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
            description = "Retrieves specified game by id."
    )
    @APIResponse(
            responseCode = "200",
            description = "Return game.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Games.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModel.class)
            )
    )
    @RolesAllowed("CoffeEnjoer")
    public Response getGame(@PathParam("id") int id){
        return gameService.findById(id)
                .map(v -> Response.ok(v)
                        .cookie(new NewCookie("gameCategory", v.getCategory(), "/", null, "Games Category", 3600, false))
                        .build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Operation(
            summary = "Create game",
            description = "Create new game."
    )
    @RolesAllowed("admin")
    public Response createGame(Games game){
        gameService.createGame(game);
        return Response.ok(new ResponseModel("Game created",200)).build();
    }

    @PATCH
    @Operation(
            summary = "Update game",
            description = "Update specified game fields."
    )
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModel.class)
            )
    )
    public Response updateGame(Games game){
        gameService.updateGame(game.getId(),game.getName(),game.getCategory());
        return Response.ok(new ResponseModel("Game updated",200)).build();
    }

    @PUT
    @Operation(
            summary = "Replace game",
            description = "Replace game."
    )
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModel.class)
            )
    )
    public Response replaceGame(Games game) {
        gameService.replaceGame(game);
        return Response.ok(new ResponseModel("Game updated",200)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Delete game",
            description = "delete game by id."
    )
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModel.class)
            )
    )
    public Response deleteGame(@PathParam("id") int id) {
        gameService.deleteGame(id);
        return Response.ok(new ResponseModel("Game deleted",200)).build();
    }
}

