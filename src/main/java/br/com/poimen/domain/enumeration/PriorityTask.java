package br.com.poimen.domain.enumeration;

/**
 * The PriorityTask enumeration.
 */
public enum PriorityTask {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String value;

    PriorityTask(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
