package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChurchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Church getChurchSample1() {
        return new Church()
            .id(1L)
            .name("name1")
            .cnpj("cnpj1")
            .address("address1")
            .city("city1")
            .phone("phone1")
            .email("email1")
            .website("website1")
            .facebook("facebook1")
            .instagram("instagram1")
            .twitter("twitter1")
            .youtube("youtube1");
    }

    public static Church getChurchSample2() {
        return new Church()
            .id(2L)
            .name("name2")
            .cnpj("cnpj2")
            .address("address2")
            .city("city2")
            .phone("phone2")
            .email("email2")
            .website("website2")
            .facebook("facebook2")
            .instagram("instagram2")
            .twitter("twitter2")
            .youtube("youtube2");
    }

    public static Church getChurchRandomSampleGenerator() {
        return new Church()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .cnpj(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .facebook(UUID.randomUUID().toString())
            .instagram(UUID.randomUUID().toString())
            .twitter(UUID.randomUUID().toString())
            .youtube(UUID.randomUUID().toString());
    }
}
