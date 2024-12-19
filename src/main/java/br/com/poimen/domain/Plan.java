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
@org.springframework.data.elasticsearch.annotations.Document(indexName = "plan")
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
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String price;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Lob
    @Column(name = "features")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String features;

    @Column(name = "renewal_period")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String renewalPeriod;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "church", "plan", "user" }, allowSetters = true)
    private Set<PlanSubscription> subscriptions = new HashSet<>();

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

    public String getDescription() {
        return this.description;
    }

    public Plan description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatures() {
        return this.features;
    }

    public Plan features(String features) {
        this.setFeatures(features);
        return this;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getRenewalPeriod() {
        return this.renewalPeriod;
    }

    public Plan renewalPeriod(String renewalPeriod) {
        this.setRenewalPeriod(renewalPeriod);
        return this;
    }

    public void setRenewalPeriod(String renewalPeriod) {
        this.renewalPeriod = renewalPeriod;
    }

    public Set<PlanSubscription> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(Set<PlanSubscription> planSubscriptions) {
        if (this.subscriptions != null) {
            this.subscriptions.forEach(i -> i.setPlan(null));
        }
        if (planSubscriptions != null) {
            planSubscriptions.forEach(i -> i.setPlan(this));
        }
        this.subscriptions = planSubscriptions;
    }

    public Plan subscriptions(Set<PlanSubscription> planSubscriptions) {
        this.setSubscriptions(planSubscriptions);
        return this;
    }

    public Plan addSubscription(PlanSubscription planSubscription) {
        this.subscriptions.add(planSubscription);
        planSubscription.setPlan(this);
        return this;
    }

    public Plan removeSubscription(PlanSubscription planSubscription) {
        this.subscriptions.remove(planSubscription);
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
            ", description='" + getDescription() + "'" +
            ", features='" + getFeatures() + "'" +
            ", renewalPeriod='" + getRenewalPeriod() + "'" +
            "}";
    }
}
