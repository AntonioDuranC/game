package pe.com.ladc.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import pe.com.ladc.enums.GameCategory;
import pe.com.ladc.entity.Games;
import pe.com.ladc.exceptions.InvalidEnumException;
import pe.com.ladc.exceptions.GameDontExistException;
import pe.com.ladc.repository.GamesRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class GameService {

    @Inject
    GamesRepository gamesRepository;

    // 🔹 Paginación con o sin filtro de nombre
    public List<Games> findPaginated(int page, int pageSize, String title) {
        return gamesRepository.findPaginated(page, pageSize, "category", title);
    }

    // 🔹 Contador con o sin filtro de nombre
    public long count(String title) {
        return (title != null && !title.isBlank())
                ? gamesRepository.countByTitle(title)
                : gamesRepository.count();
    }

    // 🔹 Buscar por ID
    public Optional<Games> findById(long id) {
        return gamesRepository.findByIdOptional(id);
    }

    // 🔹 Crear nuevo juego
    @Transactional
    public Games createGame(Games game) {
        gamesRepository.persist(game);
        return game;
    }

    // 🔹 Eliminar un juego
    @Transactional
    public void deleteGame(long id) {
        gamesRepository.findByIdOptional(id).ifPresentOrElse(
                gamesRepository::delete,
                () -> {
                    throw new GameDontExistException("Game with id " + id + " does not exist");
                }
        );
    }


    // 🔹 Actualizar parcialmente un juego
    @Transactional
    public Games updateGame(Games game) {
        return gamesRepository.findByIdOptional(game.getId()).map(existing -> {

            // Actualizamos solo los campos necesarios sobre la misma entidad
            existing.setTitle(game.getTitle() != null ? game.getTitle() : existing.getTitle());
            existing.setGameCategory(game.getGameCategory() != null ? game.getGameCategory() : existing.getGameCategory());
            existing.setDescription(game.getDescription() != null ? game.getDescription() : existing.getDescription());
            existing.setPrice(game.getPrice() != null ? game.getPrice() : existing.getPrice());
            existing.setStock(game.getStock() != null ? game.getStock() : existing.getStock());
            existing.setReleaseDate(game.getReleaseDate() != null ? game.getReleaseDate() : existing.getReleaseDate());

            // Como es una entidad gestionada por Hibernate, no necesitas persistir otra vez
            return existing;
        }).orElseThrow(() ->
                new GameDontExistException("Game with id " + game.getId() + " does not exist"));
    }


    // 🔹 Reemplazar juego completo
    @Transactional
    public Games replaceGame(long id, String title, String category) {
        return gamesRepository.findByIdOptional(id).map(game -> {
            if (title != null && !title.isBlank()) {
                game.setTitle(title);
            }
            if (category != null && !category.isBlank()) {
                game.setGameCategory(parseCategory(category));
            }
            return game; // cambios ya quedan en contexto persistente
        }).orElseThrow(() ->
                new GameDontExistException("Game with id " + id + " does not exist"));
    }

    // 🔹 Utilidad: convierte String a Enum con validación
    private GameCategory parseCategory(String category) {
        try {
            return GameCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid category: {}", category);
            throw new InvalidEnumException(category);
        }
    }
}
