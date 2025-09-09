package pe.com.ladc.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.entity.Orders;
import pe.com.ladc.repository.OrdersRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrdersService {

    @Inject
    OrdersRepository ordersRepository;

    @Transactional
    public Orders createOrder(Orders order) {
        ordersRepository.persist(order);
        return order;
    }

    public Optional<Orders> findById(Long id) {
        return ordersRepository.findByIdOptional(id);
    }

    public List<Orders> findAll() {
        return ordersRepository.listAll();
    }

    @Transactional
    public Orders updateOrder(Long id, Orders order) {
        return ordersRepository.findByIdOptional(id).map(existing -> {
            existing.setStatus(order.getStatus());
            existing.setTotal(order.getTotal());
            return existing;
        }).orElseThrow();
    }

    @Transactional
    public void deleteOrder(Long id) {
        ordersRepository.deleteById(id);
    }
}

