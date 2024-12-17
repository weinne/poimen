package br.com.poimen.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MinistryMembershipTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MinistryMembership getMinistryMembershipSample1() {
        return new MinistryMembership().id(1L);
    }

    public static MinistryMembership getMinistryMembershipSample2() {
        return new MinistryMembership().id(2L);
    }

    public static MinistryMembership getMinistryMembershipRandomSampleGenerator() {
        return new MinistryMembership().id(longCount.incrementAndGet());
    }
}
