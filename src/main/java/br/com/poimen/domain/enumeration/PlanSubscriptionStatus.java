package br.com.poimen.domain.enumeration;

/**
 * The PlanSubscriptionStatus enumeration.
 */
public enum PlanSubscriptionStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    CANCELED("Canceled"),
    PAUSED("Paused");

    private final String value;

    PlanSubscriptionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
