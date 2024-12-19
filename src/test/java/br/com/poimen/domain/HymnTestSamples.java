package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HymnTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Hymn getHymnSample1() {
        return new Hymn()
            .id(1L)
            .title("title1")
            .lyricsAuthor("lyricsAuthor1")
            .musicAuthor("musicAuthor1")
            .hymnary("hymnary1")
            .hymnNumber("hymnNumber1")
            .link("link1")
            .youtubeLink("youtubeLink1")
            .tone("tone1")
            .lyrics("lyrics1");
    }

    public static Hymn getHymnSample2() {
        return new Hymn()
            .id(2L)
            .title("title2")
            .lyricsAuthor("lyricsAuthor2")
            .musicAuthor("musicAuthor2")
            .hymnary("hymnary2")
            .hymnNumber("hymnNumber2")
            .link("link2")
            .youtubeLink("youtubeLink2")
            .tone("tone2")
            .lyrics("lyrics2");
    }

    public static Hymn getHymnRandomSampleGenerator() {
        return new Hymn()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .lyricsAuthor(UUID.randomUUID().toString())
            .musicAuthor(UUID.randomUUID().toString())
            .hymnary(UUID.randomUUID().toString())
            .hymnNumber(UUID.randomUUID().toString())
            .link(UUID.randomUUID().toString())
            .youtubeLink(UUID.randomUUID().toString())
            .tone(UUID.randomUUID().toString())
            .lyrics(UUID.randomUUID().toString());
    }
}
