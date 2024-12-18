package br.com.poimen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Hymn.
 */
@Entity
@Table(name = "hymn")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hymn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "hymn_number")
    private String hymnNumber;

    @Column(name = "lyrics")
    private String lyrics;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "hymns")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "preacher", "liturgist", "hymns", "musicians" }, allowSetters = true)
    private Set<WorshipEvent> worshipEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hymn id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Hymn title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public Hymn author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHymnNumber() {
        return this.hymnNumber;
    }

    public Hymn hymnNumber(String hymnNumber) {
        this.setHymnNumber(hymnNumber);
        return this;
    }

    public void setHymnNumber(String hymnNumber) {
        this.hymnNumber = hymnNumber;
    }

    public String getLyrics() {
        return this.lyrics;
    }

    public Hymn lyrics(String lyrics) {
        this.setLyrics(lyrics);
        return this;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public Set<WorshipEvent> getWorshipEvents() {
        return this.worshipEvents;
    }

    public void setWorshipEvents(Set<WorshipEvent> worshipEvents) {
        if (this.worshipEvents != null) {
            this.worshipEvents.forEach(i -> i.removeHymn(this));
        }
        if (worshipEvents != null) {
            worshipEvents.forEach(i -> i.addHymn(this));
        }
        this.worshipEvents = worshipEvents;
    }

    public Hymn worshipEvents(Set<WorshipEvent> worshipEvents) {
        this.setWorshipEvents(worshipEvents);
        return this;
    }

    public Hymn addWorshipEvent(WorshipEvent worshipEvent) {
        this.worshipEvents.add(worshipEvent);
        worshipEvent.getHymns().add(this);
        return this;
    }

    public Hymn removeWorshipEvent(WorshipEvent worshipEvent) {
        this.worshipEvents.remove(worshipEvent);
        worshipEvent.getHymns().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hymn)) {
            return false;
        }
        return getId() != null && getId().equals(((Hymn) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hymn{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", author='" + getAuthor() + "'" +
            ", hymnNumber='" + getHymnNumber() + "'" +
            ", lyrics='" + getLyrics() + "'" +
            "}";
    }
}
