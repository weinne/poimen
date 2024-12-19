package br.com.poimen.domain;

import br.com.poimen.domain.enumeration.AppointmentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Appointment.
 */
@Entity
@Table(name = "appointment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appointment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "subject", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String subject;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Lob
    @Column(name = "notes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String notes;

    @Column(name = "local")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String local;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AppointmentType appointmentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "users",
            "members",
            "subscriptions",
            "counselingSessions",
            "tasks",
            "transactions",
            "invoices",
            "worshipEvents",
            "appointments",
            "ministryGroups",
        },
        allowSetters = true
    )
    private Church church;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "church",
            "counselings",
            "tasks",
            "preachIns",
            "liturgyIns",
            "appointments",
            "presidentOfs",
            "supervisorOfs",
            "playIns",
            "participateIns",
            "memberOfs",
        },
        allowSetters = true
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "church", "preacher", "liturgist", "hymns", "musicians", "participants", "appointments" },
        allowSetters = true
    )
    private WorshipEvent service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "church", "president", "supervisor", "members", "appointments" }, allowSetters = true)
    private MinistryGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "church", "member", "user", "appointments" }, allowSetters = true)
    private CounselingSession counselingSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "internalUser", "church" }, allowSetters = true)
    private ApplicationUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Appointment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public Appointment subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Appointment startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Appointment endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return this.notes;
    }

    public Appointment notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocal() {
        return this.local;
    }

    public Appointment local(String local) {
        this.setLocal(local);
        return this;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public AppointmentType getAppointmentType() {
        return this.appointmentType;
    }

    public Appointment appointmentType(AppointmentType appointmentType) {
        this.setAppointmentType(appointmentType);
        return this;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Church getChurch() {
        return this.church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Appointment church(Church church) {
        this.setChurch(church);
        return this;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Appointment member(Member member) {
        this.setMember(member);
        return this;
    }

    public WorshipEvent getService() {
        return this.service;
    }

    public void setService(WorshipEvent worshipEvent) {
        this.service = worshipEvent;
    }

    public Appointment service(WorshipEvent worshipEvent) {
        this.setService(worshipEvent);
        return this;
    }

    public MinistryGroup getGroup() {
        return this.group;
    }

    public void setGroup(MinistryGroup ministryGroup) {
        this.group = ministryGroup;
    }

    public Appointment group(MinistryGroup ministryGroup) {
        this.setGroup(ministryGroup);
        return this;
    }

    public CounselingSession getCounselingSession() {
        return this.counselingSession;
    }

    public void setCounselingSession(CounselingSession counselingSession) {
        this.counselingSession = counselingSession;
    }

    public Appointment counselingSession(CounselingSession counselingSession) {
        this.setCounselingSession(counselingSession);
        return this;
    }

    public ApplicationUser getUser() {
        return this.user;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.user = applicationUser;
    }

    public Appointment user(ApplicationUser applicationUser) {
        this.setUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appointment)) {
            return false;
        }
        return getId() != null && getId().equals(((Appointment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appointment{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", notes='" + getNotes() + "'" +
            ", local='" + getLocal() + "'" +
            ", appointmentType='" + getAppointmentType() + "'" +
            "}";
    }
}
