package br.com.poimen.domain;

import br.com.poimen.domain.enumeration.RoleSchedule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Schedule.
 */
@Entity
@Table(name = "schedule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleSchedule roleType;

    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_schedule__member",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "church", "counselingSessions", "ministryMemberships", "tasks", "transactions", "schedules" },
        allowSetters = true
    )
    private Set<Member> members = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_schedule__worship_event",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "worship_event_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "hymns", "schedules" }, allowSetters = true)
    private Set<WorshipEvent> worshipEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Schedule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleSchedule getRoleType() {
        return this.roleType;
    }

    public Schedule roleType(RoleSchedule roleType) {
        this.setRoleType(roleType);
        return this;
    }

    public void setRoleType(RoleSchedule roleType) {
        this.roleType = roleType;
    }

    public String getNotes() {
        return this.notes;
    }

    public Schedule notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Schedule startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Schedule endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Set<Member> getMembers() {
        return this.members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public Schedule members(Set<Member> members) {
        this.setMembers(members);
        return this;
    }

    public Schedule addMember(Member member) {
        this.members.add(member);
        return this;
    }

    public Schedule removeMember(Member member) {
        this.members.remove(member);
        return this;
    }

    public Set<WorshipEvent> getWorshipEvents() {
        return this.worshipEvents;
    }

    public void setWorshipEvents(Set<WorshipEvent> worshipEvents) {
        this.worshipEvents = worshipEvents;
    }

    public Schedule worshipEvents(Set<WorshipEvent> worshipEvents) {
        this.setWorshipEvents(worshipEvents);
        return this;
    }

    public Schedule addWorshipEvent(WorshipEvent worshipEvent) {
        this.worshipEvents.add(worshipEvent);
        return this;
    }

    public Schedule removeWorshipEvent(WorshipEvent worshipEvent) {
        this.worshipEvents.remove(worshipEvent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Schedule)) {
            return false;
        }
        return getId() != null && getId().equals(((Schedule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Schedule{" +
            "id=" + getId() +
            ", roleType='" + getRoleType() + "'" +
            ", notes='" + getNotes() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
