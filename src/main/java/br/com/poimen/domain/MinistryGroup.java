package br.com.poimen.domain;

import br.com.poimen.domain.enumeration.GroupType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MinistryGroup.
 */
@Entity
@Table(name = "ministry_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ministrygroup")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MinistryGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "established_date")
    private LocalDate establishedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private GroupType type;

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
    private Member president;

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
    private Member supervisor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_ministry_group__members",
        joinColumns = @JoinColumn(name = "ministry_group_id"),
        inverseJoinColumns = @JoinColumn(name = "members_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Member> members = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "member", "service", "group", "counselingSession", "user" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MinistryGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MinistryGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MinistryGroup description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEstablishedDate() {
        return this.establishedDate;
    }

    public MinistryGroup establishedDate(LocalDate establishedDate) {
        this.setEstablishedDate(establishedDate);
        return this;
    }

    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }

    public GroupType getType() {
        return this.type;
    }

    public MinistryGroup type(GroupType type) {
        this.setType(type);
        return this;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public Church getChurch() {
        return this.church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public MinistryGroup church(Church church) {
        this.setChurch(church);
        return this;
    }

    public Member getPresident() {
        return this.president;
    }

    public void setPresident(Member member) {
        this.president = member;
    }

    public MinistryGroup president(Member member) {
        this.setPresident(member);
        return this;
    }

    public Member getSupervisor() {
        return this.supervisor;
    }

    public void setSupervisor(Member member) {
        this.supervisor = member;
    }

    public MinistryGroup supervisor(Member member) {
        this.setSupervisor(member);
        return this;
    }

    public Set<Member> getMembers() {
        return this.members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public MinistryGroup members(Set<Member> members) {
        this.setMembers(members);
        return this;
    }

    public MinistryGroup addMembers(Member member) {
        this.members.add(member);
        return this;
    }

    public MinistryGroup removeMembers(Member member) {
        this.members.remove(member);
        return this;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setGroup(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setGroup(this));
        }
        this.appointments = appointments;
    }

    public MinistryGroup appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public MinistryGroup addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setGroup(this);
        return this;
    }

    public MinistryGroup removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setGroup(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MinistryGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((MinistryGroup) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MinistryGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", establishedDate='" + getEstablishedDate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
