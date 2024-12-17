package br.com.poimen.web.rest;

import br.com.poimen.domain.WorshipEvent;
import br.com.poimen.repository.WorshipEventRepository;
import br.com.poimen.service.WorshipEventService;
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
 * REST controller for managing {@link br.com.poimen.domain.WorshipEvent}.
 */
@RestController
@RequestMapping("/api/worship-events")
public class WorshipEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(WorshipEventResource.class);

    private static final String ENTITY_NAME = "worshipEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorshipEventService worshipEventService;

    private final WorshipEventRepository worshipEventRepository;

    public WorshipEventResource(WorshipEventService worshipEventService, WorshipEventRepository worshipEventRepository) {
        this.worshipEventService = worshipEventService;
        this.worshipEventRepository = worshipEventRepository;
    }

    /**
     * {@code POST  /worship-events} : Create a new worshipEvent.
     *
     * @param worshipEvent the worshipEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new worshipEvent, or with status {@code 400 (Bad Request)} if the worshipEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WorshipEvent> createWorshipEvent(@Valid @RequestBody WorshipEvent worshipEvent) throws URISyntaxException {
        LOG.debug("REST request to save WorshipEvent : {}", worshipEvent);
        if (worshipEvent.getId() != null) {
            throw new BadRequestAlertException("A new worshipEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        worshipEvent = worshipEventService.save(worshipEvent);
        return ResponseEntity.created(new URI("/api/worship-events/" + worshipEvent.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, worshipEvent.getId().toString()))
            .body(worshipEvent);
    }

    /**
     * {@code PUT  /worship-events/:id} : Updates an existing worshipEvent.
     *
     * @param id the id of the worshipEvent to save.
     * @param worshipEvent the worshipEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated worshipEvent,
     * or with status {@code 400 (Bad Request)} if the worshipEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the worshipEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorshipEvent> updateWorshipEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorshipEvent worshipEvent
    ) throws URISyntaxException {
        LOG.debug("REST request to update WorshipEvent : {}, {}", id, worshipEvent);
        if (worshipEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, worshipEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!worshipEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        worshipEvent = worshipEventService.update(worshipEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, worshipEvent.getId().toString()))
            .body(worshipEvent);
    }

    /**
     * {@code PATCH  /worship-events/:id} : Partial updates given fields of an existing worshipEvent, field will ignore if it is null
     *
     * @param id the id of the worshipEvent to save.
     * @param worshipEvent the worshipEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated worshipEvent,
     * or with status {@code 400 (Bad Request)} if the worshipEvent is not valid,
     * or with status {@code 404 (Not Found)} if the worshipEvent is not found,
     * or with status {@code 500 (Internal Server Error)} if the worshipEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorshipEvent> partialUpdateWorshipEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorshipEvent worshipEvent
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WorshipEvent partially : {}, {}", id, worshipEvent);
        if (worshipEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, worshipEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!worshipEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorshipEvent> result = worshipEventService.partialUpdate(worshipEvent);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, worshipEvent.getId().toString())
        );
    }

    /**
     * {@code GET  /worship-events} : get all the worshipEvents.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of worshipEvents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WorshipEvent>> getAllWorshipEvents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of WorshipEvents");
        Page<WorshipEvent> page;
        if (eagerload) {
            page = worshipEventService.findAllWithEagerRelationships(pageable);
        } else {
            page = worshipEventService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /worship-events/:id} : get the "id" worshipEvent.
     *
     * @param id the id of the worshipEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the worshipEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorshipEvent> getWorshipEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WorshipEvent : {}", id);
        Optional<WorshipEvent> worshipEvent = worshipEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(worshipEvent);
    }

    /**
     * {@code DELETE  /worship-events/:id} : delete the "id" worshipEvent.
     *
     * @param id the id of the worshipEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorshipEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WorshipEvent : {}", id);
        worshipEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
