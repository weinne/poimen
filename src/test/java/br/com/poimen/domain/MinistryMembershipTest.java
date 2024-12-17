package br.com.poimen.domain;

import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.MinistryGroupTestSamples.*;
import static br.com.poimen.domain.MinistryMembershipTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MinistryMembershipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MinistryMembership.class);
        MinistryMembership ministryMembership1 = getMinistryMembershipSample1();
        MinistryMembership ministryMembership2 = new MinistryMembership();
        assertThat(ministryMembership1).isNotEqualTo(ministryMembership2);

        ministryMembership2.setId(ministryMembership1.getId());
        assertThat(ministryMembership1).isEqualTo(ministryMembership2);

        ministryMembership2 = getMinistryMembershipSample2();
        assertThat(ministryMembership1).isNotEqualTo(ministryMembership2);
    }

    @Test
    void ministryGroupTest() {
        MinistryMembership ministryMembership = getMinistryMembershipRandomSampleGenerator();
        MinistryGroup ministryGroupBack = getMinistryGroupRandomSampleGenerator();

        ministryMembership.setMinistryGroup(ministryGroupBack);
        assertThat(ministryMembership.getMinistryGroup()).isEqualTo(ministryGroupBack);

        ministryMembership.ministryGroup(null);
        assertThat(ministryMembership.getMinistryGroup()).isNull();
    }

    @Test
    void memberTest() {
        MinistryMembership ministryMembership = getMinistryMembershipRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        ministryMembership.setMember(memberBack);
        assertThat(ministryMembership.getMember()).isEqualTo(memberBack);

        ministryMembership.member(null);
        assertThat(ministryMembership.getMember()).isNull();
    }
}
