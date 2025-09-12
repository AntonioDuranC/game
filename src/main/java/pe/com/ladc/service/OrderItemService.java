package pe.com.ladc.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.entity.OrderItem;
import pe.com.ladc.entity.Order;
import pe.com.ladc.enums.OrderStatus;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.repository.OrderItemRepository;
import pe.com.ladc.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderItemService {

    private final OrderItemRepository orderItemsRepository;

    private final OrderRepository ordersRepository;

    @Inject
    public OrderItemService(OrderItemRepository orderItemsRepository, OrderRepository ordersRepository) {
        this.orderItemsRepository = orderItemsRepository;
        this.ordersRepository = ordersRepository;
    }


    /**
     * Add a new item to an order
     */
    @Transactional
    public OrderItem addItem(OrderItem item) {
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
    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemsRepository.list("order.id", orderId);
    }

    /**
     * Find a specific item by order and item id
     */
    public Optional<OrderItem> findById(Long orderId, Long itemId) {
        return orderItemsRepository.find("order.id = ?1 and id = ?2", orderId, itemId)
                .firstResultOptional();
    }

    /**
     * Partial update quantity when status is PENDING
     */
    @Transactional
    public OrderItem updateQuantity(Long orderId, Long itemId, Integer quantity) {
        if (quantity <= 0) {
            throw new InvalidOperationException("Quantity invalid");
        }

        Order order = ordersRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new InvalidOperationException("Order " + orderId + " not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOperationException("Cannot update items from order in status: " + order.getStatus());
        }

        return findById(orderId, itemId).map(existing -> {
            existing.setQuantity(quantity);
            return existing;
        }).orElseThrow(
                () -> new InvalidOperationException("Item " + itemId + " not found for order " + orderId));
    }

    /**
     * Delete an item
     */
    @Transactional
    public void deleteItem(Long orderId, Long itemId) {
        Order order = ordersRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new InvalidOperationException("Order " + orderId + " not found"));

        switch (order.getStatus()) {
            case ACCEPTED, PROCESSING, SHIPPED, DELIVERED ->
                throw new InvalidOperationException("Cannot delete items from order in status: " + order.getStatus());

            default ->
                findById(orderId, itemId).ifPresentOrElse(orderItemsRepository::delete,
                    () -> { throw new InvalidOperationException("Item " + itemId + " not found for order " + orderId); }
                );
        }


    }
}


