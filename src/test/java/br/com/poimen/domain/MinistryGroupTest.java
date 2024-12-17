package br.com.poimen.domain;

import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.MinistryGroupTestSamples.*;
import static br.com.poimen.domain.MinistryMembershipTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MinistryGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MinistryGroup.class);
        MinistryGroup ministryGroup1 = getMinistryGroupSample1();
        MinistryGroup ministryGroup2 = new MinistryGroup();
        assertThat(ministryGroup1).isNotEqualTo(ministryGroup2);

        ministryGroup2.setId(ministryGroup1.getId());
        assertThat(ministryGroup1).isEqualTo(ministryGroup2);

        ministryGroup2 = getMinistryGroupSample2();
        assertThat(ministryGroup1).isNotEqualTo(ministryGroup2);
    }

    @Test
    void churchTest() {
        MinistryGroup ministryGroup = getMinistryGroupRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        ministryGroup.setChurch(churchBack);
        assertThat(ministryGroup.getChurch()).isEqualTo(churchBack);

        ministryGroup.church(null);
        assertThat(ministryGroup.getChurch()).isNull();
    }

    @Test
    void ministryMembershipTest() {
        MinistryGroup ministryGroup = getMinistryGroupRandomSampleGenerator();
        MinistryMembership ministryMembershipBack = getMinistryMembershipRandomSampleGenerator();

        ministryGroup.addMinistryMembership(ministryMembershipBack);
        assertThat(ministryGroup.getMinistryMemberships()).containsOnly(ministryMembershipBack);
        assertThat(ministryMembershipBack.getMinistryGroup()).isEqualTo(ministryGroup);

        ministryGroup.removeMinistryMembership(ministryMembershipBack);
        assertThat(ministryGroup.getMinistryMemberships()).doesNotContain(ministryMembershipBack);
        assertThat(ministryMembershipBack.getMinistryGroup()).isNull();

        ministryGroup.ministryMemberships(new HashSet<>(Set.of(ministryMembershipBack)));
        assertThat(ministryGroup.getMinistryMemberships()).containsOnly(ministryMembershipBack);
        assertThat(ministryMembershipBack.getMinistryGroup()).isEqualTo(ministryGroup);

        ministryGroup.setMinistryMemberships(new HashSet<>());
        assertThat(ministryGroup.getMinistryMemberships()).doesNotContain(ministryMembershipBack);
        assertThat(ministryMembershipBack.getMinistryGroup()).isNull();
    }
}
