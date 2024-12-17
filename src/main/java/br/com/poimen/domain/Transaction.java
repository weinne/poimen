package br.com.poimen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "payment_method")
    private String paymentMethod;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "supplier_or_client")
    private String supplierOrClient;

    @Column(name = "invoice_file")
    private String invoiceFile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transaction")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "church", "transaction" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

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
        value = { "church", "counselingSessions", "ministryMemberships", "tasks", "transactions", "schedules" },
        allowSetters = true
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Transaction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Transaction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getDate() {
        return this.date;
    }

    public Transaction date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public Transaction paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getType() {
        return this.type;
    }

    public Transaction type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSupplierOrClient() {
        return this.supplierOrClient;
    }

    public Transaction supplierOrClient(String supplierOrClient) {
        this.setSupplierOrClient(supplierOrClient);
        return this;
    }

    public void setSupplierOrClient(String supplierOrClient) {
        this.supplierOrClient = supplierOrClient;
    }

    public String getInvoiceFile() {
        return this.invoiceFile;
    }

    public Transaction invoiceFile(String invoiceFile) {
        this.setInvoiceFile(invoiceFile);
        return this;
    }

    public void setInvoiceFile(String invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setTransaction(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setTransaction(this));
        }
        this.invoices = invoices;
    }

    public Transaction invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Transaction addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setTransaction(this);
        return this;
    }

    public Transaction removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setTransaction(null);
        return this;
    }

    public Church getChurch() {
        return this.church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Transaction church(Church church) {
        this.setChurch(church);
        return this;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Transaction member(Member member) {
        this.setMember(member);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Transaction user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return getId() != null && getId().equals(((Transaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", type='" + getType() + "'" +
            ", supplierOrClient='" + getSupplierOrClient() + "'" +
            ", invoiceFile='" + getInvoiceFile() + "'" +
            "}";
    }
}
