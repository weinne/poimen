package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WorshipEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WorshipEvent getWorshipEventSample1() {
        return new WorshipEvent()
            .id(1L)
            .title("title1")
            .guestPreacher("guestPreacher1")
            .callToWorshipText("callToWorshipText1")
            .confessionOfSinText("confessionOfSinText1")
            .assuranceOfPardonText("assuranceOfPardonText1")
            .lordSupperText("lordSupperText1")
            .benedictionText("benedictionText1")
            .confessionalText("confessionalText1")
            .sermonLink("sermonLink1")
            .youtubeLink("youtubeLink1");
    }

    public static WorshipEvent getWorshipEventSample2() {
        return new WorshipEvent()
            .id(2L)
            .title("title2")
            .guestPreacher("guestPreacher2")
            .callToWorshipText("callToWorshipText2")
            .confessionOfSinText("confessionOfSinText2")
            .assuranceOfPardonText("assuranceOfPardonText2")
            .lordSupperText("lordSupperText2")
            .benedictionText("benedictionText2")
            .confessionalText("confessionalText2")
            .sermonLink("sermonLink2")
            .youtubeLink("youtubeLink2");
    }

    public static WorshipEvent getWorshipEventRandomSampleGenerator() {
        return new WorshipEvent()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .guestPreacher(UUID.randomUUID().toString())
            .callToWorshipText(UUID.randomUUID().toString())
            .confessionOfSinText(UUID.randomUUID().toString())
            .assuranceOfPardonText(UUID.randomUUID().toString())
            .lordSupperText(UUID.randomUUID().toString())
            .benedictionText(UUID.randomUUID().toString())
            .confessionalText(UUID.randomUUID().toString())
            .sermonLink(UUID.randomUUID().toString())
            .youtubeLink(UUID.randomUUID().toString());
    }
}
