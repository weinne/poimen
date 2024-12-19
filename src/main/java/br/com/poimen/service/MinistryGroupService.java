package br.com.poimen.service;

import br.com.poimen.domain.MinistryGroup;
import br.com.poimen.repository.MinistryGroupRepository;
import br.com.poimen.repository.search.MinistryGroupSearchRepository;
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

    private final MinistryGroupSearchRepository ministryGroupSearchRepository;

    public MinistryGroupService(
        MinistryGroupRepository ministryGroupRepository,
        MinistryGroupSearchRepository ministryGroupSearchRepository
    ) {
        this.ministryGroupRepository = ministryGroupRepository;
        this.ministryGroupSearchRepository = ministryGroupSearchRepository;
    }

    /**
     * Save a ministryGroup.
     *
     * @param ministryGroup the entity to save.
     * @return the persisted entity.
     */
    public MinistryGroup save(MinistryGroup ministryGroup) {
        LOG.debug("Request to save MinistryGroup : {}", ministryGroup);
        ministryGroup = ministryGroupRepository.save(ministryGroup);
        ministryGroupSearchRepository.index(ministryGroup);
        return ministryGroup;
    }

    /**
     * Update a ministryGroup.
     *
     * @param ministryGroup the entity to save.
     * @return the persisted entity.
     */
    public MinistryGroup update(MinistryGroup ministryGroup) {
        LOG.debug("Request to update MinistryGroup : {}", ministryGroup);
        ministryGroup = ministryGroupRepository.save(ministryGroup);
        ministryGroupSearchRepository.index(ministryGroup);
        return ministryGroup;
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
                if (ministryGroup.getType() != null) {
                    existingMinistryGroup.setType(ministryGroup.getType());
                }

                return existingMinistryGroup;
            })
            .map(ministryGroupRepository::save)
            .map(savedMinistryGroup -> {
                ministryGroupSearchRepository.index(savedMinistryGroup);
                return savedMinistryGroup;
            });
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
     * Get all the ministryGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MinistryGroup> findAllWithEagerRelationships(Pageable pageable) {
        return ministryGroupRepository.findAllWithEagerRelationships(pageable);
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
        return ministryGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the ministryGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MinistryGroup : {}", id);
        ministryGroupRepository.deleteById(id);
        ministryGroupSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the ministryGroup corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MinistryGroup> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MinistryGroups for query {}", query);
        return ministryGroupSearchRepository.search(query, pageable);
    }
}
