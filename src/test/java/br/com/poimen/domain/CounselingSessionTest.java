package br.com.poimen.domain;

import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.CounselingSessionTestSamples.*;
import static br.com.poimen.domain.MemberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CounselingSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CounselingSession.class);
        CounselingSession counselingSession1 = getCounselingSessionSample1();
        CounselingSession counselingSession2 = new CounselingSession();
        assertThat(counselingSession1).isNotEqualTo(counselingSession2);

        counselingSession2.setId(counselingSession1.getId());
        assertThat(counselingSession1).isEqualTo(counselingSession2);

        counselingSession2 = getCounselingSessionSample2();
        assertThat(counselingSession1).isNotEqualTo(counselingSession2);
    }

    @Test
    void churchTest() {
        CounselingSession counselingSession = getCounselingSessionRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        counselingSession.setChurch(churchBack);
        assertThat(counselingSession.getChurch()).isEqualTo(churchBack);

        counselingSession.church(null);
        assertThat(counselingSession.getChurch()).isNull();
    }

    @Test
    void memberTest() {
        CounselingSession counselingSession = getCounselingSessionRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        counselingSession.setMember(memberBack);
        assertThat(counselingSession.getMember()).isEqualTo(memberBack);

        counselingSession.member(null);
        assertThat(counselingSession.getMember()).isNull();
    }
}
