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
 * A Church.
 */
@Entity
@Table(name = "church")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Church implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "date_foundation", nullable = false)
    private Instant dateFoundation;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_church__user", joinColumns = @JoinColumn(name = "church_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "church", "counselingSessions", "ministryMemberships", "tasks", "transactions", "schedules" },
        allowSetters = true
    )
    private Set<Member> members = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "ministryMemberships" }, allowSetters = true)
    private Set<MinistryGroup> ministryGroups = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "hymns", "schedules" }, allowSetters = true)
    private Set<WorshipEvent> worshipEvents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "member", "user" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "member", "user" }, allowSetters = true)
    private Set<CounselingSession> counselingSessions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "transaction" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoices", "church", "member", "user" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "plan", "church", "user" }, allowSetters = true)
    private Set<PlanSubscription> planSubscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Church id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Church name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public Church cnpj(String cnpj) {
        this.setCnpj(cnpj);
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getAddress() {
        return this.address;
    }

    public Church address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public Church city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Instant getDateFoundation() {
        return this.dateFoundation;
    }

    public Church dateFoundation(Instant dateFoundation) {
        this.setDateFoundation(dateFoundation);
        return this;
    }

    public void setDateFoundation(Instant dateFoundation) {
        this.dateFoundation = dateFoundation;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Church users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Church addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Church removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Set<Member> getMembers() {
        return this.members;
    }

    public void setMembers(Set<Member> members) {
        if (this.members != null) {
            this.members.forEach(i -> i.setChurch(null));
        }
        if (members != null) {
            members.forEach(i -> i.setChurch(this));
        }
        this.members = members;
    }

    public Church members(Set<Member> members) {
        this.setMembers(members);
        return this;
    }

    public Church addMember(Member member) {
        this.members.add(member);
        member.setChurch(this);
        return this;
    }

    public Church removeMember(Member member) {
        this.members.remove(member);
        member.setChurch(null);
        return this;
    }

    public Set<MinistryGroup> getMinistryGroups() {
        return this.ministryGroups;
    }

    public void setMinistryGroups(Set<MinistryGroup> ministryGroups) {
        if (this.ministryGroups != null) {
            this.ministryGroups.forEach(i -> i.setChurch(null));
        }
        if (ministryGroups != null) {
            ministryGroups.forEach(i -> i.setChurch(this));
        }
        this.ministryGroups = ministryGroups;
    }

    public Church ministryGroups(Set<MinistryGroup> ministryGroups) {
        this.setMinistryGroups(ministryGroups);
        return this;
    }

    public Church addMinistryGroup(MinistryGroup ministryGroup) {
        this.ministryGroups.add(ministryGroup);
        ministryGroup.setChurch(this);
        return this;
    }

    public Church removeMinistryGroup(MinistryGroup ministryGroup) {
        this.ministryGroups.remove(ministryGroup);
        ministryGroup.setChurch(null);
        return this;
    }

    public Set<WorshipEvent> getWorshipEvents() {
        return this.worshipEvents;
    }

    public void setWorshipEvents(Set<WorshipEvent> worshipEvents) {
        if (this.worshipEvents != null) {
            this.worshipEvents.forEach(i -> i.setChurch(null));
        }
        if (worshipEvents != null) {
            worshipEvents.forEach(i -> i.setChurch(this));
        }
        this.worshipEvents = worshipEvents;
    }

    public Church worshipEvents(Set<WorshipEvent> worshipEvents) {
        this.setWorshipEvents(worshipEvents);
        return this;
    }

    public Church addWorshipEvent(WorshipEvent worshipEvent) {
        this.worshipEvents.add(worshipEvent);
        worshipEvent.setChurch(this);
        return this;
    }

    public Church removeWorshipEvent(WorshipEvent worshipEvent) {
        this.worshipEvents.remove(worshipEvent);
        worshipEvent.setChurch(null);
        return this;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setChurch(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setChurch(this));
        }
        this.tasks = tasks;
    }

    public Church tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Church addTask(Task task) {
        this.tasks.add(task);
        task.setChurch(this);
        return this;
    }

    public Church removeTask(Task task) {
        this.tasks.remove(task);
        task.setChurch(null);
        return this;
    }

    public Set<CounselingSession> getCounselingSessions() {
        return this.counselingSessions;
    }

    public void setCounselingSessions(Set<CounselingSession> counselingSessions) {
        if (this.counselingSessions != null) {
            this.counselingSessions.forEach(i -> i.setChurch(null));
        }
        if (counselingSessions != null) {
            counselingSessions.forEach(i -> i.setChurch(this));
        }
        this.counselingSessions = counselingSessions;
    }

    public Church counselingSessions(Set<CounselingSession> counselingSessions) {
        this.setCounselingSessions(counselingSessions);
        return this;
    }

    public Church addCounselingSession(CounselingSession counselingSession) {
        this.counselingSessions.add(counselingSession);
        counselingSession.setChurch(this);
        return this;
    }

    public Church removeCounselingSession(CounselingSession counselingSession) {
        this.counselingSessions.remove(counselingSession);
        counselingSession.setChurch(null);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setChurch(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setChurch(this));
        }
        this.invoices = invoices;
    }

    public Church invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Church addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setChurch(this);
        return this;
    }

    public Church removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setChurch(null);
        return this;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setChurch(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setChurch(this));
        }
        this.transactions = transactions;
    }

    public Church transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Church addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setChurch(this);
        return this;
    }

    public Church removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setChurch(null);
        return this;
    }

    public Set<PlanSubscription> getPlanSubscriptions() {
        return this.planSubscriptions;
    }

    public void setPlanSubscriptions(Set<PlanSubscription> planSubscriptions) {
        if (this.planSubscriptions != null) {
            this.planSubscriptions.forEach(i -> i.setChurch(null));
        }
        if (planSubscriptions != null) {
            planSubscriptions.forEach(i -> i.setChurch(this));
        }
        this.planSubscriptions = planSubscriptions;
    }

    public Church planSubscriptions(Set<PlanSubscription> planSubscriptions) {
        this.setPlanSubscriptions(planSubscriptions);
        return this;
    }

    public Church addPlanSubscription(PlanSubscription planSubscription) {
        this.planSubscriptions.add(planSubscription);
        planSubscription.setChurch(this);
        return this;
    }

    public Church removePlanSubscription(PlanSubscription planSubscription) {
        this.planSubscriptions.remove(planSubscription);
        planSubscription.setChurch(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Church)) {
            return false;
        }
        return getId() != null && getId().equals(((Church) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Church{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", dateFoundation='" + getDateFoundation() + "'" +
            "}";
    }
}
