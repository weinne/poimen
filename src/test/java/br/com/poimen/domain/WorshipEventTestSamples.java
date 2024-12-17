package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WorshipEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WorshipEvent getWorshipEventSample1() {
        return new WorshipEvent().id(1L).title("title1").description("description1");
    }

    public static WorshipEvent getWorshipEventSample2() {
        return new WorshipEvent().id(2L).title("title2").description("description2");
    }

    public static WorshipEvent getWorshipEventRandomSampleGenerator() {
        return new WorshipEvent()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
