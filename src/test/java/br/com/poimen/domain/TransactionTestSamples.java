package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transaction getTransactionSample1() {
        return new Transaction()
            .id(1L)
            .description("description1")
            .paymentMethod("paymentMethod1")
            .type("type1")
            .supplierOrClient("supplierOrClient1")
            .invoiceFile("invoiceFile1");
    }

    public static Transaction getTransactionSample2() {
        return new Transaction()
            .id(2L)
            .description("description2")
            .paymentMethod("paymentMethod2")
            .type("type2")
            .supplierOrClient("supplierOrClient2")
            .invoiceFile("invoiceFile2");
    }

    public static Transaction getTransactionRandomSampleGenerator() {
        return new Transaction()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .paymentMethod(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .supplierOrClient(UUID.randomUUID().toString())
            .invoiceFile(UUID.randomUUID().toString());
    }
}
