package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import pe.com.ladc.entity.Game;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.enums.GameCategory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GameRepository implements PanacheRepositoryBase<Game, Long> {

    /**
     * Devuelve una lista paginada de juegos.
     *
     * @param page número de página (0-based)
     * @param size tamaño de página
     * @param sortBy campo por el que se ordena (default "category")
     * @param title filtro opcional por título (LIKE)
     */
    public List<Game> findPaginated(int page, int size, String sortBy, String title) {
        Sort sort = Sort.ascending(sortBy != null ? sortBy : "category");

        PanacheQuery<Game> query;
        if (title != null && !title.isBlank()) {
            query = find("title like ?1 and active = ?2", sort, "%" + title + "%", true);
        } else {
            query = find("active = ?1", sort, true);
        }

        return query.page(Page.of(page, size)).list();
    }

    /**
     * Cuenta el total de juegos, con o sin filtro por título.
     */
    public long countByTitle(String title) {
        if (title != null && !title.isBlank()) {
            return count("title like ?1", "%" + title + "%");
        }
        return count();
    }

    public Optional<Game> findByIdAndActive(long id) {
        return find("id = ?1 and active = true", id).firstResultOptional();
    }

    public Optional<Game> findByIdWithStock(Long gameId) {
        return find("FROM Game g LEFT JOIN FETCH g.stock WHERE g.id = ?1", gameId).firstResultOptional();
    }

    public boolean existsByTitleAndCategory(String title, GameCategory category) {
        long count = count("title = ?1 and category = ?2", title, category);
        return count > 0;
    }
}

