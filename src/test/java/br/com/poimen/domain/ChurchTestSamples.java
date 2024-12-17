package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChurchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Church getChurchSample1() {
        return new Church().id(1L).name("name1").cnpj("cnpj1").address("address1").city("city1");
    }

    public static Church getChurchSample2() {
        return new Church().id(2L).name("name2").cnpj("cnpj2").address("address2").city("city2");
    }

    public static Church getChurchRandomSampleGenerator() {
        return new Church()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .cnpj(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString());
    }
}
