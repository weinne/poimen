package br.com.poimen.domain;

import br.com.poimen.domain.enumeration.RoleMinistry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MinistryMembership.
 */
@Entity
@Table(name = "ministry_membership")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MinistryMembership implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleMinistry role;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "church", "ministryMemberships" }, allowSetters = true)
    private MinistryGroup ministryGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "church", "counselingSessions", "ministryMemberships", "tasks", "transactions", "schedules", "worshipEvents" },
        allowSetters = true
    )
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MinistryMembership id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleMinistry getRole() {
        return this.role;
    }

    public MinistryMembership role(RoleMinistry role) {
        this.setRole(role);
        return this;
    }

    public void setRole(RoleMinistry role) {
        this.role = role;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public MinistryMembership startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public MinistryMembership endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public MinistryMembership status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public MinistryGroup getMinistryGroup() {
        return this.ministryGroup;
    }

    public void setMinistryGroup(MinistryGroup ministryGroup) {
        this.ministryGroup = ministryGroup;
    }

    public MinistryMembership ministryGroup(MinistryGroup ministryGroup) {
        this.setMinistryGroup(ministryGroup);
        return this;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MinistryMembership member(Member member) {
        this.setMember(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MinistryMembership)) {
            return false;
        }
        return getId() != null && getId().equals(((MinistryMembership) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MinistryMembership{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
