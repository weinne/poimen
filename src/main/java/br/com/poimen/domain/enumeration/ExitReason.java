package br.com.poimen.domain.enumeration;

/**
 * The ExitReason enumeration.
 */
public enum ExitReason {
    TRANSFER("Transfer"),
    EXCOMMUNICATION("Excommunication"),
    DEATH("Death"),
    DISMISSAL("Dismissal"),
    ORDENATION("Ordenation"),
    OTHER("Other");

    private final String value;

    ExitReason(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
