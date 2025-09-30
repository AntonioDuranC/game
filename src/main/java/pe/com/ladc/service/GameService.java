package pe.com.ladc.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.dto.GameRequestDTO;
import pe.com.ladc.dto.GameResponseDTO;
import pe.com.ladc.entity.Game;
import pe.com.ladc.enums.GameCategory;

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
    public GameResponseDTO createGame(GameRequestDTO request) {
        if (repository.existsByTitleAndCategory(request.getTitle(), request.getCategory())) {
            throw new InvalidOperationException("A game with the same title and category already exists");
        }

        Game game = GameMapper.toEntity(request);
        game.activate();
        repository.persist(game);

        return GameMapper.toResponse(game);
    }

    @Transactional
    @CacheInvalidate(cacheName = GAME_CACHE) // invalidar solo la key asociada al id
    public GameResponseDTO replaceGame(Long id, GameRequestDTO request) {
        Game game = repository.findByIdOptional(id)
                .orElseThrow(() -> new InvalidOperationException("Game with id " + id + " not found"));

        game.updateDescription(request.getDescription());
        game.changePrice(request.getPrice());
        game.setCategory(request.getCategory());
        game.setReleaseDate(request.getReleaseDate());

        repository.persist(game);

        return GameMapper.toResponse(game);
    }

    @Transactional
    @CacheInvalidate(cacheName = GAME_CACHE)
    public GameResponseDTO updateGame(Long id, GameRequestDTO request) {
        Game existingGame = repository.findByIdOptional(id)
                .orElseThrow(() -> new InvalidOperationException("Game does not exist with id " + id));

        if (request.getTitle() != null) {
            existingGame.setTitle(request.getTitle());
        }
        if (request.getCategory() != null) {
            existingGame.setCategory(parseCategory(request.getCategory().name()));
        }
        if (request.getDescription() != null) {
            existingGame.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            existingGame.setPrice(request.getPrice());
        }
        if (request.getReleaseDate() != null) {
            existingGame.setReleaseDate(request.getReleaseDate());
        }

        return GameMapper.toResponse(existingGame);
    }

    @Transactional
    @CacheInvalidate(cacheName = GAME_CACHE)
    public void deleteGame(@CacheKey long id) {
        Game game = repository.findByIdOptional(id)
                .orElseThrow(() -> new InvalidOperationException("Game with id " + id + " not found"));

        game.deactivate();
        repository.persist(game);
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
