package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.Payments;

@ApplicationScoped
public class PaymentsRepository implements PanacheRepositoryBase<Payments, Long> {}
