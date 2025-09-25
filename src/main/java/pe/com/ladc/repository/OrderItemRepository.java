package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.OrderItem;

import java.util.Optional;

@ApplicationScoped
public class OrderItemRepository implements PanacheRepositoryBase<OrderItem, Long> {

    public Optional<OrderItem> findByOrderIdAndItemId(Long orderId, Long itemId) {
        return find("order.id = ?1 and id = ?2", orderId, itemId)
                .firstResultOptional();
    }

    public Optional<OrderItem> findByOrderIdAndGameId(Long orderId, Long gameId) {
        return find("order.id = ?1 and game.id = ?2", orderId, gameId)
                .firstResultOptional();
    }

}
