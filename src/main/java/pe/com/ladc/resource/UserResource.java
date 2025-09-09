package pe.com.ladc.resource;

import pe.com.ladc.util.ResponseModel;
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
public class UserResource {

    @Inject
    UserService userService;

    @POST
    public ResponseModel register(Users users){
        userService.createUser(users);
        return new ResponseModel("The user has been created",200);
    }

    @POST
    @Path("/login")
    public Response login(Users users){
        return userService.login(users.getUsername(), users.getPassword());
    }
}
