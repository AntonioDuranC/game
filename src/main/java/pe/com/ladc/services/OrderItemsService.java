package pe.com.ladc.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.entity.OrderItems;
import pe.com.ladc.entity.Orders;
import pe.com.ladc.enums.OrderStatus;
import pe.com.ladc.exceptions.InvalidOperationException;
import pe.com.ladc.repository.OrderItemsRepository;
import pe.com.ladc.repository.OrdersRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderItemsService {

    private final OrderItemsRepository orderItemsRepository;

    private final OrdersRepository ordersRepository;

    @Inject
    public OrderItemsService(OrderItemsRepository orderItemsRepository, OrdersRepository ordersRepository) {
        this.orderItemsRepository = orderItemsRepository;
        this.ordersRepository = ordersRepository;
    }


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
     * Partial update quantity when status is PENDING
     */
    @Transactional
    public OrderItems updateQuantity(Long orderId, Long itemId, Integer quantity) {
        if (quantity <= 0) {
            throw new InvalidOperationException("Quantity invalid");
        }

        Orders order = ordersRepository.findByIdOptional(orderId)
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
        Orders order = ordersRepository.findByIdOptional(orderId)
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


