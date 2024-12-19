package br.com.poimen.web.rest;

import br.com.poimen.domain.Plan;
import br.com.poimen.repository.PlanRepository;
import br.com.poimen.service.PlanService;
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
 * REST controller for managing {@link br.com.poimen.domain.Plan}.
 */
@RestController
@RequestMapping("/api/plans")
public class PlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlanResource.class);

    private static final String ENTITY_NAME = "plan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanService planService;

    private final PlanRepository planRepository;

    public PlanResource(PlanService planService, PlanRepository planRepository) {
        this.planService = planService;
        this.planRepository = planRepository;
    }

    /**
     * {@code POST  /plans} : Create a new plan.
     *
     * @param plan the plan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plan, or with status {@code 400 (Bad Request)} if the plan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Plan> createPlan(@Valid @RequestBody Plan plan) throws URISyntaxException {
        LOG.debug("REST request to save Plan : {}", plan);
        if (plan.getId() != null) {
            throw new BadRequestAlertException("A new plan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        plan = planService.save(plan);
        return ResponseEntity.created(new URI("/api/plans/" + plan.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, plan.getId().toString()))
            .body(plan);
    }

    /**
     * {@code PUT  /plans/:id} : Updates an existing plan.
     *
     * @param id the id of the plan to save.
     * @param plan the plan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plan,
     * or with status {@code 400 (Bad Request)} if the plan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Plan plan)
        throws URISyntaxException {
        LOG.debug("REST request to update Plan : {}, {}", id, plan);
        if (plan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        plan = planService.update(plan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plan.getId().toString()))
            .body(plan);
    }

    /**
     * {@code PATCH  /plans/:id} : Partial updates given fields of an existing plan, field will ignore if it is null
     *
     * @param id the id of the plan to save.
     * @param plan the plan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plan,
     * or with status {@code 400 (Bad Request)} if the plan is not valid,
     * or with status {@code 404 (Not Found)} if the plan is not found,
     * or with status {@code 500 (Internal Server Error)} if the plan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Plan> partialUpdatePlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Plan plan
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Plan partially : {}, {}", id, plan);
        if (plan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Plan> result = planService.partialUpdate(plan);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plan.getId().toString())
        );
    }

    /**
     * {@code GET  /plans} : get all the plans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plans in body.
     */
    @GetMapping("")
    public List<Plan> getAllPlans() {
        LOG.debug("REST request to get all Plans");
        return planService.findAll();
    }

    /**
     * {@code GET  /plans/:id} : get the "id" plan.
     *
     * @param id the id of the plan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Plan : {}", id);
        Optional<Plan> plan = planService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plan);
    }

    /**
     * {@code DELETE  /plans/:id} : delete the "id" plan.
     *
     * @param id the id of the plan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Plan : {}", id);
        planService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /plans/_search?query=:query} : search for the plan corresponding
     * to the query.
     *
     * @param query the query of the plan search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Plan> searchPlans(@RequestParam("query") String query) {
        LOG.debug("REST request to search Plans for query {}", query);
        try {
            return planService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
