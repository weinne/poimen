package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Plan getPlanSample1() {
        return new Plan().id(1L).name("name1").price("price1").description("description1").renewalPeriod("renewalPeriod1");
    }

    public static Plan getPlanSample2() {
        return new Plan().id(2L).name("name2").price("price2").description("description2").renewalPeriod("renewalPeriod2");
    }

    public static Plan getPlanRandomSampleGenerator() {
        return new Plan()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .price(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .renewalPeriod(UUID.randomUUID().toString());
    }
}
