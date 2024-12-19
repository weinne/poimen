package br.com.poimen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "invoice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String number;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private Instant issueDate;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String type;

    @Column(name = "supplier")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String supplier;

    @Lob
    @Column(name = "invoice_file")
    private byte[] invoiceFile;

    @Column(name = "invoice_file_content_type")
    private String invoiceFileContentType;

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
    @JsonIgnoreProperties(value = { "invoices", "church", "member", "user" }, allowSetters = true)
    private Transaction transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public Invoice number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Instant getIssueDate() {
        return this.issueDate;
    }

    public Invoice issueDate(Instant issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Invoice totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getType() {
        return this.type;
    }

    public Invoice type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public Invoice supplier(String supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public byte[] getInvoiceFile() {
        return this.invoiceFile;
    }

    public Invoice invoiceFile(byte[] invoiceFile) {
        this.setInvoiceFile(invoiceFile);
        return this;
    }

    public void setInvoiceFile(byte[] invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    public String getInvoiceFileContentType() {
        return this.invoiceFileContentType;
    }

    public Invoice invoiceFileContentType(String invoiceFileContentType) {
        this.invoiceFileContentType = invoiceFileContentType;
        return this;
    }

    public void setInvoiceFileContentType(String invoiceFileContentType) {
        this.invoiceFileContentType = invoiceFileContentType;
    }

    public Church getChurch() {
        return this.church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Invoice church(Church church) {
        this.setChurch(church);
        return this;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Invoice transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", type='" + getType() + "'" +
            ", supplier='" + getSupplier() + "'" +
            ", invoiceFile='" + getInvoiceFile() + "'" +
            ", invoiceFileContentType='" + getInvoiceFileContentType() + "'" +
            "}";
    }
}
