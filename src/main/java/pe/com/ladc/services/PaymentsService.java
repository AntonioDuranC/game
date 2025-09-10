package pe.com.ladc.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.entity.Orders;
import pe.com.ladc.entity.Payments;
import pe.com.ladc.enums.PaymentStatus;
import pe.com.ladc.exceptions.InvalidEnumException;
import pe.com.ladc.exceptions.InvalidOperationException;
import pe.com.ladc.repository.OrdersRepository;
import pe.com.ladc.repository.PaymentsRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;

    private final OrdersRepository ordersRepository;

    @Inject
    public PaymentsService(PaymentsRepository paymentsRepository, OrdersRepository ordersRepository) {
        this.paymentsRepository = paymentsRepository;
        this.ordersRepository = ordersRepository;
    }


    @Transactional
    public Payments create(Payments payment) {

        Orders order = ordersRepository.findById(payment.getOrder().getId());
        if (order == null) {
            throw new InvalidEnumException("Order with id " + payment.getOrder().getId() + " not found");
        }

        payment.setOrder(order);
        payment.setPaymentDate(LocalDateTime.now());

        paymentsRepository.persist(payment);

        return payment;
    }

    public List<Payments> findAll() {
        return paymentsRepository.listAll();
    }

    public Payments findById(Long id) {
        return paymentsRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    @Transactional
    public Payments updateStatus(Long id, String newStatus) {
        return paymentsRepository.findByIdOptional(id).map(existing -> {
            existing.setStatus(parseStatus(newStatus));
            return existing;
        }).orElseThrow(() -> new InvalidOperationException("Order with id " + id + " does not exist"));
    }

    private PaymentStatus parseStatus(String status) {
        try {
            return PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException(status);
        }
    }

}

