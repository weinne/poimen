package br.com.poimen.domain.enumeration;

/**
 * The StatusCounseling enumeration.
 */
public enum StatusCounseling {
    SCHEDULED("Scheduled"),
    IN_PROGRESS("In Progress"),
    DONE("Done"),
    CANCELED("Canceled");

    private final String value;

    StatusCounseling(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
