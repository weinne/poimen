package br.com.poimen.domain;

import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.CounselingSessionTestSamples.*;
import static br.com.poimen.domain.InvoiceTestSamples.*;
import static br.com.poimen.domain.MemberTestSamples.*;
import static br.com.poimen.domain.MinistryGroupTestSamples.*;
import static br.com.poimen.domain.PlanSubscriptionTestSamples.*;
import static br.com.poimen.domain.TaskTestSamples.*;
import static br.com.poimen.domain.TransactionTestSamples.*;
import static br.com.poimen.domain.WorshipEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChurchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Church.class);
        Church church1 = getChurchSample1();
        Church church2 = new Church();
        assertThat(church1).isNotEqualTo(church2);

        church2.setId(church1.getId());
        assertThat(church1).isEqualTo(church2);

        church2 = getChurchSample2();
        assertThat(church1).isNotEqualTo(church2);
    }

    @Test
    void memberTest() {
        Church church = getChurchRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        church.addMember(memberBack);
        assertThat(church.getMembers()).containsOnly(memberBack);
        assertThat(memberBack.getChurch()).isEqualTo(church);

        church.removeMember(memberBack);
        assertThat(church.getMembers()).doesNotContain(memberBack);
        assertThat(memberBack.getChurch()).isNull();

        church.members(new HashSet<>(Set.of(memberBack)));
        assertThat(church.getMembers()).containsOnly(memberBack);
        assertThat(memberBack.getChurch()).isEqualTo(church);

        church.setMembers(new HashSet<>());
        assertThat(church.getMembers()).doesNotContain(memberBack);
        assertThat(memberBack.getChurch()).isNull();
    }

    @Test
    void ministryGroupTest() {
        Church church = getChurchRandomSampleGenerator();
        MinistryGroup ministryGroupBack = getMinistryGroupRandomSampleGenerator();

        church.addMinistryGroup(ministryGroupBack);
        assertThat(church.getMinistryGroups()).containsOnly(ministryGroupBack);
        assertThat(ministryGroupBack.getChurch()).isEqualTo(church);

        church.removeMinistryGroup(ministryGroupBack);
        assertThat(church.getMinistryGroups()).doesNotContain(ministryGroupBack);
        assertThat(ministryGroupBack.getChurch()).isNull();

        church.ministryGroups(new HashSet<>(Set.of(ministryGroupBack)));
        assertThat(church.getMinistryGroups()).containsOnly(ministryGroupBack);
        assertThat(ministryGroupBack.getChurch()).isEqualTo(church);

        church.setMinistryGroups(new HashSet<>());
        assertThat(church.getMinistryGroups()).doesNotContain(ministryGroupBack);
        assertThat(ministryGroupBack.getChurch()).isNull();
    }

    @Test
    void worshipEventTest() {
        Church church = getChurchRandomSampleGenerator();
        WorshipEvent worshipEventBack = getWorshipEventRandomSampleGenerator();

        church.addWorshipEvent(worshipEventBack);
        assertThat(church.getWorshipEvents()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getChurch()).isEqualTo(church);

        church.removeWorshipEvent(worshipEventBack);
        assertThat(church.getWorshipEvents()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getChurch()).isNull();

        church.worshipEvents(new HashSet<>(Set.of(worshipEventBack)));
        assertThat(church.getWorshipEvents()).containsOnly(worshipEventBack);
        assertThat(worshipEventBack.getChurch()).isEqualTo(church);

        church.setWorshipEvents(new HashSet<>());
        assertThat(church.getWorshipEvents()).doesNotContain(worshipEventBack);
        assertThat(worshipEventBack.getChurch()).isNull();
    }

    @Test
    void taskTest() {
        Church church = getChurchRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        church.addTask(taskBack);
        assertThat(church.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getChurch()).isEqualTo(church);

        church.removeTask(taskBack);
        assertThat(church.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getChurch()).isNull();

        church.tasks(new HashSet<>(Set.of(taskBack)));
        assertThat(church.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getChurch()).isEqualTo(church);

        church.setTasks(new HashSet<>());
        assertThat(church.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getChurch()).isNull();
    }

    @Test
    void counselingSessionTest() {
        Church church = getChurchRandomSampleGenerator();
        CounselingSession counselingSessionBack = getCounselingSessionRandomSampleGenerator();

        church.addCounselingSession(counselingSessionBack);
        assertThat(church.getCounselingSessions()).containsOnly(counselingSessionBack);
        assertThat(counselingSessionBack.getChurch()).isEqualTo(church);

        church.removeCounselingSession(counselingSessionBack);
        assertThat(church.getCounselingSessions()).doesNotContain(counselingSessionBack);
        assertThat(counselingSessionBack.getChurch()).isNull();

        church.counselingSessions(new HashSet<>(Set.of(counselingSessionBack)));
        assertThat(church.getCounselingSessions()).containsOnly(counselingSessionBack);
        assertThat(counselingSessionBack.getChurch()).isEqualTo(church);

        church.setCounselingSessions(new HashSet<>());
        assertThat(church.getCounselingSessions()).doesNotContain(counselingSessionBack);
        assertThat(counselingSessionBack.getChurch()).isNull();
    }

    @Test
    void invoiceTest() {
        Church church = getChurchRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        church.addInvoice(invoiceBack);
        assertThat(church.getInvoices()).containsOnly(invoiceBack);
        assertThat(invoiceBack.getChurch()).isEqualTo(church);

        church.removeInvoice(invoiceBack);
        assertThat(church.getInvoices()).doesNotContain(invoiceBack);
        assertThat(invoiceBack.getChurch()).isNull();

        church.invoices(new HashSet<>(Set.of(invoiceBack)));
        assertThat(church.getInvoices()).containsOnly(invoiceBack);
        assertThat(invoiceBack.getChurch()).isEqualTo(church);

        church.setInvoices(new HashSet<>());
        assertThat(church.getInvoices()).doesNotContain(invoiceBack);
        assertThat(invoiceBack.getChurch()).isNull();
    }

    @Test
    void transactionTest() {
        Church church = getChurchRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        church.addTransaction(transactionBack);
        assertThat(church.getTransactions()).containsOnly(transactionBack);
        assertThat(transactionBack.getChurch()).isEqualTo(church);

        church.removeTransaction(transactionBack);
        assertThat(church.getTransactions()).doesNotContain(transactionBack);
        assertThat(transactionBack.getChurch()).isNull();

        church.transactions(new HashSet<>(Set.of(transactionBack)));
        assertThat(church.getTransactions()).containsOnly(transactionBack);
        assertThat(transactionBack.getChurch()).isEqualTo(church);

        church.setTransactions(new HashSet<>());
        assertThat(church.getTransactions()).doesNotContain(transactionBack);
        assertThat(transactionBack.getChurch()).isNull();
    }

    @Test
    void planSubscriptionTest() {
        Church church = getChurchRandomSampleGenerator();
        PlanSubscription planSubscriptionBack = getPlanSubscriptionRandomSampleGenerator();

        church.addPlanSubscription(planSubscriptionBack);
        assertThat(church.getPlanSubscriptions()).containsOnly(planSubscriptionBack);
        assertThat(planSubscriptionBack.getChurch()).isEqualTo(church);

        church.removePlanSubscription(planSubscriptionBack);
        assertThat(church.getPlanSubscriptions()).doesNotContain(planSubscriptionBack);
        assertThat(planSubscriptionBack.getChurch()).isNull();

        church.planSubscriptions(new HashSet<>(Set.of(planSubscriptionBack)));
        assertThat(church.getPlanSubscriptions()).containsOnly(planSubscriptionBack);
        assertThat(planSubscriptionBack.getChurch()).isEqualTo(church);

        church.setPlanSubscriptions(new HashSet<>());
        assertThat(church.getPlanSubscriptions()).doesNotContain(planSubscriptionBack);
        assertThat(planSubscriptionBack.getChurch()).isNull();
    }
}
