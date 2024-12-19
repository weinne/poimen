package br.com.poimen.web.rest;

import br.com.poimen.domain.Church;
import br.com.poimen.repository.ChurchRepository;
import br.com.poimen.service.ChurchService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.poimen.domain.Church}.
 */
@RestController
@RequestMapping("/api/churches")
public class ChurchResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChurchResource.class);

    private static final String ENTITY_NAME = "church";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChurchService churchService;

    private final ChurchRepository churchRepository;

    public ChurchResource(ChurchService churchService, ChurchRepository churchRepository) {
        this.churchService = churchService;
        this.churchRepository = churchRepository;
    }

    /**
     * {@code POST  /churches} : Create a new church.
     *
     * @param church the church to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new church, or with status {@code 400 (Bad Request)} if the church has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Church> createChurch(@Valid @RequestBody Church church) throws URISyntaxException {
        LOG.debug("REST request to save Church : {}", church);
        if (church.getId() != null) {
            throw new BadRequestAlertException("A new church cannot already have an ID", ENTITY_NAME, "idexists");
        }
        church = churchService.save(church);
        return ResponseEntity.created(new URI("/api/churches/" + church.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, church.getId().toString()))
            .body(church);
    }

    /**
     * {@code PUT  /churches/:id} : Updates an existing church.
     *
     * @param id the id of the church to save.
     * @param church the church to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated church,
     * or with status {@code 400 (Bad Request)} if the church is not valid,
     * or with status {@code 500 (Internal Server Error)} if the church couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Church> updateChurch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Church church
    ) throws URISyntaxException {
        LOG.debug("REST request to update Church : {}, {}", id, church);
        if (church.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, church.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!churchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        church = churchService.update(church);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, church.getId().toString()))
            .body(church);
    }

    /**
     * {@code PATCH  /churches/:id} : Partial updates given fields of an existing church, field will ignore if it is null
     *
     * @param id the id of the church to save.
     * @param church the church to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated church,
     * or with status {@code 400 (Bad Request)} if the church is not valid,
     * or with status {@code 404 (Not Found)} if the church is not found,
     * or with status {@code 500 (Internal Server Error)} if the church couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Church> partialUpdateChurch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Church church
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Church partially : {}, {}", id, church);
        if (church.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, church.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!churchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Church> result = churchService.partialUpdate(church);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, church.getId().toString())
        );
    }

    /**
     * {@code GET  /churches} : get all the churches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of churches in body.
     */
    @GetMapping("")
    public List<Church> getAllChurches() {
        LOG.debug("REST request to get all Churches");
        return churchService.findAll();
    }

    /**
     * {@code GET  /churches/:id} : get the "id" church.
     *
     * @param id the id of the church to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the church, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Church> getChurch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Church : {}", id);
        Optional<Church> church = churchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(church);
    }

    /**
     * {@code DELETE  /churches/:id} : delete the "id" church.
     *
     * @param id the id of the church to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChurch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Church : {}", id);
        churchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /churches/_search?query=:query} : search for the church corresponding
     * to the query.
     *
     * @param query the query of the church search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Church> searchChurches(@RequestParam("query") String query) {
        LOG.debug("REST request to search Churches for query {}", query);
        try {
            return churchService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
