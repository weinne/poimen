package br.com.poimen.web.rest;

import static br.com.poimen.domain.CounselingSessionAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.CounselingSession;
import br.com.poimen.repository.CounselingSessionRepository;
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
 * Integration tests for the {@link CounselingSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CounselingSessionResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/counseling-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CounselingSessionRepository counselingSessionRepository;

    @Autowired
    private UserRepository userRepository;

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
        return new CounselingSession().date(DEFAULT_DATE).notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounselingSession createUpdatedEntity() {
        return new CounselingSession().date(UPDATED_DATE).notes(UPDATED_NOTES);
    }

    @BeforeEach
    public void initTest() {
        counselingSession = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCounselingSession != null) {
            counselingSessionRepository.delete(insertedCounselingSession);
            insertedCounselingSession = null;
        }
    }

    @Test
    @Transactional
    void createCounselingSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
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

        insertedCounselingSession = returnedCounselingSession;
    }

    @Test
    @Transactional
    void createCounselingSessionWithExistingId() throws Exception {
        // Create the CounselingSession with an existing ID
        counselingSession.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCounselingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isBadRequest());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        counselingSession.setDate(null);

        // Create the CounselingSession, which fails.

        restCounselingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
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
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
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

        // Update the counselingSession
        CounselingSession updatedCounselingSession = counselingSessionRepository.findById(counselingSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCounselingSession are not directly saved in db
        em.detach(updatedCounselingSession);
        updatedCounselingSession.date(UPDATED_DATE).notes(UPDATED_NOTES);

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
    }

    @Test
    @Transactional
    void putNonExistingCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        counselingSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounselingSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
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

        partialUpdatedCounselingSession.date(UPDATED_DATE).notes(UPDATED_NOTES);

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

        partialUpdatedCounselingSession.date(UPDATED_DATE).notes(UPDATED_NOTES);

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
    }

    @Test
    @Transactional
    void patchWithIdMismatchCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCounselingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        counselingSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounselingSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(counselingSession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounselingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCounselingSession() throws Exception {
        // Initialize the database
        insertedCounselingSession = counselingSessionRepository.saveAndFlush(counselingSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the counselingSession
        restCounselingSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, counselingSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
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
