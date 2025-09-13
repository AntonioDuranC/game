package pe.com.ladc.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import pe.com.ladc.dto.OrderItemResponseDTO;
import pe.com.ladc.entity.OrderItem;
import pe.com.ladc.entity.Order;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.mapper.GameMapper;
import pe.com.ladc.repository.OrderItemRepository;
import pe.com.ladc.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Inject
    public OrderItemService(OrderItemRepository orderItemsRepository, OrderRepository ordersRepository) {
        this.orderItemRepository = orderItemsRepository;
        this.orderRepository = ordersRepository;
    }

    /**
     * Add a new item to an order
     */
    @Transactional
    public OrderItemResponseDTO addItem(Long orderId, OrderItem item) {
        item.setOrder(Order.builder()
                .id(orderId)
                .build());
        log.info("item: {}", item.toString());

        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        Order order = orderRepository.findByIdOptional(item.getOrder().getId())
                .orElseThrow(() -> new InvalidOperationException("Order not found " + item.getId()));
        item.setOrder(order);

        orderItemRepository.persist(item);
        return GameMapper.toResponse(item);
    }

    /**
     * List all items of an order
     */
    public List<OrderItemResponseDTO> findByOrderId(Long orderId) {
        return orderItemRepository.list("order.id", orderId).stream()
                .map(GameMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Find a specific item by order and item id
     */
    public Optional<OrderItemResponseDTO> findById(Long orderId, Long itemId) {
        return orderItemRepository.find("order.id = ?1 and id = ?2", orderId, itemId)
                .firstResultOptional()
                .map(GameMapper::toResponse);
    }

    /**
     * Partial update quantity when status is PENDING
     */
    @Transactional
    public OrderItemResponseDTO updateQuantity(Long orderId, Long itemId, Integer quantity) {
        if (quantity <= 0) {
            throw new InvalidOperationException("Quantity invalid");
        }

        Order order = orderRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new InvalidOperationException("Order " + orderId + " not found"));

        order.validStatusItem();

        OrderItem updated = orderItemRepository.find("order.id = ?1 and id = ?2", orderId, itemId)
                .firstResultOptional()
                .map(existing -> {
                    existing.setQuantity(quantity);
                    return existing;
                }).orElseThrow(
                        () -> new InvalidOperationException("Item " + itemId + " not found for order " + orderId));

        return GameMapper.toResponse(updated);
    }

    /**
     * Delete an item
     */
    @Transactional
    public void deleteItem(Long orderId, Long itemId) {
        Order order = orderRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new InvalidOperationException("Order " + orderId + " not found"));

        if (order.validDeleteItem()) {
            orderItemRepository.find("order.id = ?1 and id = ?2", orderId, itemId)
                    .firstResultOptional()
                    .ifPresentOrElse(orderItemRepository::delete,
                            () -> { throw new InvalidOperationException(
                                    "Item " + itemId + " not found for order " + orderId); }
                    );
        }
    }
}
