package br.com.poimen.service;

import br.com.poimen.domain.MinistryMembership;
import br.com.poimen.repository.MinistryMembershipRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.MinistryMembership}.
 */
@Service
@Transactional
public class MinistryMembershipService {

    private static final Logger LOG = LoggerFactory.getLogger(MinistryMembershipService.class);

    private final MinistryMembershipRepository ministryMembershipRepository;

    public MinistryMembershipService(MinistryMembershipRepository ministryMembershipRepository) {
        this.ministryMembershipRepository = ministryMembershipRepository;
    }

    /**
     * Save a ministryMembership.
     *
     * @param ministryMembership the entity to save.
     * @return the persisted entity.
     */
    public MinistryMembership save(MinistryMembership ministryMembership) {
        LOG.debug("Request to save MinistryMembership : {}", ministryMembership);
        return ministryMembershipRepository.save(ministryMembership);
    }

    /**
     * Update a ministryMembership.
     *
     * @param ministryMembership the entity to save.
     * @return the persisted entity.
     */
    public MinistryMembership update(MinistryMembership ministryMembership) {
        LOG.debug("Request to update MinistryMembership : {}", ministryMembership);
        return ministryMembershipRepository.save(ministryMembership);
    }

    /**
     * Partially update a ministryMembership.
     *
     * @param ministryMembership the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MinistryMembership> partialUpdate(MinistryMembership ministryMembership) {
        LOG.debug("Request to partially update MinistryMembership : {}", ministryMembership);

        return ministryMembershipRepository
            .findById(ministryMembership.getId())
            .map(existingMinistryMembership -> {
                if (ministryMembership.getRole() != null) {
                    existingMinistryMembership.setRole(ministryMembership.getRole());
                }
                if (ministryMembership.getStartDate() != null) {
                    existingMinistryMembership.setStartDate(ministryMembership.getStartDate());
                }
                if (ministryMembership.getEndDate() != null) {
                    existingMinistryMembership.setEndDate(ministryMembership.getEndDate());
                }
                if (ministryMembership.getStatus() != null) {
                    existingMinistryMembership.setStatus(ministryMembership.getStatus());
                }

                return existingMinistryMembership;
            })
            .map(ministryMembershipRepository::save);
    }

    /**
     * Get all the ministryMemberships.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MinistryMembership> findAll() {
        LOG.debug("Request to get all MinistryMemberships");
        return ministryMembershipRepository.findAll();
    }

    /**
     * Get one ministryMembership by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MinistryMembership> findOne(Long id) {
        LOG.debug("Request to get MinistryMembership : {}", id);
        return ministryMembershipRepository.findById(id);
    }

    /**
     * Delete the ministryMembership by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MinistryMembership : {}", id);
        ministryMembershipRepository.deleteById(id);
    }
}
