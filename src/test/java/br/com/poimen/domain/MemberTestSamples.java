package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Member getMemberSample1() {
        return new Member()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .address("address1");
    }

    public static Member getMemberSample2() {
        return new Member()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .address("address2");
    }

    public static Member getMemberRandomSampleGenerator() {
        return new Member()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString());
    }
}
