package br.com.poimen.web.rest;

import static br.com.poimen.domain.MinistryGroupAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.MinistryGroup;
import br.com.poimen.domain.enumeration.GroupType;
import br.com.poimen.repository.MinistryGroupRepository;
import br.com.poimen.repository.search.MinistryGroupSearchRepository;
import br.com.poimen.service.MinistryGroupService;
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
 * Integration tests for the {@link MinistryGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MinistryGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ESTABLISHED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESTABLISHED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final GroupType DEFAULT_TYPE = GroupType.DEPARTMENT;
    private static final GroupType UPDATED_TYPE = GroupType.INTERNAL_SOCIETY;

    private static final String ENTITY_API_URL = "/api/ministry-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ministry-groups/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MinistryGroupRepository ministryGroupRepository;

    @Mock
    private MinistryGroupRepository ministryGroupRepositoryMock;

    @Mock
    private MinistryGroupService ministryGroupServiceMock;

    @Autowired
    private MinistryGroupSearchRepository ministryGroupSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMinistryGroupMockMvc;

    private MinistryGroup ministryGroup;

    private MinistryGroup insertedMinistryGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MinistryGroup createEntity() {
        return new MinistryGroup()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .establishedDate(DEFAULT_ESTABLISHED_DATE)
            .type(DEFAULT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MinistryGroup createUpdatedEntity() {
        return new MinistryGroup()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .establishedDate(UPDATED_ESTABLISHED_DATE)
            .type(UPDATED_TYPE);
    }

    @BeforeEach
    public void initTest() {
        ministryGroup = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMinistryGroup != null) {
            ministryGroupRepository.delete(insertedMinistryGroup);
            ministryGroupSearchRepository.delete(insertedMinistryGroup);
            insertedMinistryGroup = null;
        }
    }

    @Test
    @Transactional
    void createMinistryGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        // Create the MinistryGroup
        var returnedMinistryGroup = om.readValue(
            restMinistryGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ministryGroup)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MinistryGroup.class
        );

        // Validate the MinistryGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMinistryGroupUpdatableFieldsEquals(returnedMinistryGroup, getPersistedMinistryGroup(returnedMinistryGroup));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMinistryGroup = returnedMinistryGroup;
    }

    @Test
    @Transactional
    void createMinistryGroupWithExistingId() throws Exception {
        // Create the MinistryGroup with an existing ID
        ministryGroup.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMinistryGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ministryGroup)))
            .andExpect(status().isBadRequest());

        // Validate the MinistryGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        // set the field null
        ministryGroup.setName(null);

        // Create the MinistryGroup, which fails.

        restMinistryGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ministryGroup)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        // set the field null
        ministryGroup.setType(null);

        // Create the MinistryGroup, which fails.

        restMinistryGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ministryGroup)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMinistryGroups() throws Exception {
        // Initialize the database
        insertedMinistryGroup = ministryGroupRepository.saveAndFlush(ministryGroup);

        // Get all the ministryGroupList
        restMinistryGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ministryGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].establishedDate").value(hasItem(DEFAULT_ESTABLISHED_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMinistryGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ministryGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMinistryGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ministryGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMinistryGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ministryGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMinistryGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ministryGroupRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMinistryGroup() throws Exception {
        // Initialize the database
        insertedMinistryGroup = ministryGroupRepository.saveAndFlush(ministryGroup);

        // Get the ministryGroup
        restMinistryGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, ministryGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ministryGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.establishedDate").value(DEFAULT_ESTABLISHED_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMinistryGroup() throws Exception {
        // Get the ministryGroup
        restMinistryGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMinistryGroup() throws Exception {
        // Initialize the database
        insertedMinistryGroup = ministryGroupRepository.saveAndFlush(ministryGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        ministryGroupSearchRepository.save(ministryGroup);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());

        // Update the ministryGroup
        MinistryGroup updatedMinistryGroup = ministryGroupRepository.findById(ministryGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMinistryGroup are not directly saved in db
        em.detach(updatedMinistryGroup);
        updatedMinistryGroup
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .establishedDate(UPDATED_ESTABLISHED_DATE)
            .type(UPDATED_TYPE);

        restMinistryGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMinistryGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMinistryGroup))
            )
            .andExpect(status().isOk());

        // Validate the MinistryGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMinistryGroupToMatchAllProperties(updatedMinistryGroup);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MinistryGroup> ministryGroupSearchList = Streamable.of(ministryGroupSearchRepository.findAll()).toList();
                MinistryGroup testMinistryGroupSearch = ministryGroupSearchList.get(searchDatabaseSizeAfter - 1);

                assertMinistryGroupAllPropertiesEquals(testMinistryGroupSearch, updatedMinistryGroup);
            });
    }

    @Test
    @Transactional
    void putNonExistingMinistryGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        ministryGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMinistryGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ministryGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ministryGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the MinistryGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMinistryGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        ministryGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ministryGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the MinistryGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMinistryGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        ministryGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ministryGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MinistryGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMinistryGroupWithPatch() throws Exception {
        // Initialize the database
        insertedMinistryGroup = ministryGroupRepository.saveAndFlush(ministryGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ministryGroup using partial update
        MinistryGroup partialUpdatedMinistryGroup = new MinistryGroup();
        partialUpdatedMinistryGroup.setId(ministryGroup.getId());

        partialUpdatedMinistryGroup.name(UPDATED_NAME);

        restMinistryGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMinistryGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMinistryGroup))
            )
            .andExpect(status().isOk());

        // Validate the MinistryGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMinistryGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMinistryGroup, ministryGroup),
            getPersistedMinistryGroup(ministryGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateMinistryGroupWithPatch() throws Exception {
        // Initialize the database
        insertedMinistryGroup = ministryGroupRepository.saveAndFlush(ministryGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ministryGroup using partial update
        MinistryGroup partialUpdatedMinistryGroup = new MinistryGroup();
        partialUpdatedMinistryGroup.setId(ministryGroup.getId());

        partialUpdatedMinistryGroup
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .establishedDate(UPDATED_ESTABLISHED_DATE)
            .type(UPDATED_TYPE);

        restMinistryGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMinistryGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMinistryGroup))
            )
            .andExpect(status().isOk());

        // Validate the MinistryGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMinistryGroupUpdatableFieldsEquals(partialUpdatedMinistryGroup, getPersistedMinistryGroup(partialUpdatedMinistryGroup));
    }

    @Test
    @Transactional
    void patchNonExistingMinistryGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        ministryGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMinistryGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ministryGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ministryGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the MinistryGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMinistryGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        ministryGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ministryGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the MinistryGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMinistryGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        ministryGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ministryGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MinistryGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMinistryGroup() throws Exception {
        // Initialize the database
        insertedMinistryGroup = ministryGroupRepository.saveAndFlush(ministryGroup);
        ministryGroupRepository.save(ministryGroup);
        ministryGroupSearchRepository.save(ministryGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ministryGroup
        restMinistryGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, ministryGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ministryGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMinistryGroup() throws Exception {
        // Initialize the database
        insertedMinistryGroup = ministryGroupRepository.saveAndFlush(ministryGroup);
        ministryGroupSearchRepository.save(ministryGroup);

        // Search the ministryGroup
        restMinistryGroupMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ministryGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ministryGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].establishedDate").value(hasItem(DEFAULT_ESTABLISHED_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    protected long getRepositoryCount() {
        return ministryGroupRepository.count();
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

    protected MinistryGroup getPersistedMinistryGroup(MinistryGroup ministryGroup) {
        return ministryGroupRepository.findById(ministryGroup.getId()).orElseThrow();
    }

    protected void assertPersistedMinistryGroupToMatchAllProperties(MinistryGroup expectedMinistryGroup) {
        assertMinistryGroupAllPropertiesEquals(expectedMinistryGroup, getPersistedMinistryGroup(expectedMinistryGroup));
    }

    protected void assertPersistedMinistryGroupToMatchUpdatableProperties(MinistryGroup expectedMinistryGroup) {
        assertMinistryGroupAllUpdatablePropertiesEquals(expectedMinistryGroup, getPersistedMinistryGroup(expectedMinistryGroup));
    }
}
