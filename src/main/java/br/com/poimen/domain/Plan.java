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
 * A Plan.
 */
@Entity
@Table(name = "plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Plan implements Serializable {

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
    @Column(name = "price", nullable = false)
    private String price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "plan", "church", "user" }, allowSetters = true)
    private Set<PlanSubscription> planSubscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Plan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    public Plan price(String price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Set<PlanSubscription> getPlanSubscriptions() {
        return this.planSubscriptions;
    }

    public void setPlanSubscriptions(Set<PlanSubscription> planSubscriptions) {
        if (this.planSubscriptions != null) {
            this.planSubscriptions.forEach(i -> i.setPlan(null));
        }
        if (planSubscriptions != null) {
            planSubscriptions.forEach(i -> i.setPlan(this));
        }
        this.planSubscriptions = planSubscriptions;
    }

    public Plan planSubscriptions(Set<PlanSubscription> planSubscriptions) {
        this.setPlanSubscriptions(planSubscriptions);
        return this;
    }

    public Plan addPlanSubscription(PlanSubscription planSubscription) {
        this.planSubscriptions.add(planSubscription);
        planSubscription.setPlan(this);
        return this;
    }

    public Plan removePlanSubscription(PlanSubscription planSubscription) {
        this.planSubscriptions.remove(planSubscription);
        planSubscription.setPlan(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plan)) {
            return false;
        }
        return getId() != null && getId().equals(((Plan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            "}";
    }
}
