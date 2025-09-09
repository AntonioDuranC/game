package pe.com.ladc.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.entity.Orders;
import pe.com.ladc.entity.Payments;
import pe.com.ladc.enums.PaymentMethod;
import pe.com.ladc.enums.PaymentStatus;
import pe.com.ladc.exceptions.InvalidEnumException;
import pe.com.ladc.repository.OrdersRepository;
import pe.com.ladc.repository.PaymentsRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class PaymentsService {

    @Inject
    PaymentsRepository paymentsRepository;

    @Inject
    OrdersRepository ordersRepository;

    @Transactional
    public Payments create(Payments payment) {

        Orders order = ordersRepository.findById(payment.getOrder().getId());
        if (order == null) {
            throw new InvalidEnumException("Order with id " + payment.getOrder().getId() + " not found");
        }

        if (payment.getMethod() == null) {
            payment.setMethod(PaymentMethod.CREDIT_CARD);
        }
        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.PENDING);
        }

        payment.setOrder(order);
        payment.setPaymentDate(LocalDateTime.now());

        paymentsRepository.persist(payment);

        return payment;
    }


    @Transactional
    public Payments update(Payments payment) {
        Payments paymentUpdated = paymentsRepository.findByIdOptional(payment.getId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getMethod() != null && !payment.getMethod().name().isBlank()) {
            try {
                paymentUpdated.setMethod(PaymentMethod.valueOf(payment.getMethod().name().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumException("Invalid payment method: " + payment.getMethod());
            }
        }

        if (payment.getStatus() != null && !payment.getStatus().name().isBlank()) {
            try {
                paymentUpdated.setStatus(PaymentStatus.valueOf(payment.getStatus().name().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumException("Invalid payment status: " + payment.getStatus());
            }
        }

        return paymentUpdated;
    }


    public List<Payments> findAll() {
        return paymentsRepository.listAll();
    }

    public Payments findById(Long id) {
        return paymentsRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    @Transactional
    public Payments replace(Long id, Payments newPayment) {
        Payments existing = paymentsRepository.findByIdOptional(id)
                .orElseThrow(() -> new InvalidEnumException("Payment with id " + id + " not found"));

        existing.setAmount(newPayment.getAmount());
        existing.setPaymentDate(newPayment.getPaymentDate());
        existing.setMethod(newPayment.getMethod() != null ? newPayment.getMethod() : existing.getMethod());
        existing.setStatus(newPayment.getStatus() != null ? newPayment.getStatus() : existing.getStatus());

        paymentsRepository.persist(existing);
        return existing;
    }

    @Transactional
    public Payments updateStatus(Long id, String newStatus) {

        Payments existing = paymentsRepository.findByIdOptional(id)
                .orElseThrow(() -> new InvalidEnumException("Payment with id " + id + " not found"));

        try {
            PaymentStatus statusEnum = PaymentStatus.valueOf(newStatus.toUpperCase());
            existing.setStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException(
                    "Invalid payment status: " + newStatus +
                            ". Allowed values are: " + Arrays.toString(PaymentStatus.values())
            );
        }

        paymentsRepository.persist(existing);
        return existing;
    }


    @Transactional
    public void delete(Long id) {
        boolean deleted = paymentsRepository.deleteById(id);
        if (!deleted) {
            throw new InvalidEnumException("Payment with id " + id + " not found");
        }
    }
}

