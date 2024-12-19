package br.com.poimen.web.rest;

import static br.com.poimen.domain.CounselingSessionAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.CounselingSession;
import br.com.poimen.domain.enumeration.StatusCounseling;
import br.com.poimen.repository.CounselingSessionRepository;
import br.com.poimen.repository.search.CounselingSessionSearchRepository;
import br.com.poimen.service.CounselingSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CounselingSessionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CounselingSessionResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_COUNSELING_TASKS = "AAAAAAAAAA";
    private static final String UPDATED_COUNSELING_TASKS = "BBBBBBBBBB";

    private static final StatusCounseling DEFAULT_STATUS = StatusCounseling.SCHEDULED;
    private static final StatusCounseling UPDATED_STATUS = StatusCounseling.IN_PROGRESS;

    private static final String ENTITY_API_URL = "/api/counseling-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/counseling-sessions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CounselingSessionRepository counselingSessionRepository;

    @Mock
    private CounselingSessionRepository counselingSessionRepositoryMock;

    @Mock
    private CounselingSessionService counselingSessionServiceMock;

    @Autowired
    private CounselingSessionSearchRepository counselingSessionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCounselingSessionMockMvc;

    private CounselingSession counselingSession;

    private CounselingSession insertedCounselingSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounselingSession createEntity() {
        return new CounselingSession()
            .subject(DEFAULT_SUBJECT)
            .date(DEFAULT_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .notes(DEFAULT_NOTES)
            .counselingTasks(DEFAULT_COUNSELING_TASKS)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounselingSession createUpdatedEntity() {
        return new CounselingSession()
            .subject(UPDATED_SUBJECT)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .notes(UPDATED_NOTES)
            .counselingTasks(UPDATED_COUNSELING_TASKS)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        counselingSession = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCounselingSession != null) {
            counselingSessionRepository.delete(insertedCounselingSession);
            counselingSessionSearchRepository.delete(insertedCounselingSession);
            insertedCounselingSession = null;
        }
    }

    @Test
    @Transactional
    void createCounselingSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        // Create the CounselingSession
        var returnedCounselingSession = om.readValue(
            restCounselingSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CounselingSession.class
        );

        // Validate the CounselingSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCounselingSessionUpdatableFieldsEquals(returnedCounselingSession, getPersistedCounselingSession(returnedCounselingSession));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCounselingSession = returnedCounselingSession;
    }

    @Test
    @Transactional
    void createCounselingSessionWithExistingId() throws Exception {
        // Create the CounselingSession with an existing ID
        counselingSession.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCounselingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isBadRequest());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        // set the field null
        counselingSession.setSubject(null);

        // Create the CounselingSession, which fails.

        restCounselingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        // set the field null
        counselingSession.setDate(null);

        // Create the CounselingSession, which fails.

        restCounselingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        // set the field null
        counselingSession.setStartTime(null);

        // Create the CounselingSession, which fails.

        restCounselingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        // set the field null
        counselingSession.setStatus(null);

        // Create the CounselingSession, which fails.

        restCounselingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCounselingSessions() throws Exception {
        // Initialize the database
        insertedCounselingSession = counselingSessionRepository.saveAndFlush(counselingSession);

        // Get all the counselingSessionList
        restCounselingSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counselingSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].counselingTasks").value(hasItem(DEFAULT_COUNSELING_TASKS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCounselingSessionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(counselingSessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCounselingSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(counselingSessionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCounselingSessionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(counselingSessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCounselingSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(counselingSessionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCounselingSession() throws Exception {
        // Initialize the database
        insertedCounselingSession = counselingSessionRepository.saveAndFlush(counselingSession);

        // Get the counselingSession
        restCounselingSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, counselingSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(counselingSession.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.counselingTasks").value(DEFAULT_COUNSELING_TASKS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCounselingSession() throws Exception {
        // Get the counselingSession
        restCounselingSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCounselingSession() throws Exception {
        // Initialize the database
        insertedCounselingSession = counselingSessionRepository.saveAndFlush(counselingSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        counselingSessionSearchRepository.save(counselingSession);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());

        // Update the counselingSession
        CounselingSession updatedCounselingSession = counselingSessionRepository.findById(counselingSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCounselingSession are not directly saved in db
        em.detach(updatedCounselingSession);
        updatedCounselingSession
            .subject(UPDATED_SUBJECT)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .notes(UPDATED_NOTES)
            .counselingTasks(UPDATED_COUNSELING_TASKS)
            .status(UPDATED_STATUS);

        restCounselingSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCounselingSession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCounselingSession))
            )
            .andExpect(status().isOk());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCounselingSessionToMatchAllProperties(updatedCounselingSession);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CounselingSession> counselingSessionSearchList = Streamable.of(counselingSessionSearchRepository.findAll()).toList();
                CounselingSession testCounselingSessionSearch = counselingSessionSearchList.get(searchDatabaseSizeAfter - 1);

                assertCounselingSessionAllPropertiesEquals(testCounselingSessionSearch, updatedCounselingSession);
            });
    }

    @Test
    @Transactional
    void putNonExistingCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        counselingSession.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounselingSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counselingSession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(counselingSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        counselingSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounselingSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(counselingSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        counselingSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounselingSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCounselingSessionWithPatch() throws Exception {
        // Initialize the database
        insertedCounselingSession = counselingSessionRepository.saveAndFlush(counselingSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the counselingSession using partial update
        CounselingSession partialUpdatedCounselingSession = new CounselingSession();
        partialUpdatedCounselingSession.setId(counselingSession.getId());

        partialUpdatedCounselingSession
            .subject(UPDATED_SUBJECT)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .counselingTasks(UPDATED_COUNSELING_TASKS)
            .status(UPDATED_STATUS);

        restCounselingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounselingSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCounselingSession))
            )
            .andExpect(status().isOk());

        // Validate the CounselingSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCounselingSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCounselingSession, counselingSession),
            getPersistedCounselingSession(counselingSession)
        );
    }

    @Test
    @Transactional
    void fullUpdateCounselingSessionWithPatch() throws Exception {
        // Initialize the database
        insertedCounselingSession = counselingSessionRepository.saveAndFlush(counselingSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the counselingSession using partial update
        CounselingSession partialUpdatedCounselingSession = new CounselingSession();
        partialUpdatedCounselingSession.setId(counselingSession.getId());

        partialUpdatedCounselingSession
            .subject(UPDATED_SUBJECT)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .notes(UPDATED_NOTES)
            .counselingTasks(UPDATED_COUNSELING_TASKS)
            .status(UPDATED_STATUS);

        restCounselingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounselingSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCounselingSession))
            )
            .andExpect(status().isOk());

        // Validate the CounselingSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCounselingSessionUpdatableFieldsEquals(
            partialUpdatedCounselingSession,
            getPersistedCounselingSession(partialUpdatedCounselingSession)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        counselingSession.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounselingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, counselingSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(counselingSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        counselingSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounselingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(counselingSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        counselingSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounselingSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCounselingSession() throws Exception {
        // Initialize the database
        insertedCounselingSession = counselingSessionRepository.saveAndFlush(counselingSession);
        counselingSessionRepository.save(counselingSession);
        counselingSessionSearchRepository.save(counselingSession);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the counselingSession
        restCounselingSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, counselingSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(counselingSessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCounselingSession() throws Exception {
        // Initialize the database
        insertedCounselingSession = counselingSessionRepository.saveAndFlush(counselingSession);
        counselingSessionSearchRepository.save(counselingSession);

        // Search the counselingSession
        restCounselingSessionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + counselingSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counselingSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].counselingTasks").value(hasItem(DEFAULT_COUNSELING_TASKS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return counselingSessionRepository.count();
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

    protected CounselingSession getPersistedCounselingSession(CounselingSession counselingSession) {
        return counselingSessionRepository.findById(counselingSession.getId()).orElseThrow();
    }

    protected void assertPersistedCounselingSessionToMatchAllProperties(CounselingSession expectedCounselingSession) {
        assertCounselingSessionAllPropertiesEquals(expectedCounselingSession, getPersistedCounselingSession(expectedCounselingSession));
    }

    protected void assertPersistedCounselingSessionToMatchUpdatableProperties(CounselingSession expectedCounselingSession) {
        assertCounselingSessionAllUpdatablePropertiesEquals(
            expectedCounselingSession,
            getPersistedCounselingSession(expectedCounselingSession)
        );
    }
}
