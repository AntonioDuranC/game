package pe.com.ladc.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.entity.Order;
import pe.com.ladc.enums.OrderStatus;
import pe.com.ladc.exception.InvalidEnumException;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderService {

    private final OrderRepository repository;

    @Inject
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }


    public Optional<Order> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    public List<Order> findAll() {
        return repository.listAll();
    }


    @Transactional
    public Order createOrder(Order order) {
        parseStatus(order.getStatus().name());
        repository.persist(order);
        return order;
    }

    @Transactional
    public Order cancelOrder(Long id) {
        return repository.findByIdOptional(id).map(existing -> {
            existing.setStatus(OrderStatus.CANCELLED);
            return existing;
        }).orElseThrow(() -> new InvalidOperationException("Order with id " + id + " does not exist"));
    }

    @Transactional
    public Order updateStatus(Long id, String newStatus) {
        return repository.findByIdOptional(id).map(existing -> {
            existing.setStatus(parseStatus(newStatus));
            return existing;
        }).orElseThrow(() -> new InvalidOperationException("Order with id " + id + " does not exist"));
    }

    private OrderStatus parseStatus(String status) {
        try {
            return OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException(status);
        }
    }
}

