package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CounselingSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CounselingSession getCounselingSessionSample1() {
        return new CounselingSession().id(1L).subject("subject1");
    }

    public static CounselingSession getCounselingSessionSample2() {
        return new CounselingSession().id(2L).subject("subject2");
    }

    public static CounselingSession getCounselingSessionRandomSampleGenerator() {
        return new CounselingSession().id(longCount.incrementAndGet()).subject(UUID.randomUUID().toString());
    }
}
