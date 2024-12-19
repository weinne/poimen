package br.com.poimen.web.rest;

import static br.com.poimen.domain.ChurchAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.Church;
import br.com.poimen.repository.ChurchRepository;
import br.com.poimen.repository.search.ChurchSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ChurchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChurchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "15813250798675";
    private static final String UPDATED_CNPJ = "55953538055493";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_FOUNDATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FOUNDATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_INSTAGRAM = "AAAAAAAAAA";
    private static final String UPDATED_INSTAGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER = "BBBBBBBBBB";

    private static final String DEFAULT_YOUTUBE = "AAAAAAAAAA";
    private static final String UPDATED_YOUTUBE = "BBBBBBBBBB";

    private static final String DEFAULT_ABOUT = "AAAAAAAAAA";
    private static final String UPDATED_ABOUT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/churches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/churches/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private ChurchSearchRepository churchSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChurchMockMvc;

    private Church church;

    private Church insertedChurch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Church createEntity() {
        return new Church()
            .name(DEFAULT_NAME)
            .cnpj(DEFAULT_CNPJ)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .dateFoundation(DEFAULT_DATE_FOUNDATION)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .website(DEFAULT_WEBSITE)
            .facebook(DEFAULT_FACEBOOK)
            .instagram(DEFAULT_INSTAGRAM)
            .twitter(DEFAULT_TWITTER)
            .youtube(DEFAULT_YOUTUBE)
            .about(DEFAULT_ABOUT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Church createUpdatedEntity() {
        return new Church()
            .name(UPDATED_NAME)
            .cnpj(UPDATED_CNPJ)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .dateFoundation(UPDATED_DATE_FOUNDATION)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .facebook(UPDATED_FACEBOOK)
            .instagram(UPDATED_INSTAGRAM)
            .twitter(UPDATED_TWITTER)
            .youtube(UPDATED_YOUTUBE)
            .about(UPDATED_ABOUT);
    }

    @BeforeEach
    public void initTest() {
        church = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedChurch != null) {
            churchRepository.delete(insertedChurch);
            churchSearchRepository.delete(insertedChurch);
            insertedChurch = null;
        }
    }

    @Test
    @Transactional
    void createChurch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        // Create the Church
        var returnedChurch = om.readValue(
            restChurchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Church.class
        );

        // Validate the Church in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertChurchUpdatableFieldsEquals(returnedChurch, getPersistedChurch(returnedChurch));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedChurch = returnedChurch;
    }

    @Test
    @Transactional
    void createChurchWithExistingId() throws Exception {
        // Create the Church with an existing ID
        church.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        // set the field null
        church.setName(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCnpjIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        // set the field null
        church.setCnpj(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        // set the field null
        church.setAddress(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        // set the field null
        church.setCity(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDateFoundationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        // set the field null
        church.setDateFoundation(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllChurches() throws Exception {
        // Initialize the database
        insertedChurch = churchRepository.saveAndFlush(church);

        // Get all the churchList
        restChurchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(church.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].dateFoundation").value(hasItem(DEFAULT_DATE_FOUNDATION.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER)))
            .andExpect(jsonPath("$.[*].youtube").value(hasItem(DEFAULT_YOUTUBE)))
            .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT.toString())));
    }

    @Test
    @Transactional
    void getChurch() throws Exception {
        // Initialize the database
        insertedChurch = churchRepository.saveAndFlush(church);

        // Get the church
        restChurchMockMvc
            .perform(get(ENTITY_API_URL_ID, church.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(church.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.dateFoundation").value(DEFAULT_DATE_FOUNDATION.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK))
            .andExpect(jsonPath("$.instagram").value(DEFAULT_INSTAGRAM))
            .andExpect(jsonPath("$.twitter").value(DEFAULT_TWITTER))
            .andExpect(jsonPath("$.youtube").value(DEFAULT_YOUTUBE))
            .andExpect(jsonPath("$.about").value(DEFAULT_ABOUT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChurch() throws Exception {
        // Get the church
        restChurchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChurch() throws Exception {
        // Initialize the database
        insertedChurch = churchRepository.saveAndFlush(church);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        churchSearchRepository.save(church);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());

        // Update the church
        Church updatedChurch = churchRepository.findById(church.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChurch are not directly saved in db
        em.detach(updatedChurch);
        updatedChurch
            .name(UPDATED_NAME)
            .cnpj(UPDATED_CNPJ)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .dateFoundation(UPDATED_DATE_FOUNDATION)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .facebook(UPDATED_FACEBOOK)
            .instagram(UPDATED_INSTAGRAM)
            .twitter(UPDATED_TWITTER)
            .youtube(UPDATED_YOUTUBE)
            .about(UPDATED_ABOUT);

        restChurchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChurch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedChurch))
            )
            .andExpect(status().isOk());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChurchToMatchAllProperties(updatedChurch);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Church> churchSearchList = Streamable.of(churchSearchRepository.findAll()).toList();
                Church testChurchSearch = churchSearchList.get(searchDatabaseSizeAfter - 1);

                assertChurchAllPropertiesEquals(testChurchSearch, updatedChurch);
            });
    }

    @Test
    @Transactional
    void putNonExistingChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        church.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(put(ENTITY_API_URL_ID, church.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        church.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(church))
            )
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        church.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateChurchWithPatch() throws Exception {
        // Initialize the database
        insertedChurch = churchRepository.saveAndFlush(church);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the church using partial update
        Church partialUpdatedChurch = new Church();
        partialUpdatedChurch.setId(church.getId());

        partialUpdatedChurch
            .name(UPDATED_NAME)
            .cnpj(UPDATED_CNPJ)
            .city(UPDATED_CITY)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .facebook(UPDATED_FACEBOOK)
            .twitter(UPDATED_TWITTER)
            .about(UPDATED_ABOUT);

        restChurchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChurch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChurch))
            )
            .andExpect(status().isOk());

        // Validate the Church in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChurchUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedChurch, church), getPersistedChurch(church));
    }

    @Test
    @Transactional
    void fullUpdateChurchWithPatch() throws Exception {
        // Initialize the database
        insertedChurch = churchRepository.saveAndFlush(church);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the church using partial update
        Church partialUpdatedChurch = new Church();
        partialUpdatedChurch.setId(church.getId());

        partialUpdatedChurch
            .name(UPDATED_NAME)
            .cnpj(UPDATED_CNPJ)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .dateFoundation(UPDATED_DATE_FOUNDATION)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .facebook(UPDATED_FACEBOOK)
            .instagram(UPDATED_INSTAGRAM)
            .twitter(UPDATED_TWITTER)
            .youtube(UPDATED_YOUTUBE)
            .about(UPDATED_ABOUT);

        restChurchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChurch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChurch))
            )
            .andExpect(status().isOk());

        // Validate the Church in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChurchUpdatableFieldsEquals(partialUpdatedChurch, getPersistedChurch(partialUpdatedChurch));
    }

    @Test
    @Transactional
    void patchNonExistingChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        church.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, church.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(church))
            )
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        church.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(church))
            )
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        church.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(church)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteChurch() throws Exception {
        // Initialize the database
        insertedChurch = churchRepository.saveAndFlush(church);
        churchRepository.save(church);
        churchSearchRepository.save(church);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the church
        restChurchMockMvc
            .perform(delete(ENTITY_API_URL_ID, church.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchChurch() throws Exception {
        // Initialize the database
        insertedChurch = churchRepository.saveAndFlush(church);
        churchSearchRepository.save(church);

        // Search the church
        restChurchMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + church.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(church.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].dateFoundation").value(hasItem(DEFAULT_DATE_FOUNDATION.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER)))
            .andExpect(jsonPath("$.[*].youtube").value(hasItem(DEFAULT_YOUTUBE)))
            .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT.toString())));
    }

    protected long getRepositoryCount() {
        return churchRepository.count();
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

    protected Church getPersistedChurch(Church church) {
        return churchRepository.findById(church.getId()).orElseThrow();
    }

    protected void assertPersistedChurchToMatchAllProperties(Church expectedChurch) {
        assertChurchAllPropertiesEquals(expectedChurch, getPersistedChurch(expectedChurch));
    }

    protected void assertPersistedChurchToMatchUpdatableProperties(Church expectedChurch) {
        assertChurchAllUpdatablePropertiesEquals(expectedChurch, getPersistedChurch(expectedChurch));
    }
}
