package br.com.poimen.domain;

import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.InvoiceTestSamples.*;
import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = getTransactionSample1();
        Transaction transaction2 = new Transaction();
        assertThat(transaction1).isNotEqualTo(transaction2);

        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);

        transaction2 = getTransactionSample2();
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void invoiceTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        transaction.addInvoice(invoiceBack);
        assertThat(transaction.getInvoices()).containsOnly(invoiceBack);
        assertThat(invoiceBack.getTransaction()).isEqualTo(transaction);

        transaction.removeInvoice(invoiceBack);
        assertThat(transaction.getInvoices()).doesNotContain(invoiceBack);
        assertThat(invoiceBack.getTransaction()).isNull();

        transaction.invoices(new HashSet<>(Set.of(invoiceBack)));
        assertThat(transaction.getInvoices()).containsOnly(invoiceBack);
        assertThat(invoiceBack.getTransaction()).isEqualTo(transaction);

        transaction.setInvoices(new HashSet<>());
        assertThat(transaction.getInvoices()).doesNotContain(invoiceBack);
        assertThat(invoiceBack.getTransaction()).isNull();
    }

    @Test
    void churchTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        transaction.setChurch(churchBack);
        assertThat(transaction.getChurch()).isEqualTo(churchBack);

        transaction.church(null);
        assertThat(transaction.getChurch()).isNull();
    }

    @Test
    void memberTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        transaction.setMember(memberBack);
        assertThat(transaction.getMember()).isEqualTo(memberBack);

        transaction.member(null);
        assertThat(transaction.getMember()).isNull();
    }
}
