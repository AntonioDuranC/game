package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import pe.com.ladc.dto.GameResponseDTO;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.entity.Game;
import pe.com.ladc.service.GameService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;


@Path("/games")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Games")
public class GameResource {

    private final GameService service;

    @Inject
    public GameResource(GameService gameService) {
        this.service = gameService;
    }

    @POST
    @RolesAllowed("admin")
    public Response create(Game game){
        return Response.ok(
                new ResponseDTO<>("Game created", 200, service.createGame(game))
        ).build();
    }

    @PUT
    @RolesAllowed("admin")
    public Response replace(Game game){
        return Response.ok(
                new ResponseDTO<>("Game updated",200, service.replaceGame(game))
        ).build();
    }

    @PATCH
    @RolesAllowed("admin")
    public Response update(Game game) {
        return Response.ok(
                new ResponseDTO<>("Game updated", 200, service.updateGame(game))
        ).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") int id) {
        service.deleteGame(id);
        return Response.ok(new ResponseDTO<>("Game deleted",204)).build();
    }

    @GET
    public Response pagedlist(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("title") String title) {

        List<GameResponseDTO> pagedGames = service.findPaginated(page, size, title);
        long totalGames = service.count(title);

        return Response.ok(new ResponseDTO<>("Games retrieved", 200, pagedGames))
                .header("X-Total-Count", totalGames)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id){
        return Response.ok(
                new ResponseDTO<>("Game retrieved", 200, service.findById(id))
        ).build();
    }


}

