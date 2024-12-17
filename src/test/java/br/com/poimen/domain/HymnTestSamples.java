package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HymnTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Hymn getHymnSample1() {
        return new Hymn().id(1L).title("title1").author("author1").hymnNumber("hymnNumber1").lyrics("lyrics1");
    }

    public static Hymn getHymnSample2() {
        return new Hymn().id(2L).title("title2").author("author2").hymnNumber("hymnNumber2").lyrics("lyrics2");
    }

    public static Hymn getHymnRandomSampleGenerator() {
        return new Hymn()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .author(UUID.randomUUID().toString())
            .hymnNumber(UUID.randomUUID().toString())
            .lyrics(UUID.randomUUID().toString());
    }
}
