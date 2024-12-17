package br.com.poimen.service;

import br.com.poimen.domain.Plan;
import br.com.poimen.repository.PlanRepository;
import java.util.List;
import java.util.Optional;
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

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    /**
     * Save a plan.
     *
     * @param plan the entity to save.
     * @return the persisted entity.
     */
    public Plan save(Plan plan) {
        LOG.debug("Request to save Plan : {}", plan);
        return planRepository.save(plan);
    }

    /**
     * Update a plan.
     *
     * @param plan the entity to save.
     * @return the persisted entity.
     */
    public Plan update(Plan plan) {
        LOG.debug("Request to update Plan : {}", plan);
        return planRepository.save(plan);
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

                return existingPlan;
            })
            .map(planRepository::save);
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
    }
}
