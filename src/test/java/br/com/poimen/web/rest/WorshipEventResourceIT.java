package br.com.poimen.web.rest;

import static br.com.poimen.domain.WorshipEventAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.WorshipEvent;
import br.com.poimen.domain.enumeration.WorshipType;
import br.com.poimen.repository.WorshipEventRepository;
import br.com.poimen.service.WorshipEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorshipEventResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorshipEventResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final WorshipType DEFAULT_WORSHIP_TYPE = WorshipType.SUNDAY_SERVICE;
    private static final WorshipType UPDATED_WORSHIP_TYPE = WorshipType.PRAYER_MEETING;

    private static final String ENTITY_API_URL = "/api/worship-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorshipEventRepository worshipEventRepository;

    @Mock
    private WorshipEventRepository worshipEventRepositoryMock;

    @Mock
    private WorshipEventService worshipEventServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorshipEventMockMvc;

    private WorshipEvent worshipEvent;

    private WorshipEvent insertedWorshipEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorshipEvent createEntity() {
        return new WorshipEvent()
            .date(DEFAULT_DATE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .worshipType(DEFAULT_WORSHIP_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorshipEvent createUpdatedEntity() {
        return new WorshipEvent()
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .worshipType(UPDATED_WORSHIP_TYPE);
    }

    @BeforeEach
    public void initTest() {
        worshipEvent = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWorshipEvent != null) {
            worshipEventRepository.delete(insertedWorshipEvent);
            insertedWorshipEvent = null;
        }
    }

    @Test
    @Transactional
    void createWorshipEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WorshipEvent
        var returnedWorshipEvent = om.readValue(
            restWorshipEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(worshipEvent)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorshipEvent.class
        );

        // Validate the WorshipEvent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWorshipEventUpdatableFieldsEquals(returnedWorshipEvent, getPersistedWorshipEvent(returnedWorshipEvent));

        insertedWorshipEvent = returnedWorshipEvent;
    }

    @Test
    @Transactional
    void createWorshipEventWithExistingId() throws Exception {
        // Create the WorshipEvent with an existing ID
        worshipEvent.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorshipEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isBadRequest());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        worshipEvent.setDate(null);

        // Create the WorshipEvent, which fails.

        restWorshipEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorshipTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        worshipEvent.setWorshipType(null);

        // Create the WorshipEvent, which fails.

        restWorshipEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorshipEvents() throws Exception {
        // Initialize the database
        insertedWorshipEvent = worshipEventRepository.saveAndFlush(worshipEvent);

        // Get all the worshipEventList
        restWorshipEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(worshipEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].worshipType").value(hasItem(DEFAULT_WORSHIP_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorshipEventsWithEagerRelationshipsIsEnabled() throws Exception {
        when(worshipEventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorshipEventMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(worshipEventServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorshipEventsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(worshipEventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorshipEventMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(worshipEventRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWorshipEvent() throws Exception {
        // Initialize the database
        insertedWorshipEvent = worshipEventRepository.saveAndFlush(worshipEvent);

        // Get the worshipEvent
        restWorshipEventMockMvc
            .perform(get(ENTITY_API_URL_ID, worshipEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(worshipEvent.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.worshipType").value(DEFAULT_WORSHIP_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorshipEvent() throws Exception {
        // Get the worshipEvent
        restWorshipEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorshipEvent() throws Exception {
        // Initialize the database
        insertedWorshipEvent = worshipEventRepository.saveAndFlush(worshipEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the worshipEvent
        WorshipEvent updatedWorshipEvent = worshipEventRepository.findById(worshipEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorshipEvent are not directly saved in db
        em.detach(updatedWorshipEvent);
        updatedWorshipEvent.date(UPDATED_DATE).title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).worshipType(UPDATED_WORSHIP_TYPE);

        restWorshipEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorshipEvent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWorshipEvent))
            )
            .andExpect(status().isOk());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorshipEventToMatchAllProperties(updatedWorshipEvent);
    }

    @Test
    @Transactional
    void putNonExistingWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        worshipEvent.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorshipEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, worshipEvent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(worshipEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        worshipEvent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorshipEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(worshipEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        worshipEvent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorshipEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorshipEventWithPatch() throws Exception {
        // Initialize the database
        insertedWorshipEvent = worshipEventRepository.saveAndFlush(worshipEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the worshipEvent using partial update
        WorshipEvent partialUpdatedWorshipEvent = new WorshipEvent();
        partialUpdatedWorshipEvent.setId(worshipEvent.getId());

        partialUpdatedWorshipEvent.title(UPDATED_TITLE);

        restWorshipEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorshipEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorshipEvent))
            )
            .andExpect(status().isOk());

        // Validate the WorshipEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorshipEventUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorshipEvent, worshipEvent),
            getPersistedWorshipEvent(worshipEvent)
        );
    }

    @Test
    @Transactional
    void fullUpdateWorshipEventWithPatch() throws Exception {
        // Initialize the database
        insertedWorshipEvent = worshipEventRepository.saveAndFlush(worshipEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the worshipEvent using partial update
        WorshipEvent partialUpdatedWorshipEvent = new WorshipEvent();
        partialUpdatedWorshipEvent.setId(worshipEvent.getId());

        partialUpdatedWorshipEvent
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .worshipType(UPDATED_WORSHIP_TYPE);

        restWorshipEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorshipEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorshipEvent))
            )
            .andExpect(status().isOk());

        // Validate the WorshipEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorshipEventUpdatableFieldsEquals(partialUpdatedWorshipEvent, getPersistedWorshipEvent(partialUpdatedWorshipEvent));
    }

    @Test
    @Transactional
    void patchNonExistingWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        worshipEvent.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorshipEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, worshipEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(worshipEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        worshipEvent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorshipEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(worshipEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        worshipEvent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorshipEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorshipEvent() throws Exception {
        // Initialize the database
        insertedWorshipEvent = worshipEventRepository.saveAndFlush(worshipEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the worshipEvent
        restWorshipEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, worshipEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return worshipEventRepository.count();
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

    protected WorshipEvent getPersistedWorshipEvent(WorshipEvent worshipEvent) {
        return worshipEventRepository.findById(worshipEvent.getId()).orElseThrow();
    }

    protected void assertPersistedWorshipEventToMatchAllProperties(WorshipEvent expectedWorshipEvent) {
        assertWorshipEventAllPropertiesEquals(expectedWorshipEvent, getPersistedWorshipEvent(expectedWorshipEvent));
    }

    protected void assertPersistedWorshipEventToMatchUpdatableProperties(WorshipEvent expectedWorshipEvent) {
        assertWorshipEventAllUpdatablePropertiesEquals(expectedWorshipEvent, getPersistedWorshipEvent(expectedWorshipEvent));
    }
}
