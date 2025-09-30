package pe.com.ladc.mapper;

import pe.com.ladc.dto.*;
import pe.com.ladc.entity.Game;
import pe.com.ladc.entity.Order;
import pe.com.ladc.entity.OrderItem;
import pe.com.ladc.entity.Payment;

public class GameMapper {

    private GameMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Game toEntity(GameRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Game.builder()
                .title(dto.getTitle())
                .category(dto.getCategory()) // ya es GameCategory
                .description(dto.getDescription())
                .price(dto.getPrice())
                .releaseDate(dto.getReleaseDate())
                .active(true) // por defecto los juegos creados están activos
                .build();
    }

    public static GameResponseDTO toResponse(Game game) {
        if (game == null) return null;

        return GameResponseDTO.builder()
                .id(game.getId())
                .title(game.getTitle())
                .category(game.getCategory())
                .description(game.getDescription())
                .price(game.getPrice())
                .releaseDate(game.getReleaseDate())
                .active(game.getActive())
                .availableStock(
                        game.getStock() != null ? game.getStock().getAvailableStock() : 0
                )
                .build();
    }

    public static OrderResponseDTO toResponse(Order order) {
        if (order == null) return null;

        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .total(order.getTotal())
                .status(order.getStatus())
                .build();
    }

    public static PaymentResponseDTO toResponse(Payment payment) {
        if (payment == null) return null;

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
        if (item == null) return null;

        return OrderItemResponseDTO.builder()
                .id(item.getId())
                .orderId(item.getOrder().getId())
                .gameId(item.getGame().getId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

}
