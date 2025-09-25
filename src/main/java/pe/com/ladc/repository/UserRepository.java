package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.Users;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {

    public Users findByUsername(String username) {
        return find("username", username).firstResult();
    }
}

