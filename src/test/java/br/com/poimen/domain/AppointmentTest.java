package br.com.poimen.domain;

import static br.com.poimen.domain.ApplicationUserTestSamples.*;
import static br.com.poimen.domain.AppointmentTestSamples.*;
import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.CounselingSessionTestSamples.*;
import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.MinistryGroupTestSamples.*;
import static br.com.poimen.domain.WorshipEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointment.class);
        Appointment appointment1 = getAppointmentSample1();
        Appointment appointment2 = new Appointment();
        assertThat(appointment1).isNotEqualTo(appointment2);

        appointment2.setId(appointment1.getId());
        assertThat(appointment1).isEqualTo(appointment2);

        appointment2 = getAppointmentSample2();
        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    @Test
    void churchTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        appointment.setChurch(churchBack);
        assertThat(appointment.getChurch()).isEqualTo(churchBack);

        appointment.church(null);
        assertThat(appointment.getChurch()).isNull();
    }

    @Test
    void memberTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        appointment.setMember(memberBack);
        assertThat(appointment.getMember()).isEqualTo(memberBack);

        appointment.member(null);
        assertThat(appointment.getMember()).isNull();
    }

    @Test
    void serviceTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        appointment.setService(worshipEventBack);
        assertThat(appointment.getService()).isEqualTo(worshipEventBack);

        appointment.service(null);
        assertThat(appointment.getService()).isNull();
    }

    @Test
    void groupTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        MinistryGroup ministryGroupBack = getMinistryGroupRandomSampleGenerator();

        appointment.setGroup(ministryGroupBack);
        assertThat(appointment.getGroup()).isEqualTo(ministryGroupBack);

        appointment.group(null);
        assertThat(appointment.getGroup()).isNull();
    }

    @Test
    void counselingSessionTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        CounselingSession counselingSessionBack = getCounselingSessionRandomSampleGenerator();

        appointment.setCounselingSession(counselingSessionBack);
        assertThat(appointment.getCounselingSession()).isEqualTo(counselingSessionBack);

        appointment.counselingSession(null);
        assertThat(appointment.getCounselingSession()).isNull();
    }

    @Test
    void userTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        appointment.setUser(applicationUserBack);
        assertThat(appointment.getUser()).isEqualTo(applicationUserBack);

        appointment.user(null);
        assertThat(appointment.getUser()).isNull();
    }
}
