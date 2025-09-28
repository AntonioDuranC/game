package pe.com.ladc.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.dto.GameResponseDTO;
import pe.com.ladc.enums.GameCategory;
import pe.com.ladc.entity.Game;
import pe.com.ladc.exception.InvalidEnumException;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.mapper.GameMapper;
import pe.com.ladc.repository.GameRepository;
import pe.com.ladc.util.GameMessages;

import java.util.List;

@ApplicationScoped
public class GameService {

    private static final String GAME_CACHE = "game-cache";

    private final GameRepository repository;

    @Inject
    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = GAME_CACHE) // invalidar toda cache cuando se crea un nuevo juego
    public GameResponseDTO createGame(Game game) {
        boolean exists = repository.find("title = ?1 and category = ?2", game.getTitle(), game.getCategory())
                .firstResultOptional()
                .isPresent();

        if (exists) {
            throw new InvalidOperationException(
                    String.format(GameMessages.GAME_ALREADY_EXISTS, game.getTitle(), game.getCategory())
            );
        }

        game.setActive(true);
        repository.persist(game);

        return GameMapper.toResponse(game);
    }

    @Transactional
    @CacheInvalidate(cacheName = GAME_CACHE) // invalidar solo la key asociada al id
    public GameResponseDTO replaceGame(@CacheKey Game game) {
        Game existingGame = repository.findByIdOptional(game.getId())
                .orElseThrow(() -> new InvalidOperationException(
                        String.format(GameMessages.GAME_DOES_NOT_EXIST, game.getId())
                ));

        existingGame.setTitle(game.getTitle());
        existingGame.setCategory(game.getCategory());
        existingGame.setDescription(game.getDescription());
        existingGame.setPrice(game.getPrice());
        existingGame.setStock(game.getStock());
        existingGame.setReleaseDate(game.getReleaseDate());

        repository.persist(existingGame);

        return GameMapper.toResponse(existingGame);
    }

    @Transactional
    @CacheInvalidate(cacheName = GAME_CACHE)
    public void deleteGame(@CacheKey long id) {
        Game game = repository.findByIdOptional(id)
                .orElseThrow(() -> new InvalidOperationException("Game does not exist with id " + id));
        game.setActive(false);
        repository.persist(game);
    }

    @Transactional
    @CacheInvalidate(cacheName = GAME_CACHE)
    public GameResponseDTO updateGame(@CacheKey Game game) {
        Game existingGame = repository.findByIdOptional(game.getId())
                .orElseThrow(() -> new InvalidOperationException("Game does not exist with id " + game.getId()));

        if (game.getTitle() != null) {
            existingGame.setTitle(game.getTitle());
        }
        if (game.getCategory() != null) {
            existingGame.setCategory(parseCategory(game.getCategory().name()));
        }
        if (game.getDescription() != null) {
            existingGame.setDescription(game.getDescription());
        }
        if (game.getPrice() != null) {
            existingGame.setPrice(game.getPrice());
        }
        if (game.getStock() != null) {
            existingGame.setStock(game.getStock());
        }
        if (game.getReleaseDate() != null) {
            existingGame.setReleaseDate(game.getReleaseDate());
        }

        return GameMapper.toResponse(existingGame);
    }

    public List<GameResponseDTO> findPaginated(int page, int pageSize, String title) {
        if (page < 0 || pageSize <= 0) {
            throw new InvalidOperationException("Page and size must be greater than 0");
        }
        return repository.findPaginated(page, pageSize, "category", title)
                .stream()
                .map(GameMapper::toResponse)
                .toList();
    }

    public long count(String title) {
        return (title != null && !title.isBlank())
                ? repository.countByTitle(title)
                : repository.count();
    }

    @CacheResult(cacheName = GAME_CACHE)
    public GameResponseDTO findByIdAndActive(@CacheKey long id) {
        System.out.println(">>> Ejecutando consulta en DB para id=" + id);
        Game game = repository.findByIdAndActive(id)
                .orElseThrow(() -> new InvalidOperationException("Game not found with id " + id));
        return GameMapper.toResponse(game);
    }

    private GameCategory parseCategory(String category) {
        try {
            return GameCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException(
                    String.format(GameMessages.INVALID_CATEGORY, category)
            );
        }
    }
}
