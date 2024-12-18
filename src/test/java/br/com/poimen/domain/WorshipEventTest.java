package br.com.poimen.domain;

import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.HymnTestSamples.*;
import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.WorshipEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WorshipEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorshipEvent.class);
        WorshipEvent worshipEvent1 = getWorshipEventSample1();
        WorshipEvent worshipEvent2 = new WorshipEvent();
        assertThat(worshipEvent1).isNotEqualTo(worshipEvent2);

        worshipEvent2.setId(worshipEvent1.getId());
        assertThat(worshipEvent1).isEqualTo(worshipEvent2);

        worshipEvent2 = getWorshipEventSample2();
        assertThat(worshipEvent1).isNotEqualTo(worshipEvent2);
    }

    @Test
    void churchTest() {
        WorshipEvent worshipEvent = getWorshipEventRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        worshipEvent.setChurch(churchBack);
        assertThat(worshipEvent.getChurch()).isEqualTo(churchBack);

        worshipEvent.church(null);
        assertThat(worshipEvent.getChurch()).isNull();
    }

    @Test
    void preacherTest() {
        WorshipEvent worshipEvent = getWorshipEventRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        worshipEvent.setPreacher(memberBack);
        assertThat(worshipEvent.getPreacher()).isEqualTo(memberBack);

        worshipEvent.preacher(null);
        assertThat(worshipEvent.getPreacher()).isNull();
    }

    @Test
    void liturgistTest() {
        WorshipEvent worshipEvent = getWorshipEventRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        worshipEvent.setLiturgist(memberBack);
        assertThat(worshipEvent.getLiturgist()).isEqualTo(memberBack);

        worshipEvent.liturgist(null);
        assertThat(worshipEvent.getLiturgist()).isNull();
    }

    @Test
    void hymnTest() {
        WorshipEvent worshipEvent = getWorshipEventRandomSampleGenerator();
        Hymn hymnBack = getHymnRandomSampleGenerator();

        worshipEvent.addHymn(hymnBack);
        assertThat(worshipEvent.getHymns()).containsOnly(hymnBack);

        worshipEvent.removeHymn(hymnBack);
        assertThat(worshipEvent.getHymns()).doesNotContain(hymnBack);

        worshipEvent.hymns(new HashSet<>(Set.of(hymnBack)));
        assertThat(worshipEvent.getHymns()).containsOnly(hymnBack);

        worshipEvent.setHymns(new HashSet<>());
        assertThat(worshipEvent.getHymns()).doesNotContain(hymnBack);
    }

    @Test
    void musiciansTest() {
        WorshipEvent worshipEvent = getWorshipEventRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        worshipEvent.addMusicians(memberBack);
        assertThat(worshipEvent.getMusicians()).containsOnly(memberBack);

        worshipEvent.removeMusicians(memberBack);
        assertThat(worshipEvent.getMusicians()).doesNotContain(memberBack);

        worshipEvent.musicians(new HashSet<>(Set.of(memberBack)));
        assertThat(worshipEvent.getMusicians()).containsOnly(memberBack);

        worshipEvent.setMusicians(new HashSet<>());
        assertThat(worshipEvent.getMusicians()).doesNotContain(memberBack);
    }
}
