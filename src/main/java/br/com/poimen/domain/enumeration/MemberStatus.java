package br.com.poimen.domain.enumeration;

/**
 * The MemberStatus enumeration.
 */
public enum MemberStatus {
    COMUNGANT_MEMBER("Comungant"),
    NON_COMUNGANT_MEMBER("Non-Comungant"),
    VISITOR("Visitor"),
    INACTIVE("Inactive"),
    EXCOMMUNICATED("Excommunicated"),
    DECEASED("Deceased"),
    TRANSFERED("Transfered"),
    CATECHUMENS("Catechumens"),
    PASTOR("Pastor"),
    SEMINARIST("Seminarist");

    private final String value;

    MemberStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
