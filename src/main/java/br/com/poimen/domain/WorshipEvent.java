package br.com.poimen.domain;

import br.com.poimen.domain.enumeration.WorshipType;
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
 * A WorshipEvent.
 */
@Entity
@Table(name = "worship_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "worshipevent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorshipEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Column(name = "guest_preacher")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String guestPreacher;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "call_to_worship_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String callToWorshipText;

    @Column(name = "confession_of_sin_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String confessionOfSinText;

    @Column(name = "assurance_of_pardon_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assuranceOfPardonText;

    @Column(name = "lord_supper_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lordSupperText;

    @Column(name = "benediction_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String benedictionText;

    @Column(name = "confessional_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String confessionalText;

    @Lob
    @Column(name = "sermon_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sermonText;

    @Lob
    @Column(name = "sermon_file")
    private byte[] sermonFile;

    @Column(name = "sermon_file_content_type")
    private String sermonFileContentType;

    @Column(name = "sermon_link")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sermonLink;

    @Column(name = "youtube_link")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String youtubeLink;

    @Lob
    @Column(name = "bulletin_file")
    private byte[] bulletinFile;

    @Column(name = "bulletin_file_content_type")
    private String bulletinFileContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "worship_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private WorshipType worshipType;

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
    private Member preacher;

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
    private Member liturgist;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_worship_event__hymns",
        joinColumns = @JoinColumn(name = "worship_event_id"),
        inverseJoinColumns = @JoinColumn(name = "hymns_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "services" }, allowSetters = true)
    private Set<Hymn> hymns = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_worship_event__musicians",
        joinColumns = @JoinColumn(name = "worship_event_id"),
        inverseJoinColumns = @JoinColumn(name = "musicians_id")
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
    private Set<Member> musicians = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_worship_event__participants",
        joinColumns = @JoinColumn(name = "worship_event_id"),
        inverseJoinColumns = @JoinColumn(name = "participants_id")
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
    private Set<Member> participants = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "member", "service", "group", "counselingSession", "user" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorshipEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public WorshipEvent date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getTitle() {
        return this.title;
    }

    public WorshipEvent title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuestPreacher() {
        return this.guestPreacher;
    }

    public WorshipEvent guestPreacher(String guestPreacher) {
        this.setGuestPreacher(guestPreacher);
        return this;
    }

    public void setGuestPreacher(String guestPreacher) {
        this.guestPreacher = guestPreacher;
    }

    public String getDescription() {
        return this.description;
    }

    public WorshipEvent description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCallToWorshipText() {
        return this.callToWorshipText;
    }

    public WorshipEvent callToWorshipText(String callToWorshipText) {
        this.setCallToWorshipText(callToWorshipText);
        return this;
    }

    public void setCallToWorshipText(String callToWorshipText) {
        this.callToWorshipText = callToWorshipText;
    }

    public String getConfessionOfSinText() {
        return this.confessionOfSinText;
    }

    public WorshipEvent confessionOfSinText(String confessionOfSinText) {
        this.setConfessionOfSinText(confessionOfSinText);
        return this;
    }

    public void setConfessionOfSinText(String confessionOfSinText) {
        this.confessionOfSinText = confessionOfSinText;
    }

    public String getAssuranceOfPardonText() {
        return this.assuranceOfPardonText;
    }

    public WorshipEvent assuranceOfPardonText(String assuranceOfPardonText) {
        this.setAssuranceOfPardonText(assuranceOfPardonText);
        return this;
    }

    public void setAssuranceOfPardonText(String assuranceOfPardonText) {
        this.assuranceOfPardonText = assuranceOfPardonText;
    }

    public String getLordSupperText() {
        return this.lordSupperText;
    }

    public WorshipEvent lordSupperText(String lordSupperText) {
        this.setLordSupperText(lordSupperText);
        return this;
    }

    public void setLordSupperText(String lordSupperText) {
        this.lordSupperText = lordSupperText;
    }

    public String getBenedictionText() {
        return this.benedictionText;
    }

    public WorshipEvent benedictionText(String benedictionText) {
        this.setBenedictionText(benedictionText);
        return this;
    }

    public void setBenedictionText(String benedictionText) {
        this.benedictionText = benedictionText;
    }

    public String getConfessionalText() {
        return this.confessionalText;
    }

    public WorshipEvent confessionalText(String confessionalText) {
        this.setConfessionalText(confessionalText);
        return this;
    }

    public void setConfessionalText(String confessionalText) {
        this.confessionalText = confessionalText;
    }

    public String getSermonText() {
        return this.sermonText;
    }

    public WorshipEvent sermonText(String sermonText) {
        this.setSermonText(sermonText);
        return this;
    }

    public void setSermonText(String sermonText) {
        this.sermonText = sermonText;
    }

    public byte[] getSermonFile() {
        return this.sermonFile;
    }

    public WorshipEvent sermonFile(byte[] sermonFile) {
        this.setSermonFile(sermonFile);
        return this;
    }

    public void setSermonFile(byte[] sermonFile) {
        this.sermonFile = sermonFile;
    }

    public String getSermonFileContentType() {
        return this.sermonFileContentType;
    }

    public WorshipEvent sermonFileContentType(String sermonFileContentType) {
        this.sermonFileContentType = sermonFileContentType;
        return this;
    }

    public void setSermonFileContentType(String sermonFileContentType) {
        this.sermonFileContentType = sermonFileContentType;
    }

    public String getSermonLink() {
        return this.sermonLink;
    }

    public WorshipEvent sermonLink(String sermonLink) {
        this.setSermonLink(sermonLink);
        return this;
    }

    public void setSermonLink(String sermonLink) {
        this.sermonLink = sermonLink;
    }

    public String getYoutubeLink() {
        return this.youtubeLink;
    }

    public WorshipEvent youtubeLink(String youtubeLink) {
        this.setYoutubeLink(youtubeLink);
        return this;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public byte[] getBulletinFile() {
        return this.bulletinFile;
    }

    public WorshipEvent bulletinFile(byte[] bulletinFile) {
        this.setBulletinFile(bulletinFile);
        return this;
    }

    public void setBulletinFile(byte[] bulletinFile) {
        this.bulletinFile = bulletinFile;
    }

    public String getBulletinFileContentType() {
        return this.bulletinFileContentType;
    }

    public WorshipEvent bulletinFileContentType(String bulletinFileContentType) {
        this.bulletinFileContentType = bulletinFileContentType;
        return this;
    }

    public void setBulletinFileContentType(String bulletinFileContentType) {
        this.bulletinFileContentType = bulletinFileContentType;
    }

    public WorshipType getWorshipType() {
        return this.worshipType;
    }

    public WorshipEvent worshipType(WorshipType worshipType) {
        this.setWorshipType(worshipType);
        return this;
    }

    public void setWorshipType(WorshipType worshipType) {
        this.worshipType = worshipType;
    }

    public Church getChurch() {
        return this.church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public WorshipEvent church(Church church) {
        this.setChurch(church);
        return this;
    }

    public Member getPreacher() {
        return this.preacher;
    }

    public void setPreacher(Member member) {
        this.preacher = member;
    }

    public WorshipEvent preacher(Member member) {
        this.setPreacher(member);
        return this;
    }

    public Member getLiturgist() {
        return this.liturgist;
    }

    public void setLiturgist(Member member) {
        this.liturgist = member;
    }

    public WorshipEvent liturgist(Member member) {
        this.setLiturgist(member);
        return this;
    }

    public Set<Hymn> getHymns() {
        return this.hymns;
    }

    public void setHymns(Set<Hymn> hymns) {
        this.hymns = hymns;
    }

    public WorshipEvent hymns(Set<Hymn> hymns) {
        this.setHymns(hymns);
        return this;
    }

    public WorshipEvent addHymns(Hymn hymn) {
        this.hymns.add(hymn);
        return this;
    }

    public WorshipEvent removeHymns(Hymn hymn) {
        this.hymns.remove(hymn);
        return this;
    }

    public Set<Member> getMusicians() {
        return this.musicians;
    }

    public void setMusicians(Set<Member> members) {
        this.musicians = members;
    }

    public WorshipEvent musicians(Set<Member> members) {
        this.setMusicians(members);
        return this;
    }

    public WorshipEvent addMusicians(Member member) {
        this.musicians.add(member);
        return this;
    }

    public WorshipEvent removeMusicians(Member member) {
        this.musicians.remove(member);
        return this;
    }

    public Set<Member> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<Member> members) {
        this.participants = members;
    }

    public WorshipEvent participants(Set<Member> members) {
        this.setParticipants(members);
        return this;
    }

    public WorshipEvent addParticipants(Member member) {
        this.participants.add(member);
        return this;
    }

    public WorshipEvent removeParticipants(Member member) {
        this.participants.remove(member);
        return this;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setService(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setService(this));
        }
        this.appointments = appointments;
    }

    public WorshipEvent appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public WorshipEvent addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setService(this);
        return this;
    }

    public WorshipEvent removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setService(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorshipEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((WorshipEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorshipEvent{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", guestPreacher='" + getGuestPreacher() + "'" +
            ", description='" + getDescription() + "'" +
            ", callToWorshipText='" + getCallToWorshipText() + "'" +
            ", confessionOfSinText='" + getConfessionOfSinText() + "'" +
            ", assuranceOfPardonText='" + getAssuranceOfPardonText() + "'" +
            ", lordSupperText='" + getLordSupperText() + "'" +
            ", benedictionText='" + getBenedictionText() + "'" +
            ", confessionalText='" + getConfessionalText() + "'" +
            ", sermonText='" + getSermonText() + "'" +
            ", sermonFile='" + getSermonFile() + "'" +
            ", sermonFileContentType='" + getSermonFileContentType() + "'" +
            ", sermonLink='" + getSermonLink() + "'" +
            ", youtubeLink='" + getYoutubeLink() + "'" +
            ", bulletinFile='" + getBulletinFile() + "'" +
            ", bulletinFileContentType='" + getBulletinFileContentType() + "'" +
            ", worshipType='" + getWorshipType() + "'" +
            "}";
    }
}
