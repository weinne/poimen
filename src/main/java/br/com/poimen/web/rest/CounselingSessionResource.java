package br.com.poimen.web.rest;

import br.com.poimen.domain.CounselingSession;
import br.com.poimen.repository.CounselingSessionRepository;
import br.com.poimen.service.CounselingSessionService;
import br.com.poimen.web.rest.errors.BadRequestAlertException;
import br.com.poimen.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link br.com.poimen.domain.CounselingSession}.
 */
@RestController
@RequestMapping("/api/counseling-sessions")
public class CounselingSessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CounselingSessionResource.class);

    private static final String ENTITY_NAME = "counselingSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CounselingSessionService counselingSessionService;

    private final CounselingSessionRepository counselingSessionRepository;

    public CounselingSessionResource(
        CounselingSessionService counselingSessionService,
        CounselingSessionRepository counselingSessionRepository
    ) {
        this.counselingSessionService = counselingSessionService;
        this.counselingSessionRepository = counselingSessionRepository;
    }

    /**
     * {@code POST  /counseling-sessions} : Create a new counselingSession.
     *
     * @param counselingSession the counselingSession to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new counselingSession, or with status {@code 400 (Bad Request)} if the counselingSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CounselingSession> createCounselingSession(@Valid @RequestBody CounselingSession counselingSession)
        throws URISyntaxException {
        LOG.debug("REST request to save CounselingSession : {}", counselingSession);
        if (counselingSession.getId() != null) {
            throw new BadRequestAlertException("A new counselingSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        counselingSession = counselingSessionService.save(counselingSession);
        return ResponseEntity.created(new URI("/api/counseling-sessions/" + counselingSession.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, counselingSession.getId().toString()))
            .body(counselingSession);
    }

    /**
     * {@code PUT  /counseling-sessions/:id} : Updates an existing counselingSession.
     *
     * @param id the id of the counselingSession to save.
     * @param counselingSession the counselingSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counselingSession,
     * or with status {@code 400 (Bad Request)} if the counselingSession is not valid,
     * or with status {@code 500 (Internal Server Error)} if the counselingSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CounselingSession> updateCounselingSession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CounselingSession counselingSession
    ) throws URISyntaxException {
        LOG.debug("REST request to update CounselingSession : {}, {}", id, counselingSession);
        if (counselingSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counselingSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counselingSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        counselingSession = counselingSessionService.update(counselingSession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counselingSession.getId().toString()))
            .body(counselingSession);
    }

    /**
     * {@code PATCH  /counseling-sessions/:id} : Partial updates given fields of an existing counselingSession, field will ignore if it is null
     *
     * @param id the id of the counselingSession to save.
     * @param counselingSession the counselingSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counselingSession,
     * or with status {@code 400 (Bad Request)} if the counselingSession is not valid,
     * or with status {@code 404 (Not Found)} if the counselingSession is not found,
     * or with status {@code 500 (Internal Server Error)} if the counselingSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CounselingSession> partialUpdateCounselingSession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CounselingSession counselingSession
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CounselingSession partially : {}, {}", id, counselingSession);
        if (counselingSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counselingSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counselingSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CounselingSession> result = counselingSessionService.partialUpdate(counselingSession);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counselingSession.getId().toString())
        );
    }

    /**
     * {@code GET  /counseling-sessions} : get all the counselingSessions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counselingSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CounselingSession>> getAllCounselingSessions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CounselingSessions");
        Page<CounselingSession> page;
        if (eagerload) {
            page = counselingSessionService.findAllWithEagerRelationships(pageable);
        } else {
            page = counselingSessionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /counseling-sessions/:id} : get the "id" counselingSession.
     *
     * @param id the id of the counselingSession to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the counselingSession, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CounselingSession> getCounselingSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CounselingSession : {}", id);
        Optional<CounselingSession> counselingSession = counselingSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(counselingSession);
    }

    /**
     * {@code DELETE  /counseling-sessions/:id} : delete the "id" counselingSession.
     *
     * @param id the id of the counselingSession to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounselingSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CounselingSession : {}", id);
        counselingSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /counseling-sessions/_search?query=:query} : search for the counselingSession corresponding
     * to the query.
     *
     * @param query the query of the counselingSession search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<CounselingSession>> searchCounselingSessions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of CounselingSessions for query {}", query);
        try {
            Page<CounselingSession> page = counselingSessionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
