package br.com.poimen.domain;

import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.InvoiceTestSamples.*;
import static br.com.poimen.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void churchTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        invoice.setChurch(churchBack);
        assertThat(invoice.getChurch()).isEqualTo(churchBack);

        invoice.church(null);
        assertThat(invoice.getChurch()).isNull();
    }

    @Test
    void transactionTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        invoice.setTransaction(transactionBack);
        assertThat(invoice.getTransaction()).isEqualTo(transactionBack);

        invoice.transaction(null);
        assertThat(invoice.getTransaction()).isNull();
    }
}
