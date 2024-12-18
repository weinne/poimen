package br.com.poimen.domain;

import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.CounselingSessionTestSamples.*;
import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.MinistryMembershipTestSamples.*;
import static br.com.poimen.domain.ScheduleTestSamples.*;
import static br.com.poimen.domain.TaskTestSamples.*;
import static br.com.poimen.domain.TransactionTestSamples.*;
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
    void counselingSessionTest() {
        Member member = getMemberRandomSampleGenerator();
        CounselingSession counselingSessionBack = getCounselingSessionRandomSampleGenerator();

        member.addCounselingSession(counselingSessionBack);
        assertThat(member.getCounselingSessions()).containsOnly(counselingSessionBack);
        assertThat(counselingSessionBack.getMember()).isEqualTo(member);

        member.removeCounselingSession(counselingSessionBack);
        assertThat(member.getCounselingSessions()).doesNotContain(counselingSessionBack);
        assertThat(counselingSessionBack.getMember()).isNull();

        member.counselingSessions(new HashSet<>(Set.of(counselingSessionBack)));
        assertThat(member.getCounselingSessions()).containsOnly(counselingSessionBack);
        assertThat(counselingSessionBack.getMember()).isEqualTo(member);

        member.setCounselingSessions(new HashSet<>());
        assertThat(member.getCounselingSessions()).doesNotContain(counselingSessionBack);
        assertThat(counselingSessionBack.getMember()).isNull();
    }

    @Test
    void ministryMembershipTest() {
        Member member = getMemberRandomSampleGenerator();
        MinistryMembership ministryMembershipBack = getMinistryMembershipRandomSampleGenerator();

        member.addMinistryMembership(ministryMembershipBack);
        assertThat(member.getMinistryMemberships()).containsOnly(ministryMembershipBack);
        assertThat(ministryMembershipBack.getMember()).isEqualTo(member);

        member.removeMinistryMembership(ministryMembershipBack);
        assertThat(member.getMinistryMemberships()).doesNotContain(ministryMembershipBack);
        assertThat(ministryMembershipBack.getMember()).isNull();

        member.ministryMemberships(new HashSet<>(Set.of(ministryMembershipBack)));
        assertThat(member.getMinistryMemberships()).containsOnly(ministryMembershipBack);
        assertThat(ministryMembershipBack.getMember()).isEqualTo(member);

        member.setMinistryMemberships(new HashSet<>());
        assertThat(member.getMinistryMemberships()).doesNotContain(ministryMembershipBack);
        assertThat(ministryMembershipBack.getMember()).isNull();
    }

    @Test
    void taskTest() {
        Member member = getMemberRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        member.addTask(taskBack);
        assertThat(member.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getMember()).isEqualTo(member);

        member.removeTask(taskBack);
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
    void transactionTest() {
        Member member = getMemberRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        member.addTransaction(transactionBack);
        assertThat(member.getTransactions()).containsOnly(transactionBack);
        assertThat(transactionBack.getMember()).isEqualTo(member);

        member.removeTransaction(transactionBack);
        assertThat(member.getTransactions()).doesNotContain(transactionBack);
        assertThat(transactionBack.getMember()).isNull();

        member.transactions(new HashSet<>(Set.of(transactionBack)));
        assertThat(member.getTransactions()).containsOnly(transactionBack);
        assertThat(transactionBack.getMember()).isEqualTo(member);

        member.setTransactions(new HashSet<>());
        assertThat(member.getTransactions()).doesNotContain(transactionBack);
        assertThat(transactionBack.getMember()).isNull();
    }

    @Test
    void scheduleTest() {
        Member member = getMemberRandomSampleGenerator();
        Schedule scheduleBack = getScheduleRandomSampleGenerator();

        member.addSchedule(scheduleBack);
        assertThat(member.getSchedules()).containsOnly(scheduleBack);
        assertThat(scheduleBack.getMembers()).containsOnly(member);

        member.removeSchedule(scheduleBack);
        assertThat(member.getSchedules()).doesNotContain(scheduleBack);
        assertThat(scheduleBack.getMembers()).doesNotContain(member);

        member.schedules(new HashSet<>(Set.of(scheduleBack)));
        assertThat(member.getSchedules()).containsOnly(scheduleBack);
        assertThat(scheduleBack.getMembers()).containsOnly(member);

        member.setSchedules(new HashSet<>());
        assertThat(member.getSchedules()).doesNotContain(scheduleBack);
        assertThat(scheduleBack.getMembers()).doesNotContain(member);
    }

    @Test
    void worshipEventTest() {
        Member member = getMemberRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        member.addWorshipEvent(worshipEventBack);
        assertThat(member.getWorshipEvents()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getMusicians()).containsOnly(member);

        member.removeWorshipEvent(worshipEventBack);
        assertThat(member.getWorshipEvents()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getMusicians()).doesNotContain(member);

        member.worshipEvents(new HashSet<>(Set.of(worshipEventBack)));
        assertThat(member.getWorshipEvents()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getMusicians()).containsOnly(member);

        member.setWorshipEvents(new HashSet<>());
        assertThat(member.getWorshipEvents()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getMusicians()).doesNotContain(member);
    }
}
