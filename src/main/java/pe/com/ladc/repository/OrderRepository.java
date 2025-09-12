package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.Order;

@ApplicationScoped
public class OrderRepository implements PanacheRepositoryBase<Order, Long> {}

