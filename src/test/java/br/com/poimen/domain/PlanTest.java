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
    void planSubscriptionTest() {
        Plan plan = getPlanRandomSampleGenerator();
        PlanSubscription planSubscriptionBack = getPlanSubscriptionRandomSampleGenerator();

        plan.addPlanSubscription(planSubscriptionBack);
        assertThat(plan.getPlanSubscriptions()).containsOnly(planSubscriptionBack);
        assertThat(planSubscriptionBack.getPlan()).isEqualTo(plan);

        plan.removePlanSubscription(planSubscriptionBack);
        assertThat(plan.getPlanSubscriptions()).doesNotContain(planSubscriptionBack);
        assertThat(planSubscriptionBack.getPlan()).isNull();

        plan.planSubscriptions(new HashSet<>(Set.of(planSubscriptionBack)));
        assertThat(plan.getPlanSubscriptions()).containsOnly(planSubscriptionBack);
        assertThat(planSubscriptionBack.getPlan()).isEqualTo(plan);

        plan.setPlanSubscriptions(new HashSet<>());
        assertThat(plan.getPlanSubscriptions()).doesNotContain(planSubscriptionBack);
        assertThat(planSubscriptionBack.getPlan()).isNull();
    }
}
