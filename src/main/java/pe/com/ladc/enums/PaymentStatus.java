package pe.com.ladc.enums;

public enum PaymentStatus {

    /**
     * El pago ha sido iniciado y está pendiente de confirmación.
     */
    PENDING,

    /**
     * El pago se ha completado con éxito.
     */
    PAID,

    /**
     * El pago no se pudo completar debido a un error.
     */
    FAILED,

    /**
     * El proceso de pago ha sido cancelado.
     */
    CANCELLED,

    /**
     * Se ha emitido un reembolso para el pago.
     */
    REFUNDED
}
