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
@org.springframework.data.elasticsearch.annotations.Document(indexName = "hymn")
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
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Column(name = "lyrics_author")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lyricsAuthor;

    @Column(name = "music_author")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String musicAuthor;

    @Column(name = "hymnary")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String hymnary;

    @Column(name = "hymn_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String hymnNumber;

    @Column(name = "link")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String link;

    @Column(name = "youtube_link")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String youtubeLink;

    @Lob
    @Column(name = "sheet_music")
    private byte[] sheetMusic;

    @Column(name = "sheet_music_content_type")
    private String sheetMusicContentType;

    @Lob
    @Column(name = "midi")
    private byte[] midi;

    @Column(name = "midi_content_type")
    private String midiContentType;

    @Size(max = 5)
    @Column(name = "tone", length = 5)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String tone;

    @Column(name = "lyrics")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lyrics;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "hymns")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "church", "preacher", "liturgist", "hymns", "musicians", "participants", "appointments" },
        allowSetters = true
    )
    private Set<WorshipEvent> services = new HashSet<>();

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

    public String getLyricsAuthor() {
        return this.lyricsAuthor;
    }

    public Hymn lyricsAuthor(String lyricsAuthor) {
        this.setLyricsAuthor(lyricsAuthor);
        return this;
    }

    public void setLyricsAuthor(String lyricsAuthor) {
        this.lyricsAuthor = lyricsAuthor;
    }

    public String getMusicAuthor() {
        return this.musicAuthor;
    }

    public Hymn musicAuthor(String musicAuthor) {
        this.setMusicAuthor(musicAuthor);
        return this;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor;
    }

    public String getHymnary() {
        return this.hymnary;
    }

    public Hymn hymnary(String hymnary) {
        this.setHymnary(hymnary);
        return this;
    }

    public void setHymnary(String hymnary) {
        this.hymnary = hymnary;
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

    public String getLink() {
        return this.link;
    }

    public Hymn link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getYoutubeLink() {
        return this.youtubeLink;
    }

    public Hymn youtubeLink(String youtubeLink) {
        this.setYoutubeLink(youtubeLink);
        return this;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public byte[] getSheetMusic() {
        return this.sheetMusic;
    }

    public Hymn sheetMusic(byte[] sheetMusic) {
        this.setSheetMusic(sheetMusic);
        return this;
    }

    public void setSheetMusic(byte[] sheetMusic) {
        this.sheetMusic = sheetMusic;
    }

    public String getSheetMusicContentType() {
        return this.sheetMusicContentType;
    }

    public Hymn sheetMusicContentType(String sheetMusicContentType) {
        this.sheetMusicContentType = sheetMusicContentType;
        return this;
    }

    public void setSheetMusicContentType(String sheetMusicContentType) {
        this.sheetMusicContentType = sheetMusicContentType;
    }

    public byte[] getMidi() {
        return this.midi;
    }

    public Hymn midi(byte[] midi) {
        this.setMidi(midi);
        return this;
    }

    public void setMidi(byte[] midi) {
        this.midi = midi;
    }

    public String getMidiContentType() {
        return this.midiContentType;
    }

    public Hymn midiContentType(String midiContentType) {
        this.midiContentType = midiContentType;
        return this;
    }

    public void setMidiContentType(String midiContentType) {
        this.midiContentType = midiContentType;
    }

    public String getTone() {
        return this.tone;
    }

    public Hymn tone(String tone) {
        this.setTone(tone);
        return this;
    }

    public void setTone(String tone) {
        this.tone = tone;
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

    public Set<WorshipEvent> getServices() {
        return this.services;
    }

    public void setServices(Set<WorshipEvent> worshipEvents) {
        if (this.services != null) {
            this.services.forEach(i -> i.removeHymns(this));
        }
        if (worshipEvents != null) {
            worshipEvents.forEach(i -> i.addHymns(this));
        }
        this.services = worshipEvents;
    }

    public Hymn services(Set<WorshipEvent> worshipEvents) {
        this.setServices(worshipEvents);
        return this;
    }

    public Hymn addServices(WorshipEvent worshipEvent) {
        this.services.add(worshipEvent);
        worshipEvent.getHymns().add(this);
        return this;
    }

    public Hymn removeServices(WorshipEvent worshipEvent) {
        this.services.remove(worshipEvent);
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
            ", lyricsAuthor='" + getLyricsAuthor() + "'" +
            ", musicAuthor='" + getMusicAuthor() + "'" +
            ", hymnary='" + getHymnary() + "'" +
            ", hymnNumber='" + getHymnNumber() + "'" +
            ", link='" + getLink() + "'" +
            ", youtubeLink='" + getYoutubeLink() + "'" +
            ", sheetMusic='" + getSheetMusic() + "'" +
            ", sheetMusicContentType='" + getSheetMusicContentType() + "'" +
            ", midi='" + getMidi() + "'" +
            ", midiContentType='" + getMidiContentType() + "'" +
            ", tone='" + getTone() + "'" +
            ", lyrics='" + getLyrics() + "'" +
            "}";
    }
}
