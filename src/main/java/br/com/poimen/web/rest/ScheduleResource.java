package br.com.poimen.web.rest;

import br.com.poimen.domain.Schedule;
import br.com.poimen.repository.ScheduleRepository;
import br.com.poimen.service.ScheduleService;
import br.com.poimen.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.poimen.domain.Schedule}.
 */
@RestController
@RequestMapping("/api/schedules")
public class ScheduleResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleResource.class);

    private static final String ENTITY_NAME = "schedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduleService scheduleService;

    private final ScheduleRepository scheduleRepository;

    public ScheduleResource(ScheduleService scheduleService, ScheduleRepository scheduleRepository) {
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * {@code POST  /schedules} : Create a new schedule.
     *
     * @param schedule the schedule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schedule, or with status {@code 400 (Bad Request)} if the schedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody Schedule schedule) throws URISyntaxException {
        LOG.debug("REST request to save Schedule : {}", schedule);
        if (schedule.getId() != null) {
            throw new BadRequestAlertException("A new schedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        schedule = scheduleService.save(schedule);
        return ResponseEntity.created(new URI("/api/schedules/" + schedule.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, schedule.getId().toString()))
            .body(schedule);
    }

    /**
     * {@code PUT  /schedules/:id} : Updates an existing schedule.
     *
     * @param id the id of the schedule to save.
     * @param schedule the schedule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedule,
     * or with status {@code 400 (Bad Request)} if the schedule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schedule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Schedule schedule
    ) throws URISyntaxException {
        LOG.debug("REST request to update Schedule : {}, {}", id, schedule);
        if (schedule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        schedule = scheduleService.update(schedule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedule.getId().toString()))
            .body(schedule);
    }

    /**
     * {@code PATCH  /schedules/:id} : Partial updates given fields of an existing schedule, field will ignore if it is null
     *
     * @param id the id of the schedule to save.
     * @param schedule the schedule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedule,
     * or with status {@code 400 (Bad Request)} if the schedule is not valid,
     * or with status {@code 404 (Not Found)} if the schedule is not found,
     * or with status {@code 500 (Internal Server Error)} if the schedule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Schedule> partialUpdateSchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Schedule schedule
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Schedule partially : {}, {}", id, schedule);
        if (schedule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Schedule> result = scheduleService.partialUpdate(schedule);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedule.getId().toString())
        );
    }

    /**
     * {@code GET  /schedules} : get all the schedules.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schedules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Schedule>> getAllSchedules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Schedules");
        Page<Schedule> page;
        if (eagerload) {
            page = scheduleService.findAllWithEagerRelationships(pageable);
        } else {
            page = scheduleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /schedules/:id} : get the "id" schedule.
     *
     * @param id the id of the schedule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schedule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Schedule : {}", id);
        Optional<Schedule> schedule = scheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schedule);
    }

    /**
     * {@code DELETE  /schedules/:id} : delete the "id" schedule.
     *
     * @param id the id of the schedule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Schedule : {}", id);
        scheduleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
