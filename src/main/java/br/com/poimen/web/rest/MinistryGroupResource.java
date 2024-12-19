package br.com.poimen.web.rest;

import br.com.poimen.domain.MinistryGroup;
import br.com.poimen.repository.MinistryGroupRepository;
import br.com.poimen.service.MinistryGroupService;
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
 * REST controller for managing {@link br.com.poimen.domain.MinistryGroup}.
 */
@RestController
@RequestMapping("/api/ministry-groups")
public class MinistryGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(MinistryGroupResource.class);

    private static final String ENTITY_NAME = "ministryGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MinistryGroupService ministryGroupService;

    private final MinistryGroupRepository ministryGroupRepository;

    public MinistryGroupResource(MinistryGroupService ministryGroupService, MinistryGroupRepository ministryGroupRepository) {
        this.ministryGroupService = ministryGroupService;
        this.ministryGroupRepository = ministryGroupRepository;
    }

    /**
     * {@code POST  /ministry-groups} : Create a new ministryGroup.
     *
     * @param ministryGroup the ministryGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ministryGroup, or with status {@code 400 (Bad Request)} if the ministryGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MinistryGroup> createMinistryGroup(@Valid @RequestBody MinistryGroup ministryGroup) throws URISyntaxException {
        LOG.debug("REST request to save MinistryGroup : {}", ministryGroup);
        if (ministryGroup.getId() != null) {
            throw new BadRequestAlertException("A new ministryGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ministryGroup = ministryGroupService.save(ministryGroup);
        return ResponseEntity.created(new URI("/api/ministry-groups/" + ministryGroup.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ministryGroup.getId().toString()))
            .body(ministryGroup);
    }

    /**
     * {@code PUT  /ministry-groups/:id} : Updates an existing ministryGroup.
     *
     * @param id the id of the ministryGroup to save.
     * @param ministryGroup the ministryGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ministryGroup,
     * or with status {@code 400 (Bad Request)} if the ministryGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ministryGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MinistryGroup> updateMinistryGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MinistryGroup ministryGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to update MinistryGroup : {}, {}", id, ministryGroup);
        if (ministryGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ministryGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ministryGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ministryGroup = ministryGroupService.update(ministryGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ministryGroup.getId().toString()))
            .body(ministryGroup);
    }

    /**
     * {@code PATCH  /ministry-groups/:id} : Partial updates given fields of an existing ministryGroup, field will ignore if it is null
     *
     * @param id the id of the ministryGroup to save.
     * @param ministryGroup the ministryGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ministryGroup,
     * or with status {@code 400 (Bad Request)} if the ministryGroup is not valid,
     * or with status {@code 404 (Not Found)} if the ministryGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the ministryGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MinistryGroup> partialUpdateMinistryGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MinistryGroup ministryGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MinistryGroup partially : {}, {}", id, ministryGroup);
        if (ministryGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ministryGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ministryGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MinistryGroup> result = ministryGroupService.partialUpdate(ministryGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ministryGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /ministry-groups} : get all the ministryGroups.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ministryGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MinistryGroup>> getAllMinistryGroups(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of MinistryGroups");
        Page<MinistryGroup> page;
        if (eagerload) {
            page = ministryGroupService.findAllWithEagerRelationships(pageable);
        } else {
            page = ministryGroupService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ministry-groups/:id} : get the "id" ministryGroup.
     *
     * @param id the id of the ministryGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ministryGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MinistryGroup> getMinistryGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MinistryGroup : {}", id);
        Optional<MinistryGroup> ministryGroup = ministryGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ministryGroup);
    }

    /**
     * {@code DELETE  /ministry-groups/:id} : delete the "id" ministryGroup.
     *
     * @param id the id of the ministryGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMinistryGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MinistryGroup : {}", id);
        ministryGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ministry-groups/_search?query=:query} : search for the ministryGroup corresponding
     * to the query.
     *
     * @param query the query of the ministryGroup search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MinistryGroup>> searchMinistryGroups(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MinistryGroups for query {}", query);
        try {
            Page<MinistryGroup> page = ministryGroupService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
