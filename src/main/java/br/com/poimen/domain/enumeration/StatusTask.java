package br.com.poimen.domain.enumeration;

/**
 * The StatusTask enumeration.
 */
public enum StatusTask {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String value;

    StatusTask(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
