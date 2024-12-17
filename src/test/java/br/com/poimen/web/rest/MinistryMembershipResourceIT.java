package br.com.poimen.web.rest;

import static br.com.poimen.domain.MinistryMembershipAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.MinistryMembership;
import br.com.poimen.domain.enumeration.RoleMinistry;
import br.com.poimen.repository.MinistryMembershipRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MinistryMembershipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MinistryMembershipResourceIT {

    private static final RoleMinistry DEFAULT_ROLE = RoleMinistry.PRESIDENT;
    private static final RoleMinistry UPDATED_ROLE = RoleMinistry.VICE_PRESIDENT;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/ministry-memberships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MinistryMembershipRepository ministryMembershipRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMinistryMembershipMockMvc;

    private MinistryMembership ministryMembership;

    private MinistryMembership insertedMinistryMembership;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MinistryMembership createEntity() {
        return new MinistryMembership().role(DEFAULT_ROLE).startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MinistryMembership createUpdatedEntity() {
        return new MinistryMembership().role(UPDATED_ROLE).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        ministryMembership = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMinistryMembership != null) {
            ministryMembershipRepository.delete(insertedMinistryMembership);
            insertedMinistryMembership = null;
        }
    }

    @Test
    @Transactional
    void createMinistryMembership() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MinistryMembership
        var returnedMinistryMembership = om.readValue(
            restMinistryMembershipMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ministryMembership)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MinistryMembership.class
        );

        // Validate the MinistryMembership in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMinistryMembershipUpdatableFieldsEquals(
            returnedMinistryMembership,
            getPersistedMinistryMembership(returnedMinistryMembership)
        );

        insertedMinistryMembership = returnedMinistryMembership;
    }

    @Test
    @Transactional
    void createMinistryMembershipWithExistingId() throws Exception {
        // Create the MinistryMembership with an existing ID
        ministryMembership.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMinistryMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ministryMembership)))
            .andExpect(status().isBadRequest());

        // Validate the MinistryMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ministryMembership.setRole(null);

        // Create the MinistryMembership, which fails.

        restMinistryMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ministryMembership)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMinistryMemberships() throws Exception {
        // Initialize the database
        insertedMinistryMembership = ministryMembershipRepository.saveAndFlush(ministryMembership);

        // Get all the ministryMembershipList
        restMinistryMembershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ministryMembership.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getMinistryMembership() throws Exception {
        // Initialize the database
        insertedMinistryMembership = ministryMembershipRepository.saveAndFlush(ministryMembership);

        // Get the ministryMembership
        restMinistryMembershipMockMvc
            .perform(get(ENTITY_API_URL_ID, ministryMembership.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ministryMembership.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMinistryMembership() throws Exception {
        // Get the ministryMembership
        restMinistryMembershipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMinistryMembership() throws Exception {
        // Initialize the database
        insertedMinistryMembership = ministryMembershipRepository.saveAndFlush(ministryMembership);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ministryMembership
        MinistryMembership updatedMinistryMembership = ministryMembershipRepository.findById(ministryMembership.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMinistryMembership are not directly saved in db
        em.detach(updatedMinistryMembership);
        updatedMinistryMembership.role(UPDATED_ROLE).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).status(UPDATED_STATUS);

        restMinistryMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMinistryMembership.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMinistryMembership))
            )
            .andExpect(status().isOk());

        // Validate the MinistryMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMinistryMembershipToMatchAllProperties(updatedMinistryMembership);
    }

    @Test
    @Transactional
    void putNonExistingMinistryMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ministryMembership.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMinistryMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ministryMembership.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ministryMembership))
            )
            .andExpect(status().isBadRequest());

        // Validate the MinistryMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMinistryMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ministryMembership.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ministryMembership))
            )
            .andExpect(status().isBadRequest());

        // Validate the MinistryMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMinistryMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ministryMembership.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryMembershipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ministryMembership)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MinistryMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMinistryMembershipWithPatch() throws Exception {
        // Initialize the database
        insertedMinistryMembership = ministryMembershipRepository.saveAndFlush(ministryMembership);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ministryMembership using partial update
        MinistryMembership partialUpdatedMinistryMembership = new MinistryMembership();
        partialUpdatedMinistryMembership.setId(ministryMembership.getId());

        partialUpdatedMinistryMembership.role(UPDATED_ROLE).endDate(UPDATED_END_DATE).status(UPDATED_STATUS);

        restMinistryMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMinistryMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMinistryMembership))
            )
            .andExpect(status().isOk());

        // Validate the MinistryMembership in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMinistryMembershipUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMinistryMembership, ministryMembership),
            getPersistedMinistryMembership(ministryMembership)
        );
    }

    @Test
    @Transactional
    void fullUpdateMinistryMembershipWithPatch() throws Exception {
        // Initialize the database
        insertedMinistryMembership = ministryMembershipRepository.saveAndFlush(ministryMembership);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ministryMembership using partial update
        MinistryMembership partialUpdatedMinistryMembership = new MinistryMembership();
        partialUpdatedMinistryMembership.setId(ministryMembership.getId());

        partialUpdatedMinistryMembership.role(UPDATED_ROLE).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).status(UPDATED_STATUS);

        restMinistryMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMinistryMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMinistryMembership))
            )
            .andExpect(status().isOk());

        // Validate the MinistryMembership in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMinistryMembershipUpdatableFieldsEquals(
            partialUpdatedMinistryMembership,
            getPersistedMinistryMembership(partialUpdatedMinistryMembership)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMinistryMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ministryMembership.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMinistryMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ministryMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ministryMembership))
            )
            .andExpect(status().isBadRequest());

        // Validate the MinistryMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMinistryMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ministryMembership.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ministryMembership))
            )
            .andExpect(status().isBadRequest());

        // Validate the MinistryMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMinistryMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ministryMembership.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryMembershipMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ministryMembership)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MinistryMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMinistryMembership() throws Exception {
        // Initialize the database
        insertedMinistryMembership = ministryMembershipRepository.saveAndFlush(ministryMembership);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ministryMembership
        restMinistryMembershipMockMvc
            .perform(delete(ENTITY_API_URL_ID, ministryMembership.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ministryMembershipRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MinistryMembership getPersistedMinistryMembership(MinistryMembership ministryMembership) {
        return ministryMembershipRepository.findById(ministryMembership.getId()).orElseThrow();
    }

    protected void assertPersistedMinistryMembershipToMatchAllProperties(MinistryMembership expectedMinistryMembership) {
        assertMinistryMembershipAllPropertiesEquals(expectedMinistryMembership, getPersistedMinistryMembership(expectedMinistryMembership));
    }

    protected void assertPersistedMinistryMembershipToMatchUpdatableProperties(MinistryMembership expectedMinistryMembership) {
        assertMinistryMembershipAllUpdatablePropertiesEquals(
            expectedMinistryMembership,
            getPersistedMinistryMembership(expectedMinistryMembership)
        );
    }
}
