package br.com.poimen.web.rest;

import static br.com.poimen.domain.PlanSubscriptionAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.PlanSubscription;
import br.com.poimen.domain.enumeration.PaymentProvider;
import br.com.poimen.domain.enumeration.PaymentStatus;
import br.com.poimen.domain.enumeration.PlanSubscriptionStatus;
import br.com.poimen.repository.PlanSubscriptionRepository;
import br.com.poimen.repository.search.PlanSubscriptionSearchRepository;
import br.com.poimen.service.PlanSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlanSubscriptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlanSubscriptionResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final PlanSubscriptionStatus DEFAULT_STATUS = PlanSubscriptionStatus.ACTIVE;
    private static final PlanSubscriptionStatus UPDATED_STATUS = PlanSubscriptionStatus.INACTIVE;

    private static final PaymentProvider DEFAULT_PAYMENT_PROVIDER = PaymentProvider.STRIPE;
    private static final PaymentProvider UPDATED_PAYMENT_PROVIDER = PaymentProvider.PAYPAL;

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.PENDING;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.COMPLETED;

    private static final String DEFAULT_PAYMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REFERENCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/plan-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/plan-subscriptions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlanSubscriptionRepository planSubscriptionRepository;

    @Mock
    private PlanSubscriptionRepository planSubscriptionRepositoryMock;

    @Mock
    private PlanSubscriptionService planSubscriptionServiceMock;

    @Autowired
    private PlanSubscriptionSearchRepository planSubscriptionSearchRepository;

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
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .paymentProvider(DEFAULT_PAYMENT_PROVIDER)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentReference(DEFAULT_PAYMENT_REFERENCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanSubscription createUpdatedEntity() {
        return new PlanSubscription()
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE);
    }

    @BeforeEach
    public void initTest() {
        planSubscription = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPlanSubscription != null) {
            planSubscriptionRepository.delete(insertedPlanSubscription);
            planSubscriptionSearchRepository.delete(insertedPlanSubscription);
            insertedPlanSubscription = null;
        }
    }

    @Test
    @Transactional
    void createPlanSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
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

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedPlanSubscription = returnedPlanSubscription;
    }

    @Test
    @Transactional
    void createPlanSubscriptionWithExistingId() throws Exception {
        // Create the PlanSubscription with an existing ID
        planSubscription.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        // set the field null
        planSubscription.setDescription(null);

        // Create the PlanSubscription, which fails.

        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        // set the field null
        planSubscription.setStartDate(null);

        // Create the PlanSubscription, which fails.

        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        // set the field null
        planSubscription.setStatus(null);

        // Create the PlanSubscription, which fails.

        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPaymentProviderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        // set the field null
        planSubscription.setPaymentProvider(null);

        // Create the PlanSubscription, which fails.

        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPaymentStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        // set the field null
        planSubscription.setPaymentStatus(null);

        // Create the PlanSubscription, which fails.

        restPlanSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
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
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentProvider").value(hasItem(DEFAULT_PAYMENT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentReference").value(hasItem(DEFAULT_PAYMENT_REFERENCE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanSubscriptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(planSubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanSubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(planSubscriptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanSubscriptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(planSubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanSubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(planSubscriptionRepositoryMock, times(1)).findAll(any(Pageable.class));
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
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentProvider").value(DEFAULT_PAYMENT_PROVIDER.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentReference").value(DEFAULT_PAYMENT_REFERENCE));
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
        planSubscriptionSearchRepository.save(planSubscription);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());

        // Update the planSubscription
        PlanSubscription updatedPlanSubscription = planSubscriptionRepository.findById(planSubscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlanSubscription are not directly saved in db
        em.detach(updatedPlanSubscription);
        updatedPlanSubscription
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE);

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

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PlanSubscription> planSubscriptionSearchList = Streamable.of(planSubscriptionSearchRepository.findAll()).toList();
                PlanSubscription testPlanSubscriptionSearch = planSubscriptionSearchList.get(searchDatabaseSizeAfter - 1);

                assertPlanSubscriptionAllPropertiesEquals(testPlanSubscriptionSearch, updatedPlanSubscription);
            });
    }

    @Test
    @Transactional
    void putNonExistingPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        planSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanSubscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
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

        partialUpdatedPlanSubscription
            .description(UPDATED_DESCRIPTION)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER);

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
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE);

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
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        planSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanSubscriptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planSubscription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePlanSubscription() throws Exception {
        // Initialize the database
        insertedPlanSubscription = planSubscriptionRepository.saveAndFlush(planSubscription);
        planSubscriptionRepository.save(planSubscription);
        planSubscriptionSearchRepository.save(planSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the planSubscription
        restPlanSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, planSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planSubscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPlanSubscription() throws Exception {
        // Initialize the database
        insertedPlanSubscription = planSubscriptionRepository.saveAndFlush(planSubscription);
        planSubscriptionSearchRepository.save(planSubscription);

        // Search the planSubscription
        restPlanSubscriptionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + planSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentProvider").value(hasItem(DEFAULT_PAYMENT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentReference").value(hasItem(DEFAULT_PAYMENT_REFERENCE)));
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
