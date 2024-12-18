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
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "worship_type", nullable = false)
    private WorshipType worshipType;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "church", "counselingSessions", "ministryMemberships", "tasks", "transactions", "schedules", "worshipEvents" },
        allowSetters = true
    )
    private Member preacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "church", "counselingSessions", "ministryMemberships", "tasks", "transactions", "schedules", "worshipEvents" },
        allowSetters = true
    )
    private Member liturgist;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_worship_event__hymn",
        joinColumns = @JoinColumn(name = "worship_event_id"),
        inverseJoinColumns = @JoinColumn(name = "hymn_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "worshipEvents" }, allowSetters = true)
    private Set<Hymn> hymns = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_worship_event__musicians",
        joinColumns = @JoinColumn(name = "worship_event_id"),
        inverseJoinColumns = @JoinColumn(name = "musicians_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "church", "counselingSessions", "ministryMemberships", "tasks", "transactions", "schedules", "worshipEvents" },
        allowSetters = true
    )
    private Set<Member> musicians = new HashSet<>();

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

    public WorshipEvent addHymn(Hymn hymn) {
        this.hymns.add(hymn);
        return this;
    }

    public WorshipEvent removeHymn(Hymn hymn) {
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
            ", description='" + getDescription() + "'" +
            ", worshipType='" + getWorshipType() + "'" +
            "}";
    }
}
