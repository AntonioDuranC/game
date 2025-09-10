package pe.com.ladc.enums;

public enum PaymentMethod {

    /**
     * Pago realizado con una tarjeta de crédito.
     */
    CREDIT_CARD,

    /**
     * Pago procesado a través de la plataforma de PayPal.
     */
    PAYPAL,

    /**
     * Pago realizado con una tarjeta de débito.
     */
    DEBIT_CARD,

    /**
     * Transferencia de fondos directa entre cuentas bancarias.
     */
    BANK_TRANSFER,

    /**
     * Pago realizado utilizando una criptomoneda, como Bitcoin o Ethereum.
     */
    CRYPTO,

    /**
     * Pago en persona, generalmente en efectivo, al momento de la entrega.
     */
    CASH_ON_DELIVERY,

    /**
     * Un método de pago digital específico de una región o país, como un monedero móvil.
     */
    DIGITAL_WALLET,

    /**
     * Pago a plazos o mediante financiamiento, con un proveedor externo.
     */
    FINANCING
}
