package br.com.poimen.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CounselingSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CounselingSession getCounselingSessionSample1() {
        return new CounselingSession().id(1L);
    }

    public static CounselingSession getCounselingSessionSample2() {
        return new CounselingSession().id(2L);
    }

    public static CounselingSession getCounselingSessionRandomSampleGenerator() {
        return new CounselingSession().id(longCount.incrementAndGet());
    }
}
