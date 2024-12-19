package br.com.poimen.domain.enumeration;

/**
 * The GroupType enumeration.
 */
public enum GroupType {
    DEPARTMENT("Department"),
    INTERNAL_SOCIETY("Internal Society"),
    DEACON_BOARD("Deacon Board"),
    CHURCH_COUNCIL("Church Council");

    private final String value;

    GroupType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
