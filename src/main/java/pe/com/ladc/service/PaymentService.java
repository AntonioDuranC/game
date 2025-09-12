package pe.com.ladc.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.com.ladc.dto.PaymentRequestDTO;
import pe.com.ladc.dto.PaymentResponseDTO;
import pe.com.ladc.entity.Order;
import pe.com.ladc.entity.Payment;
import pe.com.ladc.enums.PaymentStatus;
import pe.com.ladc.exception.InvalidEnumException;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.repository.OrderRepository;
import pe.com.ladc.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository ordersRepository;

    @Inject
    public PaymentService(PaymentRepository paymentsRepository, OrderRepository ordersRepository) {
        this.paymentRepository = paymentsRepository;
        this.ordersRepository = ordersRepository;
    }

    /**
     * Create a new payment
     */
    @Transactional
    public PaymentResponseDTO create(PaymentRequestDTO request) {
        Order order = ordersRepository.findById(request.getOrderId());
        if (order == null) {
            throw new InvalidOperationException("Order no found with id " + request.getOrderId());
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(PaymentStatus.PENDING) // default when created
                .paymentDate(LocalDateTime.now())
                .build();

        paymentRepository.persist(payment);

        return toResponseDTO(payment);
    }

    /**
     * Find all payments
     */
    public List<PaymentResponseDTO> findAll() {
        return paymentRepository.listAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find payment by id
     */
    public PaymentResponseDTO findById(Long id) {
        return paymentRepository.findByIdOptional(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    /**
     * Update payment status
     */
    @Transactional
    public PaymentResponseDTO updateStatus(Long id, String newStatus) {
        return paymentRepository.findByIdOptional(id).map(existing -> {
            existing.validStatus(parseStatus(newStatus));
            return toResponseDTO(existing);
        }).orElseThrow(() -> new InvalidOperationException("Order no found with id " + id));
    }


    private PaymentResponseDTO toResponseDTO(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .build();
    }

    private PaymentStatus parseStatus(String status) {
        try {
            return PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException(status);
        }
    }
}
