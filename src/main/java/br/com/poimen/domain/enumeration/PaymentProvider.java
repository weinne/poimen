package br.com.poimen.domain.enumeration;

/**
 * The PaymentProvider enumeration.
 */
public enum PaymentProvider {
    STRIPE("Stripe"),
    PAYPAL("PayPal"),
    OTHER("Other");

    private final String value;

    PaymentProvider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
