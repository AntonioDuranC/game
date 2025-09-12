package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.OrderItem;

@ApplicationScoped
public class OrderItemRepository implements PanacheRepositoryBase<OrderItem, Long> {}
