package pe.com.ladc.services;

import pe.com.ladc.util.ResponseModel;
import pe.com.ladc.entity.Users;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class UserService {

    @Transactional
    public void createUser(Users users) {
        users.setPassword(BCrypt.hashpw(users.getPassword(), BCrypt.gensalt()));
        users.setId(0);
        Users.persist(users);
    }

    public Response login(String username, String password) {
        Users users = Users.find("username", username).firstResult();

        if (users == null || !BCrypt.checkpw(password, users.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    new ResponseModel("Invalid username or password",401)).build();
        }
        String jwt = Jwt.claims()
                .subject(users.getUsername())
                .groups(users.getRoles())
                .expiresAt(System.currentTimeMillis() / 1000 + 3600)
                .sign();

        return  Response.status(Response.Status.OK).cookie(
                new NewCookie("JWT",
                        jwt,
                        "/",
                        null,
                        "JWT Token",
                        3600,
                        false,
                        false))
                .entity(new ResponseModel("SUCCESS",200)).build();
    }
}
