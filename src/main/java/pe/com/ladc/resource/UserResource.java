package pe.com.ladc.resource;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.entity.Users;
import pe.com.ladc.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Users")
public class UserResource {

    private final UserService service;

    @Inject
    public UserResource(UserService userService){
        this.service = userService;
    }

    @POST
    public Response register(Users users){
        service.createUser(users);
        return Response.ok(new ResponseDTO<>("User created",200)).build();
    }

    @POST
    @Path("/login")
    public Response login(Users users){
        return service.login(users.getUsername(), users.getPassword());
    }
}
