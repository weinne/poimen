package br.com.poimen.domain;

import static br.com.poimen.domain.ApplicationUserTestSamples.*;
import static br.com.poimen.domain.ChurchTestSamples.*;
import static br.com.poimen.domain.PlanSubscriptionTestSamples.*;
import static br.com.poimen.domain.PlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanSubscription.class);
        PlanSubscription planSubscription1 = getPlanSubscriptionSample1();
        PlanSubscription planSubscription2 = new PlanSubscription();
        assertThat(planSubscription1).isNotEqualTo(planSubscription2);

        planSubscription2.setId(planSubscription1.getId());
        assertThat(planSubscription1).isEqualTo(planSubscription2);

        planSubscription2 = getPlanSubscriptionSample2();
        assertThat(planSubscription1).isNotEqualTo(planSubscription2);
    }

    @Test
    void churchTest() {
        PlanSubscription planSubscription = getPlanSubscriptionRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        planSubscription.setChurch(churchBack);
        assertThat(planSubscription.getChurch()).isEqualTo(churchBack);

        planSubscription.church(null);
        assertThat(planSubscription.getChurch()).isNull();
    }

    @Test
    void planTest() {
        PlanSubscription planSubscription = getPlanSubscriptionRandomSampleGenerator();
        Plan planBack = getPlanRandomSampleGenerator();

        planSubscription.setPlan(planBack);
        assertThat(planSubscription.getPlan()).isEqualTo(planBack);

        planSubscription.plan(null);
        assertThat(planSubscription.getPlan()).isNull();
    }

    @Test
    void userTest() {
        PlanSubscription planSubscription = getPlanSubscriptionRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        planSubscription.setUser(applicationUserBack);
        assertThat(planSubscription.getUser()).isEqualTo(applicationUserBack);

        planSubscription.user(null);
        assertThat(planSubscription.getUser()).isNull();
    }
}
