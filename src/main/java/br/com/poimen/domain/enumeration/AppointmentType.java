package br.com.poimen.domain.enumeration;

/**
 * The AppointmentType enumeration.
 */
public enum AppointmentType {
    SERVICE("Worship Service"),
    MEETING("Meeting"),
    EVENT("Event"),
    REHEARSAL("Rehearsal"),
    VISIT("Visit"),
    COUNSELING("Counseling"),
    DISCIPLESHIP("Discipleship"),
    SMALL_GROUP("Small Group"),
    OTHER("Other");

    private final String value;

    AppointmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
