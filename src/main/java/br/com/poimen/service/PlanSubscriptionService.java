package br.com.poimen.service;

import br.com.poimen.domain.PlanSubscription;
import br.com.poimen.repository.PlanSubscriptionRepository;
import br.com.poimen.repository.search.PlanSubscriptionSearchRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final PlanSubscriptionSearchRepository planSubscriptionSearchRepository;

    public PlanSubscriptionService(
        PlanSubscriptionRepository planSubscriptionRepository,
        PlanSubscriptionSearchRepository planSubscriptionSearchRepository
    ) {
        this.planSubscriptionRepository = planSubscriptionRepository;
        this.planSubscriptionSearchRepository = planSubscriptionSearchRepository;
    }

    /**
     * Save a planSubscription.
     *
     * @param planSubscription the entity to save.
     * @return the persisted entity.
     */
    public PlanSubscription save(PlanSubscription planSubscription) {
        LOG.debug("Request to save PlanSubscription : {}", planSubscription);
        planSubscription = planSubscriptionRepository.save(planSubscription);
        planSubscriptionSearchRepository.index(planSubscription);
        return planSubscription;
    }

    /**
     * Update a planSubscription.
     *
     * @param planSubscription the entity to save.
     * @return the persisted entity.
     */
    public PlanSubscription update(PlanSubscription planSubscription) {
        LOG.debug("Request to update PlanSubscription : {}", planSubscription);
        planSubscription = planSubscriptionRepository.save(planSubscription);
        planSubscriptionSearchRepository.index(planSubscription);
        return planSubscription;
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
                if (planSubscription.getDescription() != null) {
                    existingPlanSubscription.setDescription(planSubscription.getDescription());
                }
                if (planSubscription.getStartDate() != null) {
                    existingPlanSubscription.setStartDate(planSubscription.getStartDate());
                }
                if (planSubscription.getEndDate() != null) {
                    existingPlanSubscription.setEndDate(planSubscription.getEndDate());
                }
                if (planSubscription.getStatus() != null) {
                    existingPlanSubscription.setStatus(planSubscription.getStatus());
                }
                if (planSubscription.getPaymentProvider() != null) {
                    existingPlanSubscription.setPaymentProvider(planSubscription.getPaymentProvider());
                }
                if (planSubscription.getPaymentStatus() != null) {
                    existingPlanSubscription.setPaymentStatus(planSubscription.getPaymentStatus());
                }
                if (planSubscription.getPaymentReference() != null) {
                    existingPlanSubscription.setPaymentReference(planSubscription.getPaymentReference());
                }

                return existingPlanSubscription;
            })
            .map(planSubscriptionRepository::save)
            .map(savedPlanSubscription -> {
                planSubscriptionSearchRepository.index(savedPlanSubscription);
                return savedPlanSubscription;
            });
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
     * Get all the planSubscriptions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PlanSubscription> findAllWithEagerRelationships(Pageable pageable) {
        return planSubscriptionRepository.findAllWithEagerRelationships(pageable);
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
        return planSubscriptionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the planSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PlanSubscription : {}", id);
        planSubscriptionRepository.deleteById(id);
        planSubscriptionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the planSubscription corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PlanSubscription> search(String query) {
        LOG.debug("Request to search PlanSubscriptions for query {}", query);
        try {
            return StreamSupport.stream(planSubscriptionSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
