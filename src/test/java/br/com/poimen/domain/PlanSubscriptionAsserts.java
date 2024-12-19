package br.com.poimen.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PlanSubscriptionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlanSubscriptionAllPropertiesEquals(PlanSubscription expected, PlanSubscription actual) {
        assertPlanSubscriptionAutoGeneratedPropertiesEquals(expected, actual);
        assertPlanSubscriptionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlanSubscriptionAllUpdatablePropertiesEquals(PlanSubscription expected, PlanSubscription actual) {
        assertPlanSubscriptionUpdatableFieldsEquals(expected, actual);
        assertPlanSubscriptionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlanSubscriptionAutoGeneratedPropertiesEquals(PlanSubscription expected, PlanSubscription actual) {
        assertThat(expected)
            .as("Verify PlanSubscription auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlanSubscriptionUpdatableFieldsEquals(PlanSubscription expected, PlanSubscription actual) {
        assertThat(expected)
            .as("Verify PlanSubscription relevant properties")
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getStartDate()).as("check startDate").isEqualTo(actual.getStartDate()))
            .satisfies(e -> assertThat(e.getEndDate()).as("check endDate").isEqualTo(actual.getEndDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getPaymentProvider()).as("check paymentProvider").isEqualTo(actual.getPaymentProvider()))
            .satisfies(e -> assertThat(e.getPaymentStatus()).as("check paymentStatus").isEqualTo(actual.getPaymentStatus()))
            .satisfies(e -> assertThat(e.getPaymentReference()).as("check paymentReference").isEqualTo(actual.getPaymentReference()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPlanSubscriptionUpdatableRelationshipsEquals(PlanSubscription expected, PlanSubscription actual) {
        assertThat(expected)
            .as("Verify PlanSubscription relationships")
            .satisfies(e -> assertThat(e.getChurch()).as("check church").isEqualTo(actual.getChurch()))
            .satisfies(e -> assertThat(e.getPlan()).as("check plan").isEqualTo(actual.getPlan()))
            .satisfies(e -> assertThat(e.getUser()).as("check user").isEqualTo(actual.getUser()));
    }
}
