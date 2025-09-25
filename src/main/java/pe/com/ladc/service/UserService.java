package pe.com.ladc.service;

import jakarta.inject.Inject;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.entity.Users;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import pe.com.ladc.repository.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class UserService {

    private final UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(Users user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.persist();
    }

    @Transactional
    public Response login(String username, String password) {
        Users users = userRepository.findByUsername(username);

        if (users == null || !BCrypt.checkpw(password, users.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ResponseDTO<>("Invalid username or password", 401))
                    .build();
        }

        // Generar JWT
        String jwt = Jwt.issuer("http://example.com/issuer")
                .subject(users.getUsername())
                .groups(users.getRoles())
                .expiresAt(Instant.now().plus(12, ChronoUnit.HOURS))
                .sign();

        // Generar cabecera Set-Cookie manualmente para poder incluir HttpOnly, Secure y SameSite
        String cookieValue = "JWT=" + jwt +
                "; Path=/" +
                "; Max-Age=43200" +       // 12 horas
                "; HttpOnly" +
                "; Secure" +
                "; SameSite=Strict";

        return Response.ok(new ResponseDTO<>("SUCCESS", 200))
                .header("Set-Cookie", cookieValue)
                .build();
    }
}
