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
    void servicesTest() {
        Hymn hymn = getHymnRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        hymn.addServices(worshipEventBack);
        assertThat(hymn.getServices()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getHymns()).containsOnly(hymn);

        hymn.removeServices(worshipEventBack);
        assertThat(hymn.getServices()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getHymns()).doesNotContain(hymn);

        hymn.services(new HashSet<>(Set.of(worshipEventBack)));
        assertThat(hymn.getServices()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getHymns()).containsOnly(hymn);

        hymn.setServices(new HashSet<>());
        assertThat(hymn.getServices()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getHymns()).doesNotContain(hymn);
    }
}
