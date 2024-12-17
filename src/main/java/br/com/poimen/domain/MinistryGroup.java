package br.com.poimen.domain;

import br.com.poimen.domain.enumeration.GroupType;
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
 * A MinistryGroup.
 */
@Entity
@Table(name = "ministry_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "established_date")
    private Instant establishedDate;

    @Column(name = "leader")
    private String leader;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GroupType type;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ministryGroup")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ministryGroup", "member" }, allowSetters = true)
    private Set<MinistryMembership> ministryMemberships = new HashSet<>();

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

    public Instant getEstablishedDate() {
        return this.establishedDate;
    }

    public MinistryGroup establishedDate(Instant establishedDate) {
        this.setEstablishedDate(establishedDate);
        return this;
    }

    public void setEstablishedDate(Instant establishedDate) {
        this.establishedDate = establishedDate;
    }

    public String getLeader() {
        return this.leader;
    }

    public MinistryGroup leader(String leader) {
        this.setLeader(leader);
        return this;
    }

    public void setLeader(String leader) {
        this.leader = leader;
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

    public Set<MinistryMembership> getMinistryMemberships() {
        return this.ministryMemberships;
    }

    public void setMinistryMemberships(Set<MinistryMembership> ministryMemberships) {
        if (this.ministryMemberships != null) {
            this.ministryMemberships.forEach(i -> i.setMinistryGroup(null));
        }
        if (ministryMemberships != null) {
            ministryMemberships.forEach(i -> i.setMinistryGroup(this));
        }
        this.ministryMemberships = ministryMemberships;
    }

    public MinistryGroup ministryMemberships(Set<MinistryMembership> ministryMemberships) {
        this.setMinistryMemberships(ministryMemberships);
        return this;
    }

    public MinistryGroup addMinistryMembership(MinistryMembership ministryMembership) {
        this.ministryMemberships.add(ministryMembership);
        ministryMembership.setMinistryGroup(this);
        return this;
    }

    public MinistryGroup removeMinistryMembership(MinistryMembership ministryMembership) {
        this.ministryMemberships.remove(ministryMembership);
        ministryMembership.setMinistryGroup(null);
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
            ", leader='" + getLeader() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
