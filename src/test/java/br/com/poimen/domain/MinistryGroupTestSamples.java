package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MinistryGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MinistryGroup getMinistryGroupSample1() {
        return new MinistryGroup().id(1L).name("name1").description("description1");
    }

    public static MinistryGroup getMinistryGroupSample2() {
        return new MinistryGroup().id(2L).name("name2").description("description2");
    }

    public static MinistryGroup getMinistryGroupRandomSampleGenerator() {
        return new MinistryGroup()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
