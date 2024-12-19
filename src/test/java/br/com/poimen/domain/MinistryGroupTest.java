package br.com.poimen.domain;

import static br.com.poimen.domain.AppointmentTestSamples.*;
import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.MinistryGroupTestSamples.*;
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
    void presidentTest() {
        MinistryGroup ministryGroup = getMinistryGroupRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        ministryGroup.setPresident(memberBack);
        assertThat(ministryGroup.getPresident()).isEqualTo(memberBack);

        ministryGroup.president(null);
        assertThat(ministryGroup.getPresident()).isNull();
    }

    @Test
    void supervisorTest() {
        MinistryGroup ministryGroup = getMinistryGroupRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        ministryGroup.setSupervisor(memberBack);
        assertThat(ministryGroup.getSupervisor()).isEqualTo(memberBack);

        ministryGroup.supervisor(null);
        assertThat(ministryGroup.getSupervisor()).isNull();
    }

    @Test
    void membersTest() {
        MinistryGroup ministryGroup = getMinistryGroupRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        ministryGroup.addMembers(memberBack);
        assertThat(ministryGroup.getMembers()).containsOnly(memberBack);

        ministryGroup.removeMembers(memberBack);
        assertThat(ministryGroup.getMembers()).doesNotContain(memberBack);

        ministryGroup.members(new HashSet<>(Set.of(memberBack)));
        assertThat(ministryGroup.getMembers()).containsOnly(memberBack);

        ministryGroup.setMembers(new HashSet<>());
        assertThat(ministryGroup.getMembers()).doesNotContain(memberBack);
    }

    @Test
    void appointmentTest() {
        MinistryGroup ministryGroup = getMinistryGroupRandomSampleGenerator();
        Appointment appointmentBack = getAppointmentRandomSampleGenerator();

        ministryGroup.addAppointment(appointmentBack);
        assertThat(ministryGroup.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getGroup()).isEqualTo(ministryGroup);

        ministryGroup.removeAppointment(appointmentBack);
        assertThat(ministryGroup.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getGroup()).isNull();

        ministryGroup.appointments(new HashSet<>(Set.of(appointmentBack)));
        assertThat(ministryGroup.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getGroup()).isEqualTo(ministryGroup);

        ministryGroup.setAppointments(new HashSet<>());
        assertThat(ministryGroup.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getGroup()).isNull();
    }
}
