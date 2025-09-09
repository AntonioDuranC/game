package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.Orders;

@ApplicationScoped
public class OrdersRepository implements PanacheRepositoryBase<Orders, Long> {}

