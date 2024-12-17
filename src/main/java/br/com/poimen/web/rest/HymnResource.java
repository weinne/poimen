package br.com.poimen.web.rest;

import br.com.poimen.domain.Hymn;
import br.com.poimen.repository.HymnRepository;
import br.com.poimen.service.HymnService;
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
 * REST controller for managing {@link br.com.poimen.domain.Hymn}.
 */
@RestController
@RequestMapping("/api/hymns")
public class HymnResource {

    private static final Logger LOG = LoggerFactory.getLogger(HymnResource.class);

    private static final String ENTITY_NAME = "hymn";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HymnService hymnService;

    private final HymnRepository hymnRepository;

    public HymnResource(HymnService hymnService, HymnRepository hymnRepository) {
        this.hymnService = hymnService;
        this.hymnRepository = hymnRepository;
    }

    /**
     * {@code POST  /hymns} : Create a new hymn.
     *
     * @param hymn the hymn to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hymn, or with status {@code 400 (Bad Request)} if the hymn has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Hymn> createHymn(@Valid @RequestBody Hymn hymn) throws URISyntaxException {
        LOG.debug("REST request to save Hymn : {}", hymn);
        if (hymn.getId() != null) {
            throw new BadRequestAlertException("A new hymn cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hymn = hymnService.save(hymn);
        return ResponseEntity.created(new URI("/api/hymns/" + hymn.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hymn.getId().toString()))
            .body(hymn);
    }

    /**
     * {@code PUT  /hymns/:id} : Updates an existing hymn.
     *
     * @param id the id of the hymn to save.
     * @param hymn the hymn to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hymn,
     * or with status {@code 400 (Bad Request)} if the hymn is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hymn couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Hymn> updateHymn(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Hymn hymn)
        throws URISyntaxException {
        LOG.debug("REST request to update Hymn : {}, {}", id, hymn);
        if (hymn.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hymn.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hymnRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hymn = hymnService.update(hymn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hymn.getId().toString()))
            .body(hymn);
    }

    /**
     * {@code PATCH  /hymns/:id} : Partial updates given fields of an existing hymn, field will ignore if it is null
     *
     * @param id the id of the hymn to save.
     * @param hymn the hymn to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hymn,
     * or with status {@code 400 (Bad Request)} if the hymn is not valid,
     * or with status {@code 404 (Not Found)} if the hymn is not found,
     * or with status {@code 500 (Internal Server Error)} if the hymn couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Hymn> partialUpdateHymn(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Hymn hymn
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Hymn partially : {}, {}", id, hymn);
        if (hymn.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hymn.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hymnRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Hymn> result = hymnService.partialUpdate(hymn);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hymn.getId().toString())
        );
    }

    /**
     * {@code GET  /hymns} : get all the hymns.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hymns in body.
     */
    @GetMapping("")
    public List<Hymn> getAllHymns() {
        LOG.debug("REST request to get all Hymns");
        return hymnService.findAll();
    }

    /**
     * {@code GET  /hymns/:id} : get the "id" hymn.
     *
     * @param id the id of the hymn to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hymn, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Hymn> getHymn(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Hymn : {}", id);
        Optional<Hymn> hymn = hymnService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hymn);
    }

    /**
     * {@code DELETE  /hymns/:id} : delete the "id" hymn.
     *
     * @param id the id of the hymn to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHymn(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Hymn : {}", id);
        hymnService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
