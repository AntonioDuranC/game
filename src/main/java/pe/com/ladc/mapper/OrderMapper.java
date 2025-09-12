package pe.com.ladc.mapper;

import pe.com.ladc.dto.OrderItemResponseDTO;
import pe.com.ladc.dto.OrderResponseDTO;
import pe.com.ladc.dto.PaymentResponseDTO;
import pe.com.ladc.entity.Order;
import pe.com.ladc.entity.OrderItem;
import pe.com.ladc.entity.Payment;

public class OrderMapper {

    public static OrderResponseDTO toResponse(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .total(order.getTotal())
                .status(order.getStatus())
                .build();
    }

    public static PaymentResponseDTO toResponse(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .build();
    }

    public static OrderItemResponseDTO toResponse(OrderItem item) {
        return OrderItemResponseDTO.builder()
                .id(item.getId())
                .gameId(item.getGameId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}
