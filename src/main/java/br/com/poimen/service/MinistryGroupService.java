package br.com.poimen.service;

import br.com.poimen.domain.MinistryGroup;
import br.com.poimen.repository.MinistryGroupRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.MinistryGroup}.
 */
@Service
@Transactional
public class MinistryGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(MinistryGroupService.class);

    private final MinistryGroupRepository ministryGroupRepository;

    public MinistryGroupService(MinistryGroupRepository ministryGroupRepository) {
        this.ministryGroupRepository = ministryGroupRepository;
    }

    /**
     * Save a ministryGroup.
     *
     * @param ministryGroup the entity to save.
     * @return the persisted entity.
     */
    public MinistryGroup save(MinistryGroup ministryGroup) {
        LOG.debug("Request to save MinistryGroup : {}", ministryGroup);
        return ministryGroupRepository.save(ministryGroup);
    }

    /**
     * Update a ministryGroup.
     *
     * @param ministryGroup the entity to save.
     * @return the persisted entity.
     */
    public MinistryGroup update(MinistryGroup ministryGroup) {
        LOG.debug("Request to update MinistryGroup : {}", ministryGroup);
        return ministryGroupRepository.save(ministryGroup);
    }

    /**
     * Partially update a ministryGroup.
     *
     * @param ministryGroup the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MinistryGroup> partialUpdate(MinistryGroup ministryGroup) {
        LOG.debug("Request to partially update MinistryGroup : {}", ministryGroup);

        return ministryGroupRepository
            .findById(ministryGroup.getId())
            .map(existingMinistryGroup -> {
                if (ministryGroup.getName() != null) {
                    existingMinistryGroup.setName(ministryGroup.getName());
                }
                if (ministryGroup.getDescription() != null) {
                    existingMinistryGroup.setDescription(ministryGroup.getDescription());
                }
                if (ministryGroup.getEstablishedDate() != null) {
                    existingMinistryGroup.setEstablishedDate(ministryGroup.getEstablishedDate());
                }
                if (ministryGroup.getLeader() != null) {
                    existingMinistryGroup.setLeader(ministryGroup.getLeader());
                }
                if (ministryGroup.getType() != null) {
                    existingMinistryGroup.setType(ministryGroup.getType());
                }

                return existingMinistryGroup;
            })
            .map(ministryGroupRepository::save);
    }

    /**
     * Get all the ministryGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MinistryGroup> findAll(Pageable pageable) {
        LOG.debug("Request to get all MinistryGroups");
        return ministryGroupRepository.findAll(pageable);
    }

    /**
     * Get one ministryGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MinistryGroup> findOne(Long id) {
        LOG.debug("Request to get MinistryGroup : {}", id);
        return ministryGroupRepository.findById(id);
    }

    /**
     * Delete the ministryGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MinistryGroup : {}", id);
        ministryGroupRepository.deleteById(id);
    }
}
