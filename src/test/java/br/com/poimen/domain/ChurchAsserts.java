package br.com.poimen.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ChurchAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChurchAllPropertiesEquals(Church expected, Church actual) {
        assertChurchAutoGeneratedPropertiesEquals(expected, actual);
        assertChurchAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChurchAllUpdatablePropertiesEquals(Church expected, Church actual) {
        assertChurchUpdatableFieldsEquals(expected, actual);
        assertChurchUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChurchAutoGeneratedPropertiesEquals(Church expected, Church actual) {
        assertThat(expected)
            .as("Verify Church auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChurchUpdatableFieldsEquals(Church expected, Church actual) {
        assertThat(expected)
            .as("Verify Church relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getCnpj()).as("check cnpj").isEqualTo(actual.getCnpj()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getCity()).as("check city").isEqualTo(actual.getCity()))
            .satisfies(e -> assertThat(e.getDateFoundation()).as("check dateFoundation").isEqualTo(actual.getDateFoundation()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getWebsite()).as("check website").isEqualTo(actual.getWebsite()))
            .satisfies(e -> assertThat(e.getFacebook()).as("check facebook").isEqualTo(actual.getFacebook()))
            .satisfies(e -> assertThat(e.getInstagram()).as("check instagram").isEqualTo(actual.getInstagram()))
            .satisfies(e -> assertThat(e.getTwitter()).as("check twitter").isEqualTo(actual.getTwitter()))
            .satisfies(e -> assertThat(e.getYoutube()).as("check youtube").isEqualTo(actual.getYoutube()))
            .satisfies(e -> assertThat(e.getAbout()).as("check about").isEqualTo(actual.getAbout()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChurchUpdatableRelationshipsEquals(Church expected, Church actual) {
        // empty method
    }
}
