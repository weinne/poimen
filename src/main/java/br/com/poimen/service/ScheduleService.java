package br.com.poimen.service;

import br.com.poimen.domain.Schedule;
import br.com.poimen.repository.ScheduleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.Schedule}.
 */
@Service
@Transactional
public class ScheduleService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleService.class);

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * Save a schedule.
     *
     * @param schedule the entity to save.
     * @return the persisted entity.
     */
    public Schedule save(Schedule schedule) {
        LOG.debug("Request to save Schedule : {}", schedule);
        return scheduleRepository.save(schedule);
    }

    /**
     * Update a schedule.
     *
     * @param schedule the entity to save.
     * @return the persisted entity.
     */
    public Schedule update(Schedule schedule) {
        LOG.debug("Request to update Schedule : {}", schedule);
        return scheduleRepository.save(schedule);
    }

    /**
     * Partially update a schedule.
     *
     * @param schedule the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Schedule> partialUpdate(Schedule schedule) {
        LOG.debug("Request to partially update Schedule : {}", schedule);

        return scheduleRepository
            .findById(schedule.getId())
            .map(existingSchedule -> {
                if (schedule.getNotes() != null) {
                    existingSchedule.setNotes(schedule.getNotes());
                }
                if (schedule.getStartTime() != null) {
                    existingSchedule.setStartTime(schedule.getStartTime());
                }
                if (schedule.getEndTime() != null) {
                    existingSchedule.setEndTime(schedule.getEndTime());
                }

                return existingSchedule;
            })
            .map(scheduleRepository::save);
    }

    /**
     * Get all the schedules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Schedule> findAll(Pageable pageable) {
        LOG.debug("Request to get all Schedules");
        return scheduleRepository.findAll(pageable);
    }

    /**
     * Get all the schedules with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Schedule> findAllWithEagerRelationships(Pageable pageable) {
        return scheduleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one schedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Schedule> findOne(Long id) {
        LOG.debug("Request to get Schedule : {}", id);
        return scheduleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the schedule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Schedule : {}", id);
        scheduleRepository.deleteById(id);
    }
}
