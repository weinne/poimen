package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlanSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PlanSubscription getPlanSubscriptionSample1() {
        return new PlanSubscription().id(1L).description("description1").paymentReference("paymentReference1");
    }

    public static PlanSubscription getPlanSubscriptionSample2() {
        return new PlanSubscription().id(2L).description("description2").paymentReference("paymentReference2");
    }

    public static PlanSubscription getPlanSubscriptionRandomSampleGenerator() {
        return new PlanSubscription()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .paymentReference(UUID.randomUUID().toString());
    }
}
