package pe.com.ladc.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.enums.GameCategory;
import pe.com.ladc.entity.Games;
import pe.com.ladc.exceptions.InvalidEnumException;
import pe.com.ladc.exceptions.InvalidOperationException;
import pe.com.ladc.repository.GamesRepository;

import java.util.List;

@ApplicationScoped
public class GameService {

    private final GamesRepository repository;

    @Inject
    public GameService(GamesRepository repository) {
        this.repository = repository;
    }


    // 🔹 Crear nuevo juego
    @Transactional
    public Games createGame(Games game) {
        game.setActive(true);
        repository.persist(game);
        return game;
    }

    // 🔹 Reemplazar juego completo
    @Transactional
    public Games replaceGame(Games game) {

        Games existingGame = repository.findByIdOptional(game.getId())
                .orElseThrow(() -> new InvalidOperationException("Game with id " + game.getId() + " does not exist"));

        existingGame.setTitle(game.getTitle());
        existingGame.setCategory(game.getCategory());
        existingGame.setDescription(game.getDescription());
        existingGame.setPrice(game.getPrice());
        existingGame.setStock(game.getStock());
        existingGame.setReleaseDate(game.getReleaseDate());

        repository.persist(existingGame);

        return existingGame;
    }

    // 🔹 Eliminar un juego
    @Transactional
    public void deleteGame(long id) {
        Games game = repository.findByIdOptional(id)
                .orElseThrow(() -> new InvalidOperationException("Game with id " + id + " does not exist"));
        game.setActive(false);
        repository.persist(game);
    }

    // 🔹 Actualizar parcialmente un juego
    @Transactional
    public Games updateGame(Games game) {
        Games existingGame = repository.findByIdOptional(game.getId())
                .orElseThrow(() -> new InvalidOperationException("Game with id " + game.getId() + " does not exist"));

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

        return existingGame;
    }

    // 🔹 Paginación con o sin filtro de nombre
    public List<Games> findPaginated(int page, int pageSize, String title) {
        if (page < 0 || pageSize <= 0) {
            throw new InvalidOperationException("Page and size must be greater than 0");
        }
        return repository.findPaginated(page, pageSize, "category", title);
    }

    // 🔹 Contador con o sin filtro de nombre
    public long count(String title) {
        return (title != null && !title.isBlank())
                ? repository.countByTitle(title)
                : repository.count();
    }

    // 🔹 Buscar por ID
    public Games findById(long id) {
        return repository.findById(id);
    }

    // 🔹 Utilidad: convierte String a Enum con validación
    private GameCategory parseCategory(String category) {
        try {
            return GameCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException(category);
        }
    }

}
