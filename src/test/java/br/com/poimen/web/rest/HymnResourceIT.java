package br.com.poimen.web.rest;

import static br.com.poimen.domain.HymnAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.Hymn;
import br.com.poimen.repository.HymnRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link HymnResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HymnResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_HYMN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HYMN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LYRICS = "AAAAAAAAAA";
    private static final String UPDATED_LYRICS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hymns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HymnRepository hymnRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHymnMockMvc;

    private Hymn hymn;

    private Hymn insertedHymn;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hymn createEntity() {
        return new Hymn().title(DEFAULT_TITLE).author(DEFAULT_AUTHOR).hymnNumber(DEFAULT_HYMN_NUMBER).lyrics(DEFAULT_LYRICS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hymn createUpdatedEntity() {
        return new Hymn().title(UPDATED_TITLE).author(UPDATED_AUTHOR).hymnNumber(UPDATED_HYMN_NUMBER).lyrics(UPDATED_LYRICS);
    }

    @BeforeEach
    public void initTest() {
        hymn = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHymn != null) {
            hymnRepository.delete(insertedHymn);
            insertedHymn = null;
        }
    }

    @Test
    @Transactional
    void createHymn() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Hymn
        var returnedHymn = om.readValue(
            restHymnMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hymn)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Hymn.class
        );

        // Validate the Hymn in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertHymnUpdatableFieldsEquals(returnedHymn, getPersistedHymn(returnedHymn));

        insertedHymn = returnedHymn;
    }

    @Test
    @Transactional
    void createHymnWithExistingId() throws Exception {
        // Create the Hymn with an existing ID
        hymn.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHymnMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hymn)))
            .andExpect(status().isBadRequest());

        // Validate the Hymn in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hymn.setTitle(null);

        // Create the Hymn, which fails.

        restHymnMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hymn)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHymns() throws Exception {
        // Initialize the database
        insertedHymn = hymnRepository.saveAndFlush(hymn);

        // Get all the hymnList
        restHymnMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hymn.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].hymnNumber").value(hasItem(DEFAULT_HYMN_NUMBER)))
            .andExpect(jsonPath("$.[*].lyrics").value(hasItem(DEFAULT_LYRICS)));
    }

    @Test
    @Transactional
    void getHymn() throws Exception {
        // Initialize the database
        insertedHymn = hymnRepository.saveAndFlush(hymn);

        // Get the hymn
        restHymnMockMvc
            .perform(get(ENTITY_API_URL_ID, hymn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hymn.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.hymnNumber").value(DEFAULT_HYMN_NUMBER))
            .andExpect(jsonPath("$.lyrics").value(DEFAULT_LYRICS));
    }

    @Test
    @Transactional
    void getNonExistingHymn() throws Exception {
        // Get the hymn
        restHymnMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHymn() throws Exception {
        // Initialize the database
        insertedHymn = hymnRepository.saveAndFlush(hymn);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hymn
        Hymn updatedHymn = hymnRepository.findById(hymn.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHymn are not directly saved in db
        em.detach(updatedHymn);
        updatedHymn.title(UPDATED_TITLE).author(UPDATED_AUTHOR).hymnNumber(UPDATED_HYMN_NUMBER).lyrics(UPDATED_LYRICS);

        restHymnMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHymn.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedHymn))
            )
            .andExpect(status().isOk());

        // Validate the Hymn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHymnToMatchAllProperties(updatedHymn);
    }

    @Test
    @Transactional
    void putNonExistingHymn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hymn.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHymnMockMvc
            .perform(put(ENTITY_API_URL_ID, hymn.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hymn)))
            .andExpect(status().isBadRequest());

        // Validate the Hymn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHymn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hymn.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHymnMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hymn))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hymn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHymn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hymn.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHymnMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hymn)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hymn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHymnWithPatch() throws Exception {
        // Initialize the database
        insertedHymn = hymnRepository.saveAndFlush(hymn);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hymn using partial update
        Hymn partialUpdatedHymn = new Hymn();
        partialUpdatedHymn.setId(hymn.getId());

        partialUpdatedHymn.hymnNumber(UPDATED_HYMN_NUMBER);

        restHymnMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHymn.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHymn))
            )
            .andExpect(status().isOk());

        // Validate the Hymn in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHymnUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHymn, hymn), getPersistedHymn(hymn));
    }

    @Test
    @Transactional
    void fullUpdateHymnWithPatch() throws Exception {
        // Initialize the database
        insertedHymn = hymnRepository.saveAndFlush(hymn);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hymn using partial update
        Hymn partialUpdatedHymn = new Hymn();
        partialUpdatedHymn.setId(hymn.getId());

        partialUpdatedHymn.title(UPDATED_TITLE).author(UPDATED_AUTHOR).hymnNumber(UPDATED_HYMN_NUMBER).lyrics(UPDATED_LYRICS);

        restHymnMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHymn.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHymn))
            )
            .andExpect(status().isOk());

        // Validate the Hymn in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHymnUpdatableFieldsEquals(partialUpdatedHymn, getPersistedHymn(partialUpdatedHymn));
    }

    @Test
    @Transactional
    void patchNonExistingHymn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hymn.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHymnMockMvc
            .perform(patch(ENTITY_API_URL_ID, hymn.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hymn)))
            .andExpect(status().isBadRequest());

        // Validate the Hymn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHymn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hymn.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHymnMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hymn))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hymn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHymn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hymn.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHymnMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hymn)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hymn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHymn() throws Exception {
        // Initialize the database
        insertedHymn = hymnRepository.saveAndFlush(hymn);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hymn
        restHymnMockMvc
            .perform(delete(ENTITY_API_URL_ID, hymn.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hymnRepository.count();
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

    protected Hymn getPersistedHymn(Hymn hymn) {
        return hymnRepository.findById(hymn.getId()).orElseThrow();
    }

    protected void assertPersistedHymnToMatchAllProperties(Hymn expectedHymn) {
        assertHymnAllPropertiesEquals(expectedHymn, getPersistedHymn(expectedHymn));
    }

    protected void assertPersistedHymnToMatchUpdatableProperties(Hymn expectedHymn) {
        assertHymnAllUpdatablePropertiesEquals(expectedHymn, getPersistedHymn(expectedHymn));
    }
}
