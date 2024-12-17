package br.com.poimen.domain;

import static br.com.poimen.domain.HymnTestSamples.*;
import static br.com.poimen.domain.WorshipEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HymnTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hymn.class);
        Hymn hymn1 = getHymnSample1();
        Hymn hymn2 = new Hymn();
        assertThat(hymn1).isNotEqualTo(hymn2);

        hymn2.setId(hymn1.getId());
        assertThat(hymn1).isEqualTo(hymn2);

        hymn2 = getHymnSample2();
        assertThat(hymn1).isNotEqualTo(hymn2);
    }

    @Test
    void worshipEventTest() {
        Hymn hymn = getHymnRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        hymn.addWorshipEvent(worshipEventBack);
        assertThat(hymn.getWorshipEvents()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getHymns()).containsOnly(hymn);

        hymn.removeWorshipEvent(worshipEventBack);
        assertThat(hymn.getWorshipEvents()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getHymns()).doesNotContain(hymn);

        hymn.worshipEvents(new HashSet<>(Set.of(worshipEventBack)));
        assertThat(hymn.getWorshipEvents()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getHymns()).containsOnly(hymn);

        hymn.setWorshipEvents(new HashSet<>());
        assertThat(hymn.getWorshipEvents()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getHymns()).doesNotContain(hymn);
    }
}
