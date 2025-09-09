package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import pe.com.ladc.entity.Games;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GamesRepository implements PanacheRepositoryBase<Games, Long> {

    /**
     * Devuelve una lista paginada de juegos.
     *
     * @param page número de página (0-based)
     * @param size tamaño de página
     * @param sortBy campo por el que se ordena (default "category")
     * @param title filtro opcional por título (LIKE)
     */
    public List<Games> findPaginated(int page, int size, String sortBy, String title) {
        Sort sort = Sort.ascending(sortBy != null ? sortBy : "category");

        PanacheQuery<Games> query;
        if (title != null && !title.isBlank()) {
            query = find("title like ?1", sort, "%" + title + "%");
        } else {
            query = findAll(sort);
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
}

