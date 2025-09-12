package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.Stock;

@ApplicationScoped
public class StockRepository implements PanacheRepository<Stock> {

    public Stock findByGameId(Long gameId) {
        return find("game.id", gameId).firstResult();
    }
}
