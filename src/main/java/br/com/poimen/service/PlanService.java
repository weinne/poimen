package br.com.poimen.service;

import br.com.poimen.domain.Plan;
import br.com.poimen.repository.PlanRepository;
import br.com.poimen.repository.search.PlanSearchRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.Plan}.
 */
@Service
@Transactional
public class PlanService {

    private static final Logger LOG = LoggerFactory.getLogger(PlanService.class);

    private final PlanRepository planRepository;

    private final PlanSearchRepository planSearchRepository;

    public PlanService(PlanRepository planRepository, PlanSearchRepository planSearchRepository) {
        this.planRepository = planRepository;
        this.planSearchRepository = planSearchRepository;
    }

    /**
     * Save a plan.
     *
     * @param plan the entity to save.
     * @return the persisted entity.
     */
    public Plan save(Plan plan) {
        LOG.debug("Request to save Plan : {}", plan);
        plan = planRepository.save(plan);
        planSearchRepository.index(plan);
        return plan;
    }

    /**
     * Update a plan.
     *
     * @param plan the entity to save.
     * @return the persisted entity.
     */
    public Plan update(Plan plan) {
        LOG.debug("Request to update Plan : {}", plan);
        plan = planRepository.save(plan);
        planSearchRepository.index(plan);
        return plan;
    }

    /**
     * Partially update a plan.
     *
     * @param plan the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Plan> partialUpdate(Plan plan) {
        LOG.debug("Request to partially update Plan : {}", plan);

        return planRepository
            .findById(plan.getId())
            .map(existingPlan -> {
                if (plan.getName() != null) {
                    existingPlan.setName(plan.getName());
                }
                if (plan.getPrice() != null) {
                    existingPlan.setPrice(plan.getPrice());
                }
                if (plan.getDescription() != null) {
                    existingPlan.setDescription(plan.getDescription());
                }
                if (plan.getFeatures() != null) {
                    existingPlan.setFeatures(plan.getFeatures());
                }
                if (plan.getRenewalPeriod() != null) {
                    existingPlan.setRenewalPeriod(plan.getRenewalPeriod());
                }

                return existingPlan;
            })
            .map(planRepository::save)
            .map(savedPlan -> {
                planSearchRepository.index(savedPlan);
                return savedPlan;
            });
    }

    /**
     * Get all the plans.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Plan> findAll() {
        LOG.debug("Request to get all Plans");
        return planRepository.findAll();
    }

    /**
     * Get one plan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Plan> findOne(Long id) {
        LOG.debug("Request to get Plan : {}", id);
        return planRepository.findById(id);
    }

    /**
     * Delete the plan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Plan : {}", id);
        planRepository.deleteById(id);
        planSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the plan corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Plan> search(String query) {
        LOG.debug("Request to search Plans for query {}", query);
        try {
            return StreamSupport.stream(planSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
