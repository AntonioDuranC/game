package pe.com.ladc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.Payment;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<Payment, Long> {}
