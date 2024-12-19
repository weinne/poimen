package br.com.poimen.web.rest;

import static br.com.poimen.domain.WorshipEventAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.WorshipEvent;
import br.com.poimen.domain.enumeration.WorshipType;
import br.com.poimen.repository.WorshipEventRepository;
import br.com.poimen.repository.search.WorshipEventSearchRepository;
import br.com.poimen.service.WorshipEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
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

    private static final String DEFAULT_GUEST_PREACHER = "AAAAAAAAAA";
    private static final String UPDATED_GUEST_PREACHER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CALL_TO_WORSHIP_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CALL_TO_WORSHIP_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CONFESSION_OF_SIN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CONFESSION_OF_SIN_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ASSURANCE_OF_PARDON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ASSURANCE_OF_PARDON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_LORD_SUPPER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_LORD_SUPPER_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_BENEDICTION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_BENEDICTION_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CONFESSIONAL_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CONFESSIONAL_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SERMON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SERMON_TEXT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SERMON_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SERMON_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SERMON_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SERMON_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_SERMON_LINK = "AAAAAAAAAA";
    private static final String UPDATED_SERMON_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_YOUTUBE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_YOUTUBE_LINK = "BBBBBBBBBB";

    private static final byte[] DEFAULT_BULLETIN_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BULLETIN_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BULLETIN_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BULLETIN_FILE_CONTENT_TYPE = "image/png";

    private static final WorshipType DEFAULT_WORSHIP_TYPE = WorshipType.SUNDAY_SERVICE;
    private static final WorshipType UPDATED_WORSHIP_TYPE = WorshipType.PRAYER_MEETING;

    private static final String ENTITY_API_URL = "/api/worship-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/worship-events/_search";

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
    private WorshipEventSearchRepository worshipEventSearchRepository;

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
            .guestPreacher(DEFAULT_GUEST_PREACHER)
            .description(DEFAULT_DESCRIPTION)
            .callToWorshipText(DEFAULT_CALL_TO_WORSHIP_TEXT)
            .confessionOfSinText(DEFAULT_CONFESSION_OF_SIN_TEXT)
            .assuranceOfPardonText(DEFAULT_ASSURANCE_OF_PARDON_TEXT)
            .lordSupperText(DEFAULT_LORD_SUPPER_TEXT)
            .benedictionText(DEFAULT_BENEDICTION_TEXT)
            .confessionalText(DEFAULT_CONFESSIONAL_TEXT)
            .sermonText(DEFAULT_SERMON_TEXT)
            .sermonFile(DEFAULT_SERMON_FILE)
            .sermonFileContentType(DEFAULT_SERMON_FILE_CONTENT_TYPE)
            .sermonLink(DEFAULT_SERMON_LINK)
            .youtubeLink(DEFAULT_YOUTUBE_LINK)
            .bulletinFile(DEFAULT_BULLETIN_FILE)
            .bulletinFileContentType(DEFAULT_BULLETIN_FILE_CONTENT_TYPE)
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
            .guestPreacher(UPDATED_GUEST_PREACHER)
            .description(UPDATED_DESCRIPTION)
            .callToWorshipText(UPDATED_CALL_TO_WORSHIP_TEXT)
            .confessionOfSinText(UPDATED_CONFESSION_OF_SIN_TEXT)
            .assuranceOfPardonText(UPDATED_ASSURANCE_OF_PARDON_TEXT)
            .lordSupperText(UPDATED_LORD_SUPPER_TEXT)
            .benedictionText(UPDATED_BENEDICTION_TEXT)
            .confessionalText(UPDATED_CONFESSIONAL_TEXT)
            .sermonText(UPDATED_SERMON_TEXT)
            .sermonFile(UPDATED_SERMON_FILE)
            .sermonFileContentType(UPDATED_SERMON_FILE_CONTENT_TYPE)
            .sermonLink(UPDATED_SERMON_LINK)
            .youtubeLink(UPDATED_YOUTUBE_LINK)
            .bulletinFile(UPDATED_BULLETIN_FILE)
            .bulletinFileContentType(UPDATED_BULLETIN_FILE_CONTENT_TYPE)
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
            worshipEventSearchRepository.delete(insertedWorshipEvent);
            insertedWorshipEvent = null;
        }
    }

    @Test
    @Transactional
    void createWorshipEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
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

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedWorshipEvent = returnedWorshipEvent;
    }

    @Test
    @Transactional
    void createWorshipEventWithExistingId() throws Exception {
        // Create the WorshipEvent with an existing ID
        worshipEvent.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorshipEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isBadRequest());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        // set the field null
        worshipEvent.setDate(null);

        // Create the WorshipEvent, which fails.

        restWorshipEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkWorshipTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        // set the field null
        worshipEvent.setWorshipType(null);

        // Create the WorshipEvent, which fails.

        restWorshipEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
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
            .andExpect(jsonPath("$.[*].guestPreacher").value(hasItem(DEFAULT_GUEST_PREACHER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].callToWorshipText").value(hasItem(DEFAULT_CALL_TO_WORSHIP_TEXT)))
            .andExpect(jsonPath("$.[*].confessionOfSinText").value(hasItem(DEFAULT_CONFESSION_OF_SIN_TEXT)))
            .andExpect(jsonPath("$.[*].assuranceOfPardonText").value(hasItem(DEFAULT_ASSURANCE_OF_PARDON_TEXT)))
            .andExpect(jsonPath("$.[*].lordSupperText").value(hasItem(DEFAULT_LORD_SUPPER_TEXT)))
            .andExpect(jsonPath("$.[*].benedictionText").value(hasItem(DEFAULT_BENEDICTION_TEXT)))
            .andExpect(jsonPath("$.[*].confessionalText").value(hasItem(DEFAULT_CONFESSIONAL_TEXT)))
            .andExpect(jsonPath("$.[*].sermonText").value(hasItem(DEFAULT_SERMON_TEXT.toString())))
            .andExpect(jsonPath("$.[*].sermonFileContentType").value(hasItem(DEFAULT_SERMON_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sermonFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_SERMON_FILE))))
            .andExpect(jsonPath("$.[*].sermonLink").value(hasItem(DEFAULT_SERMON_LINK)))
            .andExpect(jsonPath("$.[*].youtubeLink").value(hasItem(DEFAULT_YOUTUBE_LINK)))
            .andExpect(jsonPath("$.[*].bulletinFileContentType").value(hasItem(DEFAULT_BULLETIN_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bulletinFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BULLETIN_FILE))))
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
            .andExpect(jsonPath("$.guestPreacher").value(DEFAULT_GUEST_PREACHER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.callToWorshipText").value(DEFAULT_CALL_TO_WORSHIP_TEXT))
            .andExpect(jsonPath("$.confessionOfSinText").value(DEFAULT_CONFESSION_OF_SIN_TEXT))
            .andExpect(jsonPath("$.assuranceOfPardonText").value(DEFAULT_ASSURANCE_OF_PARDON_TEXT))
            .andExpect(jsonPath("$.lordSupperText").value(DEFAULT_LORD_SUPPER_TEXT))
            .andExpect(jsonPath("$.benedictionText").value(DEFAULT_BENEDICTION_TEXT))
            .andExpect(jsonPath("$.confessionalText").value(DEFAULT_CONFESSIONAL_TEXT))
            .andExpect(jsonPath("$.sermonText").value(DEFAULT_SERMON_TEXT.toString()))
            .andExpect(jsonPath("$.sermonFileContentType").value(DEFAULT_SERMON_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.sermonFile").value(Base64.getEncoder().encodeToString(DEFAULT_SERMON_FILE)))
            .andExpect(jsonPath("$.sermonLink").value(DEFAULT_SERMON_LINK))
            .andExpect(jsonPath("$.youtubeLink").value(DEFAULT_YOUTUBE_LINK))
            .andExpect(jsonPath("$.bulletinFileContentType").value(DEFAULT_BULLETIN_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.bulletinFile").value(Base64.getEncoder().encodeToString(DEFAULT_BULLETIN_FILE)))
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
        worshipEventSearchRepository.save(worshipEvent);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());

        // Update the worshipEvent
        WorshipEvent updatedWorshipEvent = worshipEventRepository.findById(worshipEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorshipEvent are not directly saved in db
        em.detach(updatedWorshipEvent);
        updatedWorshipEvent
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .guestPreacher(UPDATED_GUEST_PREACHER)
            .description(UPDATED_DESCRIPTION)
            .callToWorshipText(UPDATED_CALL_TO_WORSHIP_TEXT)
            .confessionOfSinText(UPDATED_CONFESSION_OF_SIN_TEXT)
            .assuranceOfPardonText(UPDATED_ASSURANCE_OF_PARDON_TEXT)
            .lordSupperText(UPDATED_LORD_SUPPER_TEXT)
            .benedictionText(UPDATED_BENEDICTION_TEXT)
            .confessionalText(UPDATED_CONFESSIONAL_TEXT)
            .sermonText(UPDATED_SERMON_TEXT)
            .sermonFile(UPDATED_SERMON_FILE)
            .sermonFileContentType(UPDATED_SERMON_FILE_CONTENT_TYPE)
            .sermonLink(UPDATED_SERMON_LINK)
            .youtubeLink(UPDATED_YOUTUBE_LINK)
            .bulletinFile(UPDATED_BULLETIN_FILE)
            .bulletinFileContentType(UPDATED_BULLETIN_FILE_CONTENT_TYPE)
            .worshipType(UPDATED_WORSHIP_TYPE);

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

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<WorshipEvent> worshipEventSearchList = Streamable.of(worshipEventSearchRepository.findAll()).toList();
                WorshipEvent testWorshipEventSearch = worshipEventSearchList.get(searchDatabaseSizeAfter - 1);

                assertWorshipEventAllPropertiesEquals(testWorshipEventSearch, updatedWorshipEvent);
            });
    }

    @Test
    @Transactional
    void putNonExistingWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        worshipEvent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorshipEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
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

        partialUpdatedWorshipEvent
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .confessionOfSinText(UPDATED_CONFESSION_OF_SIN_TEXT)
            .confessionalText(UPDATED_CONFESSIONAL_TEXT)
            .sermonText(UPDATED_SERMON_TEXT)
            .sermonFile(UPDATED_SERMON_FILE)
            .sermonFileContentType(UPDATED_SERMON_FILE_CONTENT_TYPE);

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
            .guestPreacher(UPDATED_GUEST_PREACHER)
            .description(UPDATED_DESCRIPTION)
            .callToWorshipText(UPDATED_CALL_TO_WORSHIP_TEXT)
            .confessionOfSinText(UPDATED_CONFESSION_OF_SIN_TEXT)
            .assuranceOfPardonText(UPDATED_ASSURANCE_OF_PARDON_TEXT)
            .lordSupperText(UPDATED_LORD_SUPPER_TEXT)
            .benedictionText(UPDATED_BENEDICTION_TEXT)
            .confessionalText(UPDATED_CONFESSIONAL_TEXT)
            .sermonText(UPDATED_SERMON_TEXT)
            .sermonFile(UPDATED_SERMON_FILE)
            .sermonFileContentType(UPDATED_SERMON_FILE_CONTENT_TYPE)
            .sermonLink(UPDATED_SERMON_LINK)
            .youtubeLink(UPDATED_YOUTUBE_LINK)
            .bulletinFile(UPDATED_BULLETIN_FILE)
            .bulletinFileContentType(UPDATED_BULLETIN_FILE_CONTENT_TYPE)
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
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorshipEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        worshipEvent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorshipEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(worshipEvent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorshipEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteWorshipEvent() throws Exception {
        // Initialize the database
        insertedWorshipEvent = worshipEventRepository.saveAndFlush(worshipEvent);
        worshipEventRepository.save(worshipEvent);
        worshipEventSearchRepository.save(worshipEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the worshipEvent
        restWorshipEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, worshipEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(worshipEventSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchWorshipEvent() throws Exception {
        // Initialize the database
        insertedWorshipEvent = worshipEventRepository.saveAndFlush(worshipEvent);
        worshipEventSearchRepository.save(worshipEvent);

        // Search the worshipEvent
        restWorshipEventMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + worshipEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(worshipEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].guestPreacher").value(hasItem(DEFAULT_GUEST_PREACHER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].callToWorshipText").value(hasItem(DEFAULT_CALL_TO_WORSHIP_TEXT)))
            .andExpect(jsonPath("$.[*].confessionOfSinText").value(hasItem(DEFAULT_CONFESSION_OF_SIN_TEXT)))
            .andExpect(jsonPath("$.[*].assuranceOfPardonText").value(hasItem(DEFAULT_ASSURANCE_OF_PARDON_TEXT)))
            .andExpect(jsonPath("$.[*].lordSupperText").value(hasItem(DEFAULT_LORD_SUPPER_TEXT)))
            .andExpect(jsonPath("$.[*].benedictionText").value(hasItem(DEFAULT_BENEDICTION_TEXT)))
            .andExpect(jsonPath("$.[*].confessionalText").value(hasItem(DEFAULT_CONFESSIONAL_TEXT)))
            .andExpect(jsonPath("$.[*].sermonText").value(hasItem(DEFAULT_SERMON_TEXT.toString())))
            .andExpect(jsonPath("$.[*].sermonFileContentType").value(hasItem(DEFAULT_SERMON_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sermonFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_SERMON_FILE))))
            .andExpect(jsonPath("$.[*].sermonLink").value(hasItem(DEFAULT_SERMON_LINK)))
            .andExpect(jsonPath("$.[*].youtubeLink").value(hasItem(DEFAULT_YOUTUBE_LINK)))
            .andExpect(jsonPath("$.[*].bulletinFileContentType").value(hasItem(DEFAULT_BULLETIN_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bulletinFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BULLETIN_FILE))))
            .andExpect(jsonPath("$.[*].worshipType").value(hasItem(DEFAULT_WORSHIP_TYPE.toString())));
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
