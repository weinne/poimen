package br.com.poimen.domain;

import br.com.poimen.domain.enumeration.StatusCounseling;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CounselingSession.
 */
@Entity
@Table(name = "counseling_session")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "counselingsession")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CounselingSession implements Serializable {

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
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Lob
    @Column(name = "notes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String notes;

    @Lob
    @Column(name = "counseling_tasks")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String counselingTasks;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private StatusCounseling status;

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
    @JsonIgnoreProperties(value = { "internalUser", "church" }, allowSetters = true)
    private ApplicationUser user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "counselingSession")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "member", "service", "group", "counselingSession", "user" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CounselingSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public CounselingSession subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public CounselingSession date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public CounselingSession startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public CounselingSession endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return this.notes;
    }

    public CounselingSession notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCounselingTasks() {
        return this.counselingTasks;
    }

    public CounselingSession counselingTasks(String counselingTasks) {
        this.setCounselingTasks(counselingTasks);
        return this;
    }

    public void setCounselingTasks(String counselingTasks) {
        this.counselingTasks = counselingTasks;
    }

    public StatusCounseling getStatus() {
        return this.status;
    }

    public CounselingSession status(StatusCounseling status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusCounseling status) {
        this.status = status;
    }

    public Church getChurch() {
        return this.church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public CounselingSession church(Church church) {
        this.setChurch(church);
        return this;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public CounselingSession member(Member member) {
        this.setMember(member);
        return this;
    }

    public ApplicationUser getUser() {
        return this.user;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.user = applicationUser;
    }

    public CounselingSession user(ApplicationUser applicationUser) {
        this.setUser(applicationUser);
        return this;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setCounselingSession(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setCounselingSession(this));
        }
        this.appointments = appointments;
    }

    public CounselingSession appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public CounselingSession addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setCounselingSession(this);
        return this;
    }

    public CounselingSession removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setCounselingSession(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CounselingSession)) {
            return false;
        }
        return getId() != null && getId().equals(((CounselingSession) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounselingSession{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", date='" + getDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", notes='" + getNotes() + "'" +
            ", counselingTasks='" + getCounselingTasks() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
