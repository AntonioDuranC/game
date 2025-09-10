package pe.com.ladc.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.entity.Orders;
import pe.com.ladc.enums.OrderStatus;
import pe.com.ladc.exceptions.InvalidEnumException;
import pe.com.ladc.exceptions.InvalidOperationException;
import pe.com.ladc.repository.OrdersRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrdersService {

    private final OrdersRepository repository;

    @Inject
    public OrdersService(OrdersRepository repository) {
        this.repository = repository;
    }


    public Optional<Orders> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    public List<Orders> findAll() {
        return repository.listAll();
    }


    @Transactional
    public Orders createOrder(Orders order) {
        parseStatus(order.getStatus().name());
        repository.persist(order);
        return order;
    }

    @Transactional
    public Orders cancelOrder(Long id) {
        return repository.findByIdOptional(id).map(existing -> {
            existing.setStatus(OrderStatus.CANCELLED);
            return existing;
        }).orElseThrow(() -> new InvalidOperationException("Order with id " + id + " does not exist"));
    }

    @Transactional
    public Orders updateStatus(Long id, String newStatus) {
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

