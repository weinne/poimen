package br.com.poimen.web.rest;

import static br.com.poimen.domain.PlanSubscriptionAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.PlanSubscription;
import br.com.poimen.repository.PlanSubscriptionRepository;
import br.com.poimen.repository.UserRepository;
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
 * Integration tests for the {@link PlanSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlanSubscriptionResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_PLAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/plan-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlanSubscriptionRepository planSubscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanSubscriptionMockMvc;

    private PlanSubscription planSubscription;

    private PlanSubscription insertedPlanSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanSubscription createEntity() {
        return new PlanSubscription()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .active(DEFAULT_ACTIVE)
            .planName(DEFAULT_PLAN_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanSubscription createUpdatedEntity() {
        return new PlanSubscription()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .active(UPDATED_ACTIVE)
            .planName(UPDATED_PLAN_NAME);
    }

    @BeforeEach
    public void initTest() {
        planSubscription = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPlanSubscription != null) {
            planSubscriptionRepository.delete(insertedPlanSubscription);
            insertedPlanSubscription = null;
        }
    }

    @Test
    @Transactional
    void createPlanSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlanSubscription
        var returnedPlanSubscription = om.readValue(
            restPlanSubscriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlanSubscription.class
        );

        // Validate the PlanSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPlanSubscriptionUpdatableFieldsEquals(returnedPlanSubscription, getPersistedPlanSubscription(returnedPlanSubscription));

        insertedPlanSubscription = returnedPlanSubscription;
    }

    @Test
    @Transactional
    void createPlanSubscriptionWithExistingId() throws Exception {
        // Create the PlanSubscription with an existing ID
        planSubscription.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planSubscription.setStartDate(null);

        // Create the PlanSubscription, which fails.

        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planSubscription.setActive(null);

        // Create the PlanSubscription, which fails.

        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlanNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planSubscription.setPlanName(null);

        // Create the PlanSubscription, which fails.

        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlanSubscriptions() throws Exception {
        // Initialize the database
        insertedPlanSubscription = planSubscriptionRepository.saveAndFlush(planSubscription);

        // Get all the planSubscriptionList
        restPlanSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].planName").value(hasItem(DEFAULT_PLAN_NAME)));
    }

    @Test
    @Transactional
    void getPlanSubscription() throws Exception {
        // Initialize the database
        insertedPlanSubscription = planSubscriptionRepository.saveAndFlush(planSubscription);

        // Get the planSubscription
        restPlanSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, planSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planSubscription.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.planName").value(DEFAULT_PLAN_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPlanSubscription() throws Exception {
        // Get the planSubscription
        restPlanSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlanSubscription() throws Exception {
        // Initialize the database
        insertedPlanSubscription = planSubscriptionRepository.saveAndFlush(planSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planSubscription
        PlanSubscription updatedPlanSubscription = planSubscriptionRepository.findById(planSubscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlanSubscription are not directly saved in db
        em.detach(updatedPlanSubscription);
        updatedPlanSubscription.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE).planName(UPDATED_PLAN_NAME);

        restPlanSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlanSubscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPlanSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlanSubscriptionToMatchAllProperties(updatedPlanSubscription);
    }

    @Test
    @Transactional
    void putNonExistingPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planSubscription.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planSubscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanSubscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedPlanSubscription = planSubscriptionRepository.saveAndFlush(planSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planSubscription using partial update
        PlanSubscription partialUpdatedPlanSubscription = new PlanSubscription();
        partialUpdatedPlanSubscription.setId(planSubscription.getId());

        partialUpdatedPlanSubscription.startDate(UPDATED_START_DATE).active(UPDATED_ACTIVE).planName(UPDATED_PLAN_NAME);

        restPlanSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PlanSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanSubscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlanSubscription, planSubscription),
            getPersistedPlanSubscription(planSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlanSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedPlanSubscription = planSubscriptionRepository.saveAndFlush(planSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planSubscription using partial update
        PlanSubscription partialUpdatedPlanSubscription = new PlanSubscription();
        partialUpdatedPlanSubscription.setId(planSubscription.getId());

        partialUpdatedPlanSubscription
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .active(UPDATED_ACTIVE)
            .planName(UPDATED_PLAN_NAME);

        restPlanSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PlanSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanSubscriptionUpdatableFieldsEquals(
            partialUpdatedPlanSubscription,
            getPersistedPlanSubscription(partialUpdatedPlanSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planSubscription.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanSubscriptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanSubscription() throws Exception {
        // Initialize the database
        insertedPlanSubscription = planSubscriptionRepository.saveAndFlush(planSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the planSubscription
        restPlanSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, planSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return planSubscriptionRepository.count();
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

    protected PlanSubscription getPersistedPlanSubscription(PlanSubscription planSubscription) {
        return planSubscriptionRepository.findById(planSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedPlanSubscriptionToMatchAllProperties(PlanSubscription expectedPlanSubscription) {
        assertPlanSubscriptionAllPropertiesEquals(expectedPlanSubscription, getPersistedPlanSubscription(expectedPlanSubscription));
    }

    protected void assertPersistedPlanSubscriptionToMatchUpdatableProperties(PlanSubscription expectedPlanSubscription) {
        assertPlanSubscriptionAllUpdatablePropertiesEquals(
            expectedPlanSubscription,
            getPersistedPlanSubscription(expectedPlanSubscription)
        );
    }
}
