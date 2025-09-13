package pe.com.ladc.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.dto.OrderRequestDTO;
import pe.com.ladc.dto.OrderResponseDTO;
import pe.com.ladc.entity.Order;
import pe.com.ladc.enums.OrderStatus;
import pe.com.ladc.exception.InvalidEnumException;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.mapper.GameMapper;
import pe.com.ladc.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {

    private final OrderRepository repository;

    @Inject
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Optional<OrderResponseDTO> findById(Long id) {
        return repository.findByIdOptional(id).map(GameMapper::toResponse);
    }

    public List<OrderResponseDTO> findAll() {
        return repository.listAll()
                .stream()
                .map(GameMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        Order order = Order.builder()
                .userId(dto.getUserId())
                .total(dto.getTotal())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        repository.persist(order);
        return GameMapper.toResponse(order);
    }

    @Transactional
    public OrderResponseDTO cancelOrder(Long id) {
        return repository.findByIdOptional(id)
                .map(existing -> {
                    existing.cancel(); // ✅ lógica en entity
                    return GameMapper.toResponse(existing);
                })
                .orElseThrow(() -> new InvalidOperationException("Order does not exist with id " + id));
    }

    @Transactional
    public OrderResponseDTO updateStatus(Long id, String newStatus) {
        return repository.findByIdOptional(id)
                .map(existing -> {
                    existing.updateStatus(parseStatus(newStatus)); // ✅ lógica en entity
                    return GameMapper.toResponse(existing);
                })
                .orElseThrow(() -> new InvalidOperationException("Order does not exist with id " + id));
    }

    private OrderStatus parseStatus(String status) {
        try {
            return OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException(status);
        }
    }
}
