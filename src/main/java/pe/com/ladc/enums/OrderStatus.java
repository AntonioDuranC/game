package pe.com.ladc.enums;

public enum OrderStatus {

    /**
     * La orden ha sido creada pero aún no se ha enviado para su procesamiento.
     */
    DRAFT,

    /**
     * La orden ha sido enviada y está a la espera de confirmación.
     */
    PENDING,

    /**
     * La orden ha sido revisada y es aceptada para su cumplimiento.
     */
    ACCEPTED,

    /**
     * Los artículos de la orden están siendo preparados para su envío.
     */
    PROCESSING,

    /**
     * La orden ha sido enviada al cliente.
     */
    SHIPPED,

    /**
     * La orden ha sido entregada exitosamente al cliente.
     */
    DELIVERED,

    /**
     * La orden ha sido cancelada antes de su finalización.
     */
    CANCELLED,

    /**
     * La orden ha sido devuelta por el cliente.
     */
    RETURNED,

    /**
     * El pago de la orden ha fallado o ha sido rechazado.
     */
    PAYMENT_FAILED
}
