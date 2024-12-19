package br.com.poimen.domain.enumeration;

/**
 * The WorshipType enumeration.
 */
public enum WorshipType {
    SUNDAY_SERVICE("Sunday Service"),
    PRAYER_MEETING("Prayer Meeting"),
    BIBLE_STUDY("Bible Study"),
    SPECIAL_EVENT("Special Event"),
    OTHER("Other");

    private final String value;

    WorshipType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
