package br.com.poimen.web.rest;

import static br.com.poimen.domain.ScheduleAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.Schedule;
import br.com.poimen.domain.enumeration.RoleSchedule;
import br.com.poimen.repository.ScheduleRepository;
import br.com.poimen.service.ScheduleService;
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
 * Integration tests for the {@link ScheduleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ScheduleResourceIT {

    private static final RoleSchedule DEFAULT_ROLE_TYPE = RoleSchedule.PREACHER;
    private static final RoleSchedule UPDATED_ROLE_TYPE = RoleSchedule.LITURGIST;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleRepository scheduleRepositoryMock;

    @Mock
    private ScheduleService scheduleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleMockMvc;

    private Schedule schedule;

    private Schedule insertedSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schedule createEntity() {
        return new Schedule().roleType(DEFAULT_ROLE_TYPE).notes(DEFAULT_NOTES).startTime(DEFAULT_START_TIME).endTime(DEFAULT_END_TIME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schedule createUpdatedEntity() {
        return new Schedule().roleType(UPDATED_ROLE_TYPE).notes(UPDATED_NOTES).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
    }

    @BeforeEach
    public void initTest() {
        schedule = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSchedule != null) {
            scheduleRepository.delete(insertedSchedule);
            insertedSchedule = null;
        }
    }

    @Test
    @Transactional
    void createSchedule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Schedule
        var returnedSchedule = om.readValue(
            restScheduleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedule)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Schedule.class
        );

        // Validate the Schedule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertScheduleUpdatableFieldsEquals(returnedSchedule, getPersistedSchedule(returnedSchedule));

        insertedSchedule = returnedSchedule;
    }

    @Test
    @Transactional
    void createScheduleWithExistingId() throws Exception {
        // Create the Schedule with an existing ID
        schedule.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedule)))
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedule.setRoleType(null);

        // Create the Schedule, which fails.

        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedule)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedule.setStartTime(null);

        // Create the Schedule, which fails.

        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedule)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchedules() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleType").value(hasItem(DEFAULT_ROLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchedulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(scheduleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restScheduleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(scheduleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchedulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(scheduleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restScheduleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(scheduleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSchedule() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get the schedule
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, schedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedule.getId().intValue()))
            .andExpect(jsonPath("$.roleType").value(DEFAULT_ROLE_TYPE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSchedule() throws Exception {
        // Get the schedule
        restScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSchedule() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schedule
        Schedule updatedSchedule = scheduleRepository.findById(schedule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSchedule are not directly saved in db
        em.detach(updatedSchedule);
        updatedSchedule.roleType(UPDATED_ROLE_TYPE).notes(UPDATED_NOTES).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSchedule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSchedule))
            )
            .andExpect(status().isOk());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScheduleToMatchAllProperties(updatedSchedule);
    }

    @Test
    @Transactional
    void putNonExistingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedule.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedule))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(schedule))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleWithPatch() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schedule using partial update
        Schedule partialUpdatedSchedule = new Schedule();
        partialUpdatedSchedule.setId(schedule.getId());

        partialUpdatedSchedule.roleType(UPDATED_ROLE_TYPE).endTime(UPDATED_END_TIME);

        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchedule))
            )
            .andExpect(status().isOk());

        // Validate the Schedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSchedule, schedule), getPersistedSchedule(schedule));
    }

    @Test
    @Transactional
    void fullUpdateScheduleWithPatch() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schedule using partial update
        Schedule partialUpdatedSchedule = new Schedule();
        partialUpdatedSchedule.setId(schedule.getId());

        partialUpdatedSchedule.roleType(UPDATED_ROLE_TYPE).notes(UPDATED_NOTES).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchedule))
            )
            .andExpect(status().isOk());

        // Validate the Schedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduleUpdatableFieldsEquals(partialUpdatedSchedule, getPersistedSchedule(partialUpdatedSchedule));
    }

    @Test
    @Transactional
    void patchNonExistingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(schedule))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(schedule))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(schedule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchedule() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the schedule
        restScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scheduleRepository.count();
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

    protected Schedule getPersistedSchedule(Schedule schedule) {
        return scheduleRepository.findById(schedule.getId()).orElseThrow();
    }

    protected void assertPersistedScheduleToMatchAllProperties(Schedule expectedSchedule) {
        assertScheduleAllPropertiesEquals(expectedSchedule, getPersistedSchedule(expectedSchedule));
    }

    protected void assertPersistedScheduleToMatchUpdatableProperties(Schedule expectedSchedule) {
        assertScheduleAllUpdatablePropertiesEquals(expectedSchedule, getPersistedSchedule(expectedSchedule));
    }
}
