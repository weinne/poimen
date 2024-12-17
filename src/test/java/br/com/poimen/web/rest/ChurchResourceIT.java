package br.com.poimen.web.rest;

import static br.com.poimen.domain.ChurchAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.Church;
import br.com.poimen.repository.ChurchRepository;
import br.com.poimen.repository.UserRepository;
import br.com.poimen.service.ChurchService;
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
 * Integration tests for the {@link ChurchResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ChurchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_FOUNDATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FOUNDATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/churches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ChurchRepository churchRepositoryMock;

    @Mock
    private ChurchService churchServiceMock;

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
            .dateFoundation(DEFAULT_DATE_FOUNDATION);
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
            .dateFoundation(UPDATED_DATE_FOUNDATION);
    }

    @BeforeEach
    public void initTest() {
        church = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedChurch != null) {
            churchRepository.delete(insertedChurch);
            insertedChurch = null;
        }
    }

    @Test
    @Transactional
    void createChurch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
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

        insertedChurch = returnedChurch;
    }

    @Test
    @Transactional
    void createChurchWithExistingId() throws Exception {
        // Create the Church with an existing ID
        church.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        church.setName(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCnpjIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        church.setCnpj(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        church.setAddress(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        church.setCity(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFoundationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        church.setDateFoundation(null);

        // Create the Church, which fails.

        restChurchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].dateFoundation").value(hasItem(DEFAULT_DATE_FOUNDATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChurchesWithEagerRelationshipsIsEnabled() throws Exception {
        when(churchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChurchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(churchServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChurchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(churchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChurchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(churchRepositoryMock, times(1)).findAll(any(Pageable.class));
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
            .andExpect(jsonPath("$.dateFoundation").value(DEFAULT_DATE_FOUNDATION.toString()));
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

        // Update the church
        Church updatedChurch = churchRepository.findById(church.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChurch are not directly saved in db
        em.detach(updatedChurch);
        updatedChurch
            .name(UPDATED_NAME)
            .cnpj(UPDATED_CNPJ)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .dateFoundation(UPDATED_DATE_FOUNDATION);

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
    }

    @Test
    @Transactional
    void putNonExistingChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        church.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(put(ENTITY_API_URL_ID, church.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        church.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(church)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
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

        partialUpdatedChurch.name(UPDATED_NAME).city(UPDATED_CITY).dateFoundation(UPDATED_DATE_FOUNDATION);

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
            .dateFoundation(UPDATED_DATE_FOUNDATION);

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
        church.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, church.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(church))
            )
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChurch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        church.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(church)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Church in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChurch() throws Exception {
        // Initialize the database
        insertedChurch = churchRepository.saveAndFlush(church);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the church
        restChurchMockMvc
            .perform(delete(ENTITY_API_URL_ID, church.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
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
