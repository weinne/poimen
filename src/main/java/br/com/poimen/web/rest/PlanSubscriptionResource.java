package br.com.poimen.web.rest;

import br.com.poimen.domain.PlanSubscription;
import br.com.poimen.repository.PlanSubscriptionRepository;
import br.com.poimen.service.PlanSubscriptionService;
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
 * REST controller for managing {@link br.com.poimen.domain.PlanSubscription}.
 */
@RestController
@RequestMapping("/api/plan-subscriptions")
public class PlanSubscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlanSubscriptionResource.class);

    private static final String ENTITY_NAME = "planSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanSubscriptionService planSubscriptionService;

    private final PlanSubscriptionRepository planSubscriptionRepository;

    public PlanSubscriptionResource(
        PlanSubscriptionService planSubscriptionService,
        PlanSubscriptionRepository planSubscriptionRepository
    ) {
        this.planSubscriptionService = planSubscriptionService;
        this.planSubscriptionRepository = planSubscriptionRepository;
    }

    /**
     * {@code POST  /plan-subscriptions} : Create a new planSubscription.
     *
     * @param planSubscription the planSubscription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planSubscription, or with status {@code 400 (Bad Request)} if the planSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlanSubscription> createPlanSubscription(@Valid @RequestBody PlanSubscription planSubscription)
        throws URISyntaxException {
        LOG.debug("REST request to save PlanSubscription : {}", planSubscription);
        if (planSubscription.getId() != null) {
            throw new BadRequestAlertException("A new planSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        planSubscription = planSubscriptionService.save(planSubscription);
        return ResponseEntity.created(new URI("/api/plan-subscriptions/" + planSubscription.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, planSubscription.getId().toString()))
            .body(planSubscription);
    }

    /**
     * {@code PUT  /plan-subscriptions/:id} : Updates an existing planSubscription.
     *
     * @param id the id of the planSubscription to save.
     * @param planSubscription the planSubscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planSubscription,
     * or with status {@code 400 (Bad Request)} if the planSubscription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planSubscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlanSubscription> updatePlanSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlanSubscription planSubscription
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlanSubscription : {}, {}", id, planSubscription);
        if (planSubscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planSubscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        planSubscription = planSubscriptionService.update(planSubscription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planSubscription.getId().toString()))
            .body(planSubscription);
    }

    /**
     * {@code PATCH  /plan-subscriptions/:id} : Partial updates given fields of an existing planSubscription, field will ignore if it is null
     *
     * @param id the id of the planSubscription to save.
     * @param planSubscription the planSubscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planSubscription,
     * or with status {@code 400 (Bad Request)} if the planSubscription is not valid,
     * or with status {@code 404 (Not Found)} if the planSubscription is not found,
     * or with status {@code 500 (Internal Server Error)} if the planSubscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlanSubscription> partialUpdatePlanSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlanSubscription planSubscription
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlanSubscription partially : {}, {}", id, planSubscription);
        if (planSubscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planSubscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanSubscription> result = planSubscriptionService.partialUpdate(planSubscription);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planSubscription.getId().toString())
        );
    }

    /**
     * {@code GET  /plan-subscriptions} : get all the planSubscriptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planSubscriptions in body.
     */
    @GetMapping("")
    public List<PlanSubscription> getAllPlanSubscriptions() {
        LOG.debug("REST request to get all PlanSubscriptions");
        return planSubscriptionService.findAll();
    }

    /**
     * {@code GET  /plan-subscriptions/:id} : get the "id" planSubscription.
     *
     * @param id the id of the planSubscription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planSubscription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanSubscription> getPlanSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PlanSubscription : {}", id);
        Optional<PlanSubscription> planSubscription = planSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planSubscription);
    }

    /**
     * {@code DELETE  /plan-subscriptions/:id} : delete the "id" planSubscription.
     *
     * @param id the id of the planSubscription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PlanSubscription : {}", id);
        planSubscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
