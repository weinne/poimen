package br.com.poimen.domain;

import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
        Task task1 = getTaskSample1();
        Task task2 = new Task();
        assertThat(task1).isNotEqualTo(task2);

        task2.setId(task1.getId());
        assertThat(task1).isEqualTo(task2);

        task2 = getTaskSample2();
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    void churchTest() {
        Task task = getTaskRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        task.setChurch(churchBack);
        assertThat(task.getChurch()).isEqualTo(churchBack);

        task.church(null);
        assertThat(task.getChurch()).isNull();
    }

    @Test
    void memberTest() {
        Task task = getTaskRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        task.setMember(memberBack);
        assertThat(task.getMember()).isEqualTo(memberBack);

        task.member(null);
        assertThat(task.getMember()).isNull();
    }
}
