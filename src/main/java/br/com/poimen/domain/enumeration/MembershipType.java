package br.com.poimen.domain.enumeration;

/**
 * The MembershipType enumeration.
 */
public enum MembershipType {
    PROFESSION_OF_FAITH("Profession of Faith"),
    TRANSFER("Transfer"),
    BAPTISM("Baptism"),
    BAPTISM_AND_PROFESSION_OF_FAITH("Baptism and Profession of Faith"),
    JURISDICTION("Jurisdiction"),
    JURISDICTION_ON_REQUEST("Jurisdiction on Request"),
    PASTOR("Pastor");

    private final String value;

    MembershipType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
