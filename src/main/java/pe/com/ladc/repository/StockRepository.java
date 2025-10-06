package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.Stock;

import java.util.Optional;

@ApplicationScoped
public class StockRepository implements PanacheRepositoryBase<Stock, Long> {

    public Optional<Stock> findByGameId(Long gameId) {
        return find("game.id", gameId).firstResultOptional();
    }
}
