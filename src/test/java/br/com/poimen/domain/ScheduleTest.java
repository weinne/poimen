package br.com.poimen.domain;

import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.ScheduleTestSamples.*;
import static br.com.poimen.domain.WorshipEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schedule.class);
        Schedule schedule1 = getScheduleSample1();
        Schedule schedule2 = new Schedule();
        assertThat(schedule1).isNotEqualTo(schedule2);

        schedule2.setId(schedule1.getId());
        assertThat(schedule1).isEqualTo(schedule2);

        schedule2 = getScheduleSample2();
        assertThat(schedule1).isNotEqualTo(schedule2);
    }

    @Test
    void memberTest() {
        Schedule schedule = getScheduleRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        schedule.addMember(memberBack);
        assertThat(schedule.getMembers()).containsOnly(memberBack);

        schedule.removeMember(memberBack);
        assertThat(schedule.getMembers()).doesNotContain(memberBack);

        schedule.members(new HashSet<>(Set.of(memberBack)));
        assertThat(schedule.getMembers()).containsOnly(memberBack);

        schedule.setMembers(new HashSet<>());
        assertThat(schedule.getMembers()).doesNotContain(memberBack);
    }

    @Test
    void worshipEventTest() {
        Schedule schedule = getScheduleRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        schedule.addWorshipEvent(worshipEventBack);
        assertThat(schedule.getWorshipEvents()).containsOnly(worshipEventBack);

        schedule.removeWorshipEvent(worshipEventBack);
        assertThat(schedule.getWorshipEvents()).doesNotContain(worshipEventBack);

        schedule.worshipEvents(new HashSet<>(Set.of(worshipEventBack)));
        assertThat(schedule.getWorshipEvents()).containsOnly(worshipEventBack);

        schedule.setWorshipEvents(new HashSet<>());
        assertThat(schedule.getWorshipEvents()).doesNotContain(worshipEventBack);
    }
}
