package br.com.poimen.service;

import br.com.poimen.domain.PlanSubscription;
import br.com.poimen.repository.PlanSubscriptionRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.PlanSubscription}.
 */
@Service
@Transactional
public class PlanSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(PlanSubscriptionService.class);

    private final PlanSubscriptionRepository planSubscriptionRepository;

    public PlanSubscriptionService(PlanSubscriptionRepository planSubscriptionRepository) {
        this.planSubscriptionRepository = planSubscriptionRepository;
    }

    /**
     * Save a planSubscription.
     *
     * @param planSubscription the entity to save.
     * @return the persisted entity.
     */
    public PlanSubscription save(PlanSubscription planSubscription) {
        LOG.debug("Request to save PlanSubscription : {}", planSubscription);
        return planSubscriptionRepository.save(planSubscription);
    }

    /**
     * Update a planSubscription.
     *
     * @param planSubscription the entity to save.
     * @return the persisted entity.
     */
    public PlanSubscription update(PlanSubscription planSubscription) {
        LOG.debug("Request to update PlanSubscription : {}", planSubscription);
        return planSubscriptionRepository.save(planSubscription);
    }

    /**
     * Partially update a planSubscription.
     *
     * @param planSubscription the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlanSubscription> partialUpdate(PlanSubscription planSubscription) {
        LOG.debug("Request to partially update PlanSubscription : {}", planSubscription);

        return planSubscriptionRepository
            .findById(planSubscription.getId())
            .map(existingPlanSubscription -> {
                if (planSubscription.getStartDate() != null) {
                    existingPlanSubscription.setStartDate(planSubscription.getStartDate());
                }
                if (planSubscription.getEndDate() != null) {
                    existingPlanSubscription.setEndDate(planSubscription.getEndDate());
                }
                if (planSubscription.getActive() != null) {
                    existingPlanSubscription.setActive(planSubscription.getActive());
                }
                if (planSubscription.getPlanName() != null) {
                    existingPlanSubscription.setPlanName(planSubscription.getPlanName());
                }

                return existingPlanSubscription;
            })
            .map(planSubscriptionRepository::save);
    }

    /**
     * Get all the planSubscriptions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PlanSubscription> findAll() {
        LOG.debug("Request to get all PlanSubscriptions");
        return planSubscriptionRepository.findAll();
    }

    /**
     * Get one planSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlanSubscription> findOne(Long id) {
        LOG.debug("Request to get PlanSubscription : {}", id);
        return planSubscriptionRepository.findById(id);
    }

    /**
     * Delete the planSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PlanSubscription : {}", id);
        planSubscriptionRepository.deleteById(id);
    }
}
