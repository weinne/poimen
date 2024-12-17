package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Invoice getInvoiceSample1() {
        return new Invoice().id(1L).number("number1").type("type1").supplier("supplier1").invoiceFile("invoiceFile1");
    }

    public static Invoice getInvoiceSample2() {
        return new Invoice().id(2L).number("number2").type("type2").supplier("supplier2").invoiceFile("invoiceFile2");
    }

    public static Invoice getInvoiceRandomSampleGenerator() {
        return new Invoice()
            .id(longCount.incrementAndGet())
            .number(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .supplier(UUID.randomUUID().toString())
            .invoiceFile(UUID.randomUUID().toString());
    }
}
