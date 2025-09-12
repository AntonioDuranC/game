package pe.com.ladc.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.entity.Game;
import pe.com.ladc.entity.Stock;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.repository.GameRepository;
import pe.com.ladc.repository.StockRepository;


@ApplicationScoped
public class StockService {

    private final GameRepository gameRepository;

    private final StockRepository stockRepository;

    @Inject
    public StockService(GameRepository gameRepository, StockRepository stockRepository) {
        this.gameRepository = gameRepository;
        this.stockRepository = stockRepository;
    }


    @Transactional
    public Stock addStock(Long gameId, int quantity) {
        Game game = gameRepository.find("id", gameId).firstResult();
        if (game == null) {
            throw new InvalidOperationException("Game not found for id: " + gameId);
        }

        Stock stock = stockRepository.find("game.id", gameId).firstResult();

        if (stock == null) {
            stock = new Stock();
            stock.setGame(game);
            stock.setTotalStock(0);
            stock.setReservedStock(0);
        }

        stock.addStock(quantity);
        stockRepository.persist(stock);
        return stock;
    }

    @Transactional
    public Stock reserveStock(Long gameId, int quantity) {
        Stock stock = validAndGetStock(gameId);
        stock.reserveStock(quantity);
        stockRepository.persist(stock);
        return stock;
    }

    @Transactional
    public Stock releaseStock(Long gameId, int quantity) {
        Stock stock = validAndGetStock(gameId);
        stock.releaseStock(quantity);
        stockRepository.persist(stock);
        return stock;
    }

    @Transactional
    public Stock confirmStock(Long gameId, int quantity) {
        Stock stock = validAndGetStock(gameId);
        stock.confirmStock(quantity);
        stockRepository.persist(stock);
        return stock;
    }

    private void validGame(Long gameId) {
        Game game = gameRepository.find("id", gameId).firstResult();
        if (game == null) {
            throw new InvalidOperationException("Game not found for id: " + gameId);
        }
    }

    private Stock validAndGetStock(Long gameId) {
        validGame(gameId);
        Stock stock = stockRepository.find("game.id", gameId).firstResult();
        if (stock == null) {
            throw new InvalidOperationException("Stock record not found for game id: " + gameId);
        }
        return stock;
    }

}
