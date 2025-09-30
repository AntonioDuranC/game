package pe.com.ladc.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import pe.com.ladc.dto.OrderItemRequestDTO;
import pe.com.ladc.dto.OrderItemResponseDTO;
import pe.com.ladc.entity.Game;
import pe.com.ladc.entity.OrderItem;
import pe.com.ladc.entity.Order;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.mapper.GameMapper;
import pe.com.ladc.repository.GameRepository;
import pe.com.ladc.repository.OrderItemRepository;
import pe.com.ladc.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class OrderItemService {

    private final GameRepository gameRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Inject
    public OrderItemService(GameRepository gameRepository, OrderItemRepository orderItemsRepository,
                            OrderRepository ordersRepository) {
        this.gameRepository = gameRepository;
        this.orderItemRepository = orderItemsRepository;
        this.orderRepository = ordersRepository;
    }

    /**
     * Add a new item to an order
     */
    @Transactional
    public OrderItemResponseDTO addItem(Long orderId, OrderItemRequestDTO dto) {
        Objects.requireNonNull(dto.getGameId(), "GameId is required");
        Objects.requireNonNull(orderId, "OrderId is required");

        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        Game game = gameRepository.findByIdAndActive(dto.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Game does not exist"));

        Order order = orderRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new InvalidOperationException("Order not found " + orderId));

        orderItemRepository.findByOrderIdAndGameId(orderId, dto.getGameId())
                .ifPresent(item -> {
                    throw new InvalidOperationException("This game is already in the order");
                });

        OrderItem item = OrderItem.builder()
                .game(game)
                .order(order)
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .build();

        // Validaciones de negocio desde la entidad
        item.validatePrice();
        item.updateQuantity(dto.getQuantity());

        log.info("item: {}", item);

        orderItemRepository.persist(item);

        return GameMapper.toResponse(item);
    }

    /**
     * List all items of an order
     */
    public List<OrderItemResponseDTO> findByOrderId(Long orderId) {
        return orderItemRepository.list("order.id", orderId)
                .stream()
                .map(GameMapper::toResponse)
                .toList();
    }

    /**
     * Find a specific item by order and item id
     */
    public Optional<OrderItemResponseDTO> findById(Long orderId, Long itemId) {
        return orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .map(GameMapper::toResponse);
    }

    /**
     * Update quantity when status is PENDING
     */
    @Transactional
    public OrderItemResponseDTO updateQuantity(Long orderId, Long itemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOperationException("Quantity invalid");
        }

        // Validar existencia de la orden
        Order order = orderRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new InvalidOperationException("Order " + orderId + " not found"));

        // Validar estado de la orden (ej. que siga en PENDING)
        order.validStatusItem();

        // Recuperar el item
        OrderItem item = orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .orElseThrow(() -> new InvalidOperationException("Item " + itemId + " not found for order " + orderId));

        // Recuperar el juego con su stock
        Game game = gameRepository.findByIdWithStock(item.getGame().getId())
                .orElseThrow(() -> new InvalidOperationException("Game not found: " + item.getGame().getId()));

        if (game.getStock() == null) {
            throw new InvalidOperationException("Stock not found for game: " + game.getId());
        }

        // Validar stock disponible
        int available = game.getStock().getAvailableStock();
        if (quantity > available) {
            throw new InvalidOperationException(
                    "Requested quantity " + quantity + " exceeds available stock " + available
            );
        }

        // Actualizar cantidad (delegado en la entidad si quieres mantener lógica encapsulada)
        item.updateQuantity(quantity);

        return GameMapper.toResponse(item);
    }


    /**
     * Change game of an item (solo permitido si la orden lo permite)
     */
    @Transactional
    public OrderItemResponseDTO changeGame(Long orderId, Long itemId, Long newGameId) {
        Game newGame = gameRepository.findByIdAndActive(newGameId)
                .orElseThrow(() -> new IllegalArgumentException("Game does not exist"));

        OrderItem item = orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .orElseThrow(() -> new InvalidOperationException("Item " + itemId + " not found for order " + orderId));

        item.changeGame(newGame);

        return GameMapper.toResponse(item);
    }

    /**
     * Delete an item
     */
    @Transactional
    public void deleteItem(Long orderId, Long itemId) {
        OrderItem item = orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .orElseThrow(() -> new InvalidOperationException("Item " + itemId + " not found for order " + orderId));

        if (item.canBeDeleted()) {
            orderItemRepository.delete(item);
        } else {
            throw new InvalidOperationException("Item " + itemId + " cannot be deleted in current order status");
        }
    }
}
