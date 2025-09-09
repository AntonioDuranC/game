package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.OrderItems;

@ApplicationScoped
public class OrderItemsRepository implements PanacheRepositoryBase<OrderItems, Long> {}
