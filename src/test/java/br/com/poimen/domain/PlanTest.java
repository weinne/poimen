package br.com.poimen.domain;

import static br.com.poimen.domain.PlanSubscriptionTestSamples.*;
import static br.com.poimen.domain.PlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plan.class);
        Plan plan1 = getPlanSample1();
        Plan plan2 = new Plan();
        assertThat(plan1).isNotEqualTo(plan2);

        plan2.setId(plan1.getId());
        assertThat(plan1).isEqualTo(plan2);

        plan2 = getPlanSample2();
        assertThat(plan1).isNotEqualTo(plan2);
    }

    @Test
    void subscriptionTest() {
        Plan plan = getPlanRandomSampleGenerator();
        PlanSubscription planSubscriptionBack = getPlanSubscriptionRandomSampleGenerator();

        plan.addSubscription(planSubscriptionBack);
        assertThat(plan.getSubscriptions()).containsOnly(planSubscriptionBack);
        assertThat(planSubscriptionBack.getPlan()).isEqualTo(plan);

        plan.removeSubscription(planSubscriptionBack);
        assertThat(plan.getSubscriptions()).doesNotContain(planSubscriptionBack);
        assertThat(planSubscriptionBack.getPlan()).isNull();

        plan.subscriptions(new HashSet<>(Set.of(planSubscriptionBack)));
        assertThat(plan.getSubscriptions()).containsOnly(planSubscriptionBack);
        assertThat(planSubscriptionBack.getPlan()).isEqualTo(plan);

        plan.setSubscriptions(new HashSet<>());
        assertThat(plan.getSubscriptions()).doesNotContain(planSubscriptionBack);
        assertThat(planSubscriptionBack.getPlan()).isNull();
    }
}
