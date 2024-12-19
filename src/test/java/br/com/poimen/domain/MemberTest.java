package br.com.poimen.domain;

import static br.com.poimen.domain.AppointmentTestSamples.*;
import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.CounselingSessionTestSamples.*;
import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.MinistryGroupTestSamples.*;
import static br.com.poimen.domain.TaskTestSamples.*;
import static br.com.poimen.domain.WorshipEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Member.class);
        Member member1 = getMemberSample1();
        Member member2 = new Member();
        assertThat(member1).isNotEqualTo(member2);

        member2.setId(member1.getId());
        assertThat(member1).isEqualTo(member2);

        member2 = getMemberSample2();
        assertThat(member1).isNotEqualTo(member2);
    }

    @Test
    void churchTest() {
        Member member = getMemberRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        member.setChurch(churchBack);
        assertThat(member.getChurch()).isEqualTo(churchBack);

        member.church(null);
        assertThat(member.getChurch()).isNull();
    }

    @Test
    void counselingTest() {
        Member member = getMemberRandomSampleGenerator();
        CounselingSession counselingSessionBack = getCounselingSessionRandomSampleGenerator();

        member.addCounseling(counselingSessionBack);
        assertThat(member.getCounselings()).containsOnly(counselingSessionBack);
        assertThat(counselingSessionBack.getMember()).isEqualTo(member);

        member.removeCounseling(counselingSessionBack);
        assertThat(member.getCounselings()).doesNotContain(counselingSessionBack);
        assertThat(counselingSessionBack.getMember()).isNull();

        member.counselings(new HashSet<>(Set.of(counselingSessionBack)));
        assertThat(member.getCounselings()).containsOnly(counselingSessionBack);
        assertThat(counselingSessionBack.getMember()).isEqualTo(member);

        member.setCounselings(new HashSet<>());
        assertThat(member.getCounselings()).doesNotContain(counselingSessionBack);
        assertThat(counselingSessionBack.getMember()).isNull();
    }

    @Test
    void tasksTest() {
        Member member = getMemberRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        member.addTasks(taskBack);
        assertThat(member.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getMember()).isEqualTo(member);

        member.removeTasks(taskBack);
        assertThat(member.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getMember()).isNull();

        member.tasks(new HashSet<>(Set.of(taskBack)));
        assertThat(member.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getMember()).isEqualTo(member);

        member.setTasks(new HashSet<>());
        assertThat(member.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getMember()).isNull();
    }

    @Test
    void preachInTest() {
        Member member = getMemberRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        member.addPreachIn(worshipEventBack);
        assertThat(member.getPreachIns()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getPreacher()).isEqualTo(member);

        member.removePreachIn(worshipEventBack);
        assertThat(member.getPreachIns()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getPreacher()).isNull();

        member.preachIns(new HashSet<>(Set.of(worshipEventBack)));
        assertThat(member.getPreachIns()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getPreacher()).isEqualTo(member);

        member.setPreachIns(new HashSet<>());
        assertThat(member.getPreachIns()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getPreacher()).isNull();
    }

    @Test
    void liturgyInTest() {
        Member member = getMemberRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        member.addLiturgyIn(worshipEventBack);
        assertThat(member.getLiturgyIns()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getLiturgist()).isEqualTo(member);

        member.removeLiturgyIn(worshipEventBack);
        assertThat(member.getLiturgyIns()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getLiturgist()).isNull();

        member.liturgyIns(new HashSet<>(Set.of(worshipEventBack)));
        assertThat(member.getLiturgyIns()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getLiturgist()).isEqualTo(member);

        member.setLiturgyIns(new HashSet<>());
        assertThat(member.getLiturgyIns()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getLiturgist()).isNull();
    }

    @Test
    void appointmentTest() {
        Member member = getMemberRandomSampleGenerator();
        Appointment appointmentBack = getAppointmentRandomSampleGenerator();

        member.addAppointment(appointmentBack);
        assertThat(member.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getMember()).isEqualTo(member);

        member.removeAppointment(appointmentBack);
        assertThat(member.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getMember()).isNull();

        member.appointments(new HashSet<>(Set.of(appointmentBack)));
        assertThat(member.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getMember()).isEqualTo(member);

        member.setAppointments(new HashSet<>());
        assertThat(member.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getMember()).isNull();
    }

    @Test
    void presidentOfTest() {
        Member member = getMemberRandomSampleGenerator();
        MinistryGroup ministryGroupBack = getMinistryGroupRandomSampleGenerator();

        member.addPresidentOf(ministryGroupBack);
        assertThat(member.getPresidentOfs()).containsOnly(ministryGroupBack);
        assertThat(ministryGroupBack.getPresident()).isEqualTo(member);

        member.removePresidentOf(ministryGroupBack);
        assertThat(member.getPresidentOfs()).doesNotContain(ministryGroupBack);
        assertThat(ministryGroupBack.getPresident()).isNull();

        member.presidentOfs(new HashSet<>(Set.of(ministryGroupBack)));
        assertThat(member.getPresidentOfs()).containsOnly(ministryGroupBack);
        assertThat(ministryGroupBack.getPresident()).isEqualTo(member);

        member.setPresidentOfs(new HashSet<>());
        assertThat(member.getPresidentOfs()).doesNotContain(ministryGroupBack);
        assertThat(ministryGroupBack.getPresident()).isNull();
    }

    @Test
    void supervisorOfTest() {
        Member member = getMemberRandomSampleGenerator();
        MinistryGroup ministryGroupBack = getMinistryGroupRandomSampleGenerator();

        member.addSupervisorOf(ministryGroupBack);
        assertThat(member.getSupervisorOfs()).containsOnly(ministryGroupBack);
        assertThat(ministryGroupBack.getSupervisor()).isEqualTo(member);

        member.removeSupervisorOf(ministryGroupBack);
        assertThat(member.getSupervisorOfs()).doesNotContain(ministryGroupBack);
        assertThat(ministryGroupBack.getSupervisor()).isNull();

        member.supervisorOfs(new HashSet<>(Set.of(ministryGroupBack)));
        assertThat(member.getSupervisorOfs()).containsOnly(ministryGroupBack);
        assertThat(ministryGroupBack.getSupervisor()).isEqualTo(member);

        member.setSupervisorOfs(new HashSet<>());
        assertThat(member.getSupervisorOfs()).doesNotContain(ministryGroupBack);
        assertThat(ministryGroupBack.getSupervisor()).isNull();
    }

    @Test
    void playInTest() {
        Member member = getMemberRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        member.addPlayIn(worshipEventBack);
        assertThat(member.getPlayIns()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getMusicians()).containsOnly(member);

        member.removePlayIn(worshipEventBack);
        assertThat(member.getPlayIns()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getMusicians()).doesNotContain(member);

        member.playIns(new HashSet<>(Set.of(worshipEventBack)));
        assertThat(member.getPlayIns()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getMusicians()).containsOnly(member);

        member.setPlayIns(new HashSet<>());
        assertThat(member.getPlayIns()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getMusicians()).doesNotContain(member);
    }

    @Test
    void participateInTest() {
        Member member = getMemberRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        member.addParticipateIn(worshipEventBack);
        assertThat(member.getParticipateIns()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getParticipants()).containsOnly(member);

        member.removeParticipateIn(worshipEventBack);
        assertThat(member.getParticipateIns()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getParticipants()).doesNotContain(member);

        member.participateIns(new HashSet<>(Set.of(worshipEventBack)));
        assertThat(member.getParticipateIns()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getParticipants()).containsOnly(member);

        member.setParticipateIns(new HashSet<>());
        assertThat(member.getParticipateIns()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getParticipants()).doesNotContain(member);
    }

    @Test
    void memberOfTest() {
        Member member = getMemberRandomSampleGenerator();
        MinistryGroup ministryGroupBack = getMinistryGroupRandomSampleGenerator();

        member.addMemberOf(ministryGroupBack);
        assertThat(member.getMemberOfs()).containsOnly(ministryGroupBack);
        assertThat(ministryGroupBack.getMembers()).containsOnly(member);

        member.removeMemberOf(ministryGroupBack);
        assertThat(member.getMemberOfs()).doesNotContain(ministryGroupBack);
        assertThat(ministryGroupBack.getMembers()).doesNotContain(member);

        member.memberOfs(new HashSet<>(Set.of(ministryGroupBack)));
        assertThat(member.getMemberOfs()).containsOnly(ministryGroupBack);
        assertThat(ministryGroupBack.getMembers()).containsOnly(member);

        member.setMemberOfs(new HashSet<>());
        assertThat(member.getMemberOfs()).doesNotContain(ministryGroupBack);
        assertThat(ministryGroupBack.getMembers()).doesNotContain(member);
    }
}
