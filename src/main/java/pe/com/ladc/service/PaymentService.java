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
import pe.com.ladc.mapper.GameMapper;
import pe.com.ladc.repository.OrderRepository;
import pe.com.ladc.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;

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
            throw new InvalidOperationException("Order not found with id " + request.getOrderId());
        }

        // Validar que no exista otro Payment asociado a este Order
        boolean exists = paymentRepository.find("order.id", request.getOrderId())
                .firstResultOptional()
                .isPresent();
        if (exists) {
            throw new InvalidOperationException("This order already has an associated payment");
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .build();

        paymentRepository.persist(payment);

        return GameMapper.toResponse(payment);
    }


    /**
     * Find all payments
     */
    public List<PaymentResponseDTO> findAll() {
        return paymentRepository.listAll()
                .stream()
                .map(GameMapper::toResponse)
                .toList();
    }

    /**
     * Find payment by id
     */
    public PaymentResponseDTO findById(Long id) {
        return paymentRepository.findByIdOptional(id)
                .map(GameMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    /**
     * Update payment status
     */
    @Transactional
    public PaymentResponseDTO updateStatus(Long id, String newStatus) {
        return paymentRepository.findByIdOptional(id).map(existing -> {
            existing.validStatus(parseStatus(newStatus));
            return GameMapper.toResponse(existing);
        }).orElseThrow(() -> new InvalidOperationException("Order no found with id " + id));
    }



    private PaymentStatus parseStatus(String status) {
        try {
            return PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException(status);
        }
    }
}
