package br.com.poimen.domain;

import br.com.poimen.domain.enumeration.ExitReason;
import br.com.poimen.domain.enumeration.MaritalStatus;
import br.com.poimen.domain.enumeration.MemberStatus;
import br.com.poimen.domain.enumeration.MembershipType;
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
 * A Member.
 */
@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Member implements Serializable {

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

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "email")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @Column(name = "phone_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String phoneNumber;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "address")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String address;

    @Column(name = "city")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String city;

    @Column(name = "state")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String state;

    @Column(name = "zip_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String zipCode;

    @Column(name = "city_of_birth")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cityOfBirth;

    @Column(name = "previous_religion")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String previousReligion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private MaritalStatus maritalStatus;

    @Column(name = "spouse_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String spouseName;

    @Column(name = "date_of_marriage")
    private LocalDate dateOfMarriage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private MemberStatus status;

    @NotNull
    @Pattern(regexp = "^\\d{11}$")
    @Column(name = "cpf", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cpf;

    @NotNull
    @Column(name = "rg", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String rg;

    @Column(name = "date_of_baptism")
    private LocalDate dateOfBaptism;

    @Column(name = "church_of_baptism")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String churchOfBaptism;

    @Column(name = "date_of_membership")
    private LocalDate dateOfMembership;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_membership")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private MembershipType typeOfMembership;

    @Column(name = "association_meeting_minutes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String associationMeetingMinutes;

    @Column(name = "date_of_death")
    private LocalDate dateOfDeath;

    @Column(name = "date_of_exit")
    private LocalDate dateOfExit;

    @Column(name = "exit_destination")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String exitDestination;

    @Enumerated(EnumType.STRING)
    @Column(name = "exit_reason")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ExitReason exitReason;

    @Column(name = "exit_meeting_minutes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String exitMeetingMinutes;

    @Lob
    @Column(name = "notes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String notes;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "member", "user", "appointments" }, allowSetters = true)
    private Set<CounselingSession> counselings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "member", "user" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "preacher")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "church", "preacher", "liturgist", "hymns", "musicians", "participants", "appointments" },
        allowSetters = true
    )
    private Set<WorshipEvent> preachIns = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "liturgist")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "church", "preacher", "liturgist", "hymns", "musicians", "participants", "appointments" },
        allowSetters = true
    )
    private Set<WorshipEvent> liturgyIns = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "member", "service", "group", "counselingSession", "user" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "president")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "president", "supervisor", "members", "appointments" }, allowSetters = true)
    private Set<MinistryGroup> presidentOfs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "supervisor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "president", "supervisor", "members", "appointments" }, allowSetters = true)
    private Set<MinistryGroup> supervisorOfs = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "musicians")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "church", "preacher", "liturgist", "hymns", "musicians", "participants", "appointments" },
        allowSetters = true
    )
    private Set<WorshipEvent> playIns = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "church", "preacher", "liturgist", "hymns", "musicians", "participants", "appointments" },
        allowSetters = true
    )
    private Set<WorshipEvent> participateIns = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "president", "supervisor", "members", "appointments" }, allowSetters = true)
    private Set<MinistryGroup> memberOfs = new HashSet<>();

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

    public String getName() {
        return this.name;
    }

    public Member name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Member photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Member photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
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

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Member dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
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

    public String getCity() {
        return this.city;
    }

    public Member city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public Member state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public Member zipCode(String zipCode) {
        this.setZipCode(zipCode);
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityOfBirth() {
        return this.cityOfBirth;
    }

    public Member cityOfBirth(String cityOfBirth) {
        this.setCityOfBirth(cityOfBirth);
        return this;
    }

    public void setCityOfBirth(String cityOfBirth) {
        this.cityOfBirth = cityOfBirth;
    }

    public String getPreviousReligion() {
        return this.previousReligion;
    }

    public Member previousReligion(String previousReligion) {
        this.setPreviousReligion(previousReligion);
        return this;
    }

    public void setPreviousReligion(String previousReligion) {
        this.previousReligion = previousReligion;
    }

    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    public Member maritalStatus(MaritalStatus maritalStatus) {
        this.setMaritalStatus(maritalStatus);
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSpouseName() {
        return this.spouseName;
    }

    public Member spouseName(String spouseName) {
        this.setSpouseName(spouseName);
        return this;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public LocalDate getDateOfMarriage() {
        return this.dateOfMarriage;
    }

    public Member dateOfMarriage(LocalDate dateOfMarriage) {
        this.setDateOfMarriage(dateOfMarriage);
        return this;
    }

    public void setDateOfMarriage(LocalDate dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public MemberStatus getStatus() {
        return this.status;
    }

    public Member status(MemberStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Member cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return this.rg;
    }

    public Member rg(String rg) {
        this.setRg(rg);
        return this;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getDateOfBaptism() {
        return this.dateOfBaptism;
    }

    public Member dateOfBaptism(LocalDate dateOfBaptism) {
        this.setDateOfBaptism(dateOfBaptism);
        return this;
    }

    public void setDateOfBaptism(LocalDate dateOfBaptism) {
        this.dateOfBaptism = dateOfBaptism;
    }

    public String getChurchOfBaptism() {
        return this.churchOfBaptism;
    }

    public Member churchOfBaptism(String churchOfBaptism) {
        this.setChurchOfBaptism(churchOfBaptism);
        return this;
    }

    public void setChurchOfBaptism(String churchOfBaptism) {
        this.churchOfBaptism = churchOfBaptism;
    }

    public LocalDate getDateOfMembership() {
        return this.dateOfMembership;
    }

    public Member dateOfMembership(LocalDate dateOfMembership) {
        this.setDateOfMembership(dateOfMembership);
        return this;
    }

    public void setDateOfMembership(LocalDate dateOfMembership) {
        this.dateOfMembership = dateOfMembership;
    }

    public MembershipType getTypeOfMembership() {
        return this.typeOfMembership;
    }

    public Member typeOfMembership(MembershipType typeOfMembership) {
        this.setTypeOfMembership(typeOfMembership);
        return this;
    }

    public void setTypeOfMembership(MembershipType typeOfMembership) {
        this.typeOfMembership = typeOfMembership;
    }

    public String getAssociationMeetingMinutes() {
        return this.associationMeetingMinutes;
    }

    public Member associationMeetingMinutes(String associationMeetingMinutes) {
        this.setAssociationMeetingMinutes(associationMeetingMinutes);
        return this;
    }

    public void setAssociationMeetingMinutes(String associationMeetingMinutes) {
        this.associationMeetingMinutes = associationMeetingMinutes;
    }

    public LocalDate getDateOfDeath() {
        return this.dateOfDeath;
    }

    public Member dateOfDeath(LocalDate dateOfDeath) {
        this.setDateOfDeath(dateOfDeath);
        return this;
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public LocalDate getDateOfExit() {
        return this.dateOfExit;
    }

    public Member dateOfExit(LocalDate dateOfExit) {
        this.setDateOfExit(dateOfExit);
        return this;
    }

    public void setDateOfExit(LocalDate dateOfExit) {
        this.dateOfExit = dateOfExit;
    }

    public String getExitDestination() {
        return this.exitDestination;
    }

    public Member exitDestination(String exitDestination) {
        this.setExitDestination(exitDestination);
        return this;
    }

    public void setExitDestination(String exitDestination) {
        this.exitDestination = exitDestination;
    }

    public ExitReason getExitReason() {
        return this.exitReason;
    }

    public Member exitReason(ExitReason exitReason) {
        this.setExitReason(exitReason);
        return this;
    }

    public void setExitReason(ExitReason exitReason) {
        this.exitReason = exitReason;
    }

    public String getExitMeetingMinutes() {
        return this.exitMeetingMinutes;
    }

    public Member exitMeetingMinutes(String exitMeetingMinutes) {
        this.setExitMeetingMinutes(exitMeetingMinutes);
        return this;
    }

    public void setExitMeetingMinutes(String exitMeetingMinutes) {
        this.exitMeetingMinutes = exitMeetingMinutes;
    }

    public String getNotes() {
        return this.notes;
    }

    public Member notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Set<CounselingSession> getCounselings() {
        return this.counselings;
    }

    public void setCounselings(Set<CounselingSession> counselingSessions) {
        if (this.counselings != null) {
            this.counselings.forEach(i -> i.setMember(null));
        }
        if (counselingSessions != null) {
            counselingSessions.forEach(i -> i.setMember(this));
        }
        this.counselings = counselingSessions;
    }

    public Member counselings(Set<CounselingSession> counselingSessions) {
        this.setCounselings(counselingSessions);
        return this;
    }

    public Member addCounseling(CounselingSession counselingSession) {
        this.counselings.add(counselingSession);
        counselingSession.setMember(this);
        return this;
    }

    public Member removeCounseling(CounselingSession counselingSession) {
        this.counselings.remove(counselingSession);
        counselingSession.setMember(null);
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

    public Member addTasks(Task task) {
        this.tasks.add(task);
        task.setMember(this);
        return this;
    }

    public Member removeTasks(Task task) {
        this.tasks.remove(task);
        task.setMember(null);
        return this;
    }

    public Set<WorshipEvent> getPreachIns() {
        return this.preachIns;
    }

    public void setPreachIns(Set<WorshipEvent> worshipEvents) {
        if (this.preachIns != null) {
            this.preachIns.forEach(i -> i.setPreacher(null));
        }
        if (worshipEvents != null) {
            worshipEvents.forEach(i -> i.setPreacher(this));
        }
        this.preachIns = worshipEvents;
    }

    public Member preachIns(Set<WorshipEvent> worshipEvents) {
        this.setPreachIns(worshipEvents);
        return this;
    }

    public Member addPreachIn(WorshipEvent worshipEvent) {
        this.preachIns.add(worshipEvent);
        worshipEvent.setPreacher(this);
        return this;
    }

    public Member removePreachIn(WorshipEvent worshipEvent) {
        this.preachIns.remove(worshipEvent);
        worshipEvent.setPreacher(null);
        return this;
    }

    public Set<WorshipEvent> getLiturgyIns() {
        return this.liturgyIns;
    }

    public void setLiturgyIns(Set<WorshipEvent> worshipEvents) {
        if (this.liturgyIns != null) {
            this.liturgyIns.forEach(i -> i.setLiturgist(null));
        }
        if (worshipEvents != null) {
            worshipEvents.forEach(i -> i.setLiturgist(this));
        }
        this.liturgyIns = worshipEvents;
    }

    public Member liturgyIns(Set<WorshipEvent> worshipEvents) {
        this.setLiturgyIns(worshipEvents);
        return this;
    }

    public Member addLiturgyIn(WorshipEvent worshipEvent) {
        this.liturgyIns.add(worshipEvent);
        worshipEvent.setLiturgist(this);
        return this;
    }

    public Member removeLiturgyIn(WorshipEvent worshipEvent) {
        this.liturgyIns.remove(worshipEvent);
        worshipEvent.setLiturgist(null);
        return this;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setMember(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setMember(this));
        }
        this.appointments = appointments;
    }

    public Member appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public Member addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setMember(this);
        return this;
    }

    public Member removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setMember(null);
        return this;
    }

    public Set<MinistryGroup> getPresidentOfs() {
        return this.presidentOfs;
    }

    public void setPresidentOfs(Set<MinistryGroup> ministryGroups) {
        if (this.presidentOfs != null) {
            this.presidentOfs.forEach(i -> i.setPresident(null));
        }
        if (ministryGroups != null) {
            ministryGroups.forEach(i -> i.setPresident(this));
        }
        this.presidentOfs = ministryGroups;
    }

    public Member presidentOfs(Set<MinistryGroup> ministryGroups) {
        this.setPresidentOfs(ministryGroups);
        return this;
    }

    public Member addPresidentOf(MinistryGroup ministryGroup) {
        this.presidentOfs.add(ministryGroup);
        ministryGroup.setPresident(this);
        return this;
    }

    public Member removePresidentOf(MinistryGroup ministryGroup) {
        this.presidentOfs.remove(ministryGroup);
        ministryGroup.setPresident(null);
        return this;
    }

    public Set<MinistryGroup> getSupervisorOfs() {
        return this.supervisorOfs;
    }

    public void setSupervisorOfs(Set<MinistryGroup> ministryGroups) {
        if (this.supervisorOfs != null) {
            this.supervisorOfs.forEach(i -> i.setSupervisor(null));
        }
        if (ministryGroups != null) {
            ministryGroups.forEach(i -> i.setSupervisor(this));
        }
        this.supervisorOfs = ministryGroups;
    }

    public Member supervisorOfs(Set<MinistryGroup> ministryGroups) {
        this.setSupervisorOfs(ministryGroups);
        return this;
    }

    public Member addSupervisorOf(MinistryGroup ministryGroup) {
        this.supervisorOfs.add(ministryGroup);
        ministryGroup.setSupervisor(this);
        return this;
    }

    public Member removeSupervisorOf(MinistryGroup ministryGroup) {
        this.supervisorOfs.remove(ministryGroup);
        ministryGroup.setSupervisor(null);
        return this;
    }

    public Set<WorshipEvent> getPlayIns() {
        return this.playIns;
    }

    public void setPlayIns(Set<WorshipEvent> worshipEvents) {
        if (this.playIns != null) {
            this.playIns.forEach(i -> i.removeMusicians(this));
        }
        if (worshipEvents != null) {
            worshipEvents.forEach(i -> i.addMusicians(this));
        }
        this.playIns = worshipEvents;
    }

    public Member playIns(Set<WorshipEvent> worshipEvents) {
        this.setPlayIns(worshipEvents);
        return this;
    }

    public Member addPlayIn(WorshipEvent worshipEvent) {
        this.playIns.add(worshipEvent);
        worshipEvent.getMusicians().add(this);
        return this;
    }

    public Member removePlayIn(WorshipEvent worshipEvent) {
        this.playIns.remove(worshipEvent);
        worshipEvent.getMusicians().remove(this);
        return this;
    }

    public Set<WorshipEvent> getParticipateIns() {
        return this.participateIns;
    }

    public void setParticipateIns(Set<WorshipEvent> worshipEvents) {
        if (this.participateIns != null) {
            this.participateIns.forEach(i -> i.removeParticipants(this));
        }
        if (worshipEvents != null) {
            worshipEvents.forEach(i -> i.addParticipants(this));
        }
        this.participateIns = worshipEvents;
    }

    public Member participateIns(Set<WorshipEvent> worshipEvents) {
        this.setParticipateIns(worshipEvents);
        return this;
    }

    public Member addParticipateIn(WorshipEvent worshipEvent) {
        this.participateIns.add(worshipEvent);
        worshipEvent.getParticipants().add(this);
        return this;
    }

    public Member removeParticipateIn(WorshipEvent worshipEvent) {
        this.participateIns.remove(worshipEvent);
        worshipEvent.getParticipants().remove(this);
        return this;
    }

    public Set<MinistryGroup> getMemberOfs() {
        return this.memberOfs;
    }

    public void setMemberOfs(Set<MinistryGroup> ministryGroups) {
        if (this.memberOfs != null) {
            this.memberOfs.forEach(i -> i.removeMembers(this));
        }
        if (ministryGroups != null) {
            ministryGroups.forEach(i -> i.addMembers(this));
        }
        this.memberOfs = ministryGroups;
    }

    public Member memberOfs(Set<MinistryGroup> ministryGroups) {
        this.setMemberOfs(ministryGroups);
        return this;
    }

    public Member addMemberOf(MinistryGroup ministryGroup) {
        this.memberOfs.add(ministryGroup);
        ministryGroup.getMembers().add(this);
        return this;
    }

    public Member removeMemberOf(MinistryGroup ministryGroup) {
        this.memberOfs.remove(ministryGroup);
        ministryGroup.getMembers().remove(this);
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
            ", name='" + getName() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", cityOfBirth='" + getCityOfBirth() + "'" +
            ", previousReligion='" + getPreviousReligion() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", spouseName='" + getSpouseName() + "'" +
            ", dateOfMarriage='" + getDateOfMarriage() + "'" +
            ", status='" + getStatus() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", rg='" + getRg() + "'" +
            ", dateOfBaptism='" + getDateOfBaptism() + "'" +
            ", churchOfBaptism='" + getChurchOfBaptism() + "'" +
            ", dateOfMembership='" + getDateOfMembership() + "'" +
            ", typeOfMembership='" + getTypeOfMembership() + "'" +
            ", associationMeetingMinutes='" + getAssociationMeetingMinutes() + "'" +
            ", dateOfDeath='" + getDateOfDeath() + "'" +
            ", dateOfExit='" + getDateOfExit() + "'" +
            ", exitDestination='" + getExitDestination() + "'" +
            ", exitReason='" + getExitReason() + "'" +
            ", exitMeetingMinutes='" + getExitMeetingMinutes() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
