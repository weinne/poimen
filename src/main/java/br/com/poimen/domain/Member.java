package br.com.poimen.domain;

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
 * A Member.
 */
@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "users",
            "members",
            "ministryGroups",
            "worshipEvents",
            "tasks",
            "counselingSessions",
            "invoices",
            "transactions",
            "planSubscriptions",
        },
        allowSetters = true
    )
    private Church church;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "member", "user" }, allowSetters = true)
    private Set<CounselingSession> counselingSessions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ministryGroup", "member" }, allowSetters = true)
    private Set<MinistryMembership> ministryMemberships = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "member", "user" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoices", "church", "member", "user" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "members", "worshipEvents" }, allowSetters = true)
    private Set<Schedule> schedules = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Member id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Member firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Member lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Member email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Member phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Member dateOfBirth(Instant dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return this.address;
    }

    public Member address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Church getChurch() {
        return this.church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Member church(Church church) {
        this.setChurch(church);
        return this;
    }

    public Set<CounselingSession> getCounselingSessions() {
        return this.counselingSessions;
    }

    public void setCounselingSessions(Set<CounselingSession> counselingSessions) {
        if (this.counselingSessions != null) {
            this.counselingSessions.forEach(i -> i.setMember(null));
        }
        if (counselingSessions != null) {
            counselingSessions.forEach(i -> i.setMember(this));
        }
        this.counselingSessions = counselingSessions;
    }

    public Member counselingSessions(Set<CounselingSession> counselingSessions) {
        this.setCounselingSessions(counselingSessions);
        return this;
    }

    public Member addCounselingSession(CounselingSession counselingSession) {
        this.counselingSessions.add(counselingSession);
        counselingSession.setMember(this);
        return this;
    }

    public Member removeCounselingSession(CounselingSession counselingSession) {
        this.counselingSessions.remove(counselingSession);
        counselingSession.setMember(null);
        return this;
    }

    public Set<MinistryMembership> getMinistryMemberships() {
        return this.ministryMemberships;
    }

    public void setMinistryMemberships(Set<MinistryMembership> ministryMemberships) {
        if (this.ministryMemberships != null) {
            this.ministryMemberships.forEach(i -> i.setMember(null));
        }
        if (ministryMemberships != null) {
            ministryMemberships.forEach(i -> i.setMember(this));
        }
        this.ministryMemberships = ministryMemberships;
    }

    public Member ministryMemberships(Set<MinistryMembership> ministryMemberships) {
        this.setMinistryMemberships(ministryMemberships);
        return this;
    }

    public Member addMinistryMembership(MinistryMembership ministryMembership) {
        this.ministryMemberships.add(ministryMembership);
        ministryMembership.setMember(this);
        return this;
    }

    public Member removeMinistryMembership(MinistryMembership ministryMembership) {
        this.ministryMemberships.remove(ministryMembership);
        ministryMembership.setMember(null);
        return this;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setMember(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setMember(this));
        }
        this.tasks = tasks;
    }

    public Member tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Member addTask(Task task) {
        this.tasks.add(task);
        task.setMember(this);
        return this;
    }

    public Member removeTask(Task task) {
        this.tasks.remove(task);
        task.setMember(null);
        return this;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setMember(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setMember(this));
        }
        this.transactions = transactions;
    }

    public Member transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Member addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setMember(this);
        return this;
    }

    public Member removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setMember(null);
        return this;
    }

    public Set<Schedule> getSchedules() {
        return this.schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        if (this.schedules != null) {
            this.schedules.forEach(i -> i.removeMember(this));
        }
        if (schedules != null) {
            schedules.forEach(i -> i.addMember(this));
        }
        this.schedules = schedules;
    }

    public Member schedules(Set<Schedule> schedules) {
        this.setSchedules(schedules);
        return this;
    }

    public Member addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
        schedule.getMembers().add(this);
        return this;
    }

    public Member removeSchedule(Schedule schedule) {
        this.schedules.remove(schedule);
        schedule.getMembers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return getId() != null && getId().equals(((Member) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
