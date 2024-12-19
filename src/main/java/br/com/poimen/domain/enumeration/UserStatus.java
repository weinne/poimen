package br.com.poimen.domain.enumeration;

/**
 * The UserStatus enumeration.
 */
public enum UserStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
