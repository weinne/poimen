package br.com.poimen.domain;

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
 * A Church.
 */
@Entity
@Table(name = "church")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "church")
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
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @NotNull
    @Pattern(regexp = "^\\d{14}$")
    @Column(name = "cnpj", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cnpj;

    @NotNull
    @Column(name = "address", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String address;

    @NotNull
    @Column(name = "city", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String city;

    @NotNull
    @Column(name = "date_foundation", nullable = false)
    private LocalDate dateFoundation;

    @Column(name = "phone")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String phone;

    @Column(name = "email")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @Column(name = "website")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String website;

    @Column(name = "facebook")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String facebook;

    @Column(name = "instagram")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String instagram;

    @Column(name = "twitter")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String twitter;

    @Column(name = "youtube")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String youtube;

    @Lob
    @Column(name = "about")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String about;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "internalUser", "church" }, allowSetters = true)
    private Set<ApplicationUser> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "plan", "user" }, allowSetters = true)
    private Set<PlanSubscription> subscriptions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "member", "user", "appointments" }, allowSetters = true)
    private Set<CounselingSession> counselingSessions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "member", "user" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "invoices", "church", "member", "user" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "transaction" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "church", "preacher", "liturgist", "hymns", "musicians", "participants", "appointments" },
        allowSetters = true
    )
    private Set<WorshipEvent> worshipEvents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "member", "service", "group", "counselingSession", "user" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "church")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "president", "supervisor", "members", "appointments" }, allowSetters = true)
    private Set<MinistryGroup> ministryGroups = new HashSet<>();

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

    public LocalDate getDateFoundation() {
        return this.dateFoundation;
    }

    public Church dateFoundation(LocalDate dateFoundation) {
        this.setDateFoundation(dateFoundation);
        return this;
    }

    public void setDateFoundation(LocalDate dateFoundation) {
        this.dateFoundation = dateFoundation;
    }

    public String getPhone() {
        return this.phone;
    }

    public Church phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Church email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return this.website;
    }

    public Church website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public Church facebook(String facebook) {
        this.setFacebook(facebook);
        return this;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return this.instagram;
    }

    public Church instagram(String instagram) {
        this.setInstagram(instagram);
        return this;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public Church twitter(String twitter) {
        this.setTwitter(twitter);
        return this;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return this.youtube;
    }

    public Church youtube(String youtube) {
        this.setYoutube(youtube);
        return this;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getAbout() {
        return this.about;
    }

    public Church about(String about) {
        this.setAbout(about);
        return this;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Set<ApplicationUser> getUsers() {
        return this.users;
    }

    public void setUsers(Set<ApplicationUser> applicationUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.setChurch(null));
        }
        if (applicationUsers != null) {
            applicationUsers.forEach(i -> i.setChurch(this));
        }
        this.users = applicationUsers;
    }

    public Church users(Set<ApplicationUser> applicationUsers) {
        this.setUsers(applicationUsers);
        return this;
    }

    public Church addUser(ApplicationUser applicationUser) {
        this.users.add(applicationUser);
        applicationUser.setChurch(this);
        return this;
    }

    public Church removeUser(ApplicationUser applicationUser) {
        this.users.remove(applicationUser);
        applicationUser.setChurch(null);
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

    public Set<PlanSubscription> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(Set<PlanSubscription> planSubscriptions) {
        if (this.subscriptions != null) {
            this.subscriptions.forEach(i -> i.setChurch(null));
        }
        if (planSubscriptions != null) {
            planSubscriptions.forEach(i -> i.setChurch(this));
        }
        this.subscriptions = planSubscriptions;
    }

    public Church subscriptions(Set<PlanSubscription> planSubscriptions) {
        this.setSubscriptions(planSubscriptions);
        return this;
    }

    public Church addSubscription(PlanSubscription planSubscription) {
        this.subscriptions.add(planSubscription);
        planSubscription.setChurch(this);
        return this;
    }

    public Church removeSubscription(PlanSubscription planSubscription) {
        this.subscriptions.remove(planSubscription);
        planSubscription.setChurch(null);
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

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setChurch(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setChurch(this));
        }
        this.appointments = appointments;
    }

    public Church appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public Church addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setChurch(this);
        return this;
    }

    public Church removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setChurch(null);
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
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", website='" + getWebsite() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", instagram='" + getInstagram() + "'" +
            ", twitter='" + getTwitter() + "'" +
            ", youtube='" + getYoutube() + "'" +
            ", about='" + getAbout() + "'" +
            "}";
    }
}
