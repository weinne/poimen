package br.com.poimen.web.rest;

import br.com.poimen.domain.MinistryMembership;
import br.com.poimen.repository.MinistryMembershipRepository;
import br.com.poimen.service.MinistryMembershipService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.poimen.domain.MinistryMembership}.
 */
@RestController
@RequestMapping("/api/ministry-memberships")
public class MinistryMembershipResource {

    private static final Logger LOG = LoggerFactory.getLogger(MinistryMembershipResource.class);

    private static final String ENTITY_NAME = "ministryMembership";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MinistryMembershipService ministryMembershipService;

    private final MinistryMembershipRepository ministryMembershipRepository;

    public MinistryMembershipResource(
        MinistryMembershipService ministryMembershipService,
        MinistryMembershipRepository ministryMembershipRepository
    ) {
        this.ministryMembershipService = ministryMembershipService;
        this.ministryMembershipRepository = ministryMembershipRepository;
    }

    /**
     * {@code POST  /ministry-memberships} : Create a new ministryMembership.
     *
     * @param ministryMembership the ministryMembership to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ministryMembership, or with status {@code 400 (Bad Request)} if the ministryMembership has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MinistryMembership> createMinistryMembership(@Valid @RequestBody MinistryMembership ministryMembership)
        throws URISyntaxException {
        LOG.debug("REST request to save MinistryMembership : {}", ministryMembership);
        if (ministryMembership.getId() != null) {
            throw new BadRequestAlertException("A new ministryMembership cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ministryMembership = ministryMembershipService.save(ministryMembership);
        return ResponseEntity.created(new URI("/api/ministry-memberships/" + ministryMembership.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ministryMembership.getId().toString()))
            .body(ministryMembership);
    }

    /**
     * {@code PUT  /ministry-memberships/:id} : Updates an existing ministryMembership.
     *
     * @param id the id of the ministryMembership to save.
     * @param ministryMembership the ministryMembership to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ministryMembership,
     * or with status {@code 400 (Bad Request)} if the ministryMembership is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ministryMembership couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MinistryMembership> updateMinistryMembership(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MinistryMembership ministryMembership
    ) throws URISyntaxException {
        LOG.debug("REST request to update MinistryMembership : {}, {}", id, ministryMembership);
        if (ministryMembership.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ministryMembership.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ministryMembershipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ministryMembership = ministryMembershipService.update(ministryMembership);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ministryMembership.getId().toString()))
            .body(ministryMembership);
    }

    /**
     * {@code PATCH  /ministry-memberships/:id} : Partial updates given fields of an existing ministryMembership, field will ignore if it is null
     *
     * @param id the id of the ministryMembership to save.
     * @param ministryMembership the ministryMembership to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ministryMembership,
     * or with status {@code 400 (Bad Request)} if the ministryMembership is not valid,
     * or with status {@code 404 (Not Found)} if the ministryMembership is not found,
     * or with status {@code 500 (Internal Server Error)} if the ministryMembership couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MinistryMembership> partialUpdateMinistryMembership(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MinistryMembership ministryMembership
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MinistryMembership partially : {}, {}", id, ministryMembership);
        if (ministryMembership.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ministryMembership.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ministryMembershipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MinistryMembership> result = ministryMembershipService.partialUpdate(ministryMembership);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ministryMembership.getId().toString())
        );
    }

    /**
     * {@code GET  /ministry-memberships} : get all the ministryMemberships.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ministryMemberships in body.
     */
    @GetMapping("")
    public List<MinistryMembership> getAllMinistryMemberships() {
        LOG.debug("REST request to get all MinistryMemberships");
        return ministryMembershipService.findAll();
    }

    /**
     * {@code GET  /ministry-memberships/:id} : get the "id" ministryMembership.
     *
     * @param id the id of the ministryMembership to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ministryMembership, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MinistryMembership> getMinistryMembership(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MinistryMembership : {}", id);
        Optional<MinistryMembership> ministryMembership = ministryMembershipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ministryMembership);
    }

    /**
     * {@code DELETE  /ministry-memberships/:id} : delete the "id" ministryMembership.
     *
     * @param id the id of the ministryMembership to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMinistryMembership(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MinistryMembership : {}", id);
        ministryMembershipService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
