package pe.com.ladc.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.entity.OrderItems;
import pe.com.ladc.exceptions.GameDontExistException;
import pe.com.ladc.repository.OrderItemsRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderItemsService {

    @Inject
    OrderItemsRepository orderItemsRepository;

    /**
     * Add a new item to an order
     */
    @Transactional
    public OrderItems addItem(OrderItems item) {
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        orderItemsRepository.persist(item);
        return item;
    }

    /**
     * List all items of an order
     */
    public List<OrderItems> findByOrderId(Long orderId) {
        return orderItemsRepository.list("order.id", orderId);
    }

    /**
     * Find a specific item by order and item id
     */
    public Optional<OrderItems> findById(Long orderId, Long itemId) {
        return orderItemsRepository.find("order.id = ?1 and id = ?2", orderId, itemId)
                .firstResultOptional();
    }

    /**
     * Replace (full update) an item
     */
    @Transactional
    public OrderItems updateItem(Long orderId, Long itemId, OrderItems item) {
        return findById(orderId, itemId).map(existing -> {
            existing.setGameId(item.getGameId());
            existing.setQuantity(item.getQuantity());
            existing.setPrice(item.getPrice());
            return existing;
        }).orElseThrow(() ->
                new GameDontExistException("Item " + itemId + " not found for order " + orderId));
    }

    /**
     * Partial update (patch) of an item
     */
    @Transactional
    public OrderItems patchItem(Long orderId, Long itemId, OrderItems item) {
        return findById(orderId, itemId).map(existing -> {
            if (item.getGameId() != null) existing.setGameId(item.getGameId());
            if (item.getQuantity() != null) existing.setQuantity(item.getQuantity());
            if (item.getPrice() != null) existing.setPrice(item.getPrice());
            return existing;
        }).orElseThrow(() ->
                new GameDontExistException("Item " + itemId + " not found for order " + orderId));
    }

    /**
     * Delete an item
     */
    @Transactional
    public void deleteItem(Long orderId, Long itemId) {
        findById(orderId, itemId).ifPresentOrElse(orderItemsRepository::delete,
                () -> { throw new GameDontExistException("Item " + itemId + " not found for order " + orderId); });
    }
}


