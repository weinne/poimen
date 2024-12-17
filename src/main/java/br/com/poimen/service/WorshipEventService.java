package br.com.poimen.service;

import br.com.poimen.domain.WorshipEvent;
import br.com.poimen.repository.WorshipEventRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.WorshipEvent}.
 */
@Service
@Transactional
public class WorshipEventService {

    private static final Logger LOG = LoggerFactory.getLogger(WorshipEventService.class);

    private final WorshipEventRepository worshipEventRepository;

    public WorshipEventService(WorshipEventRepository worshipEventRepository) {
        this.worshipEventRepository = worshipEventRepository;
    }

    /**
     * Save a worshipEvent.
     *
     * @param worshipEvent the entity to save.
     * @return the persisted entity.
     */
    public WorshipEvent save(WorshipEvent worshipEvent) {
        LOG.debug("Request to save WorshipEvent : {}", worshipEvent);
        return worshipEventRepository.save(worshipEvent);
    }

    /**
     * Update a worshipEvent.
     *
     * @param worshipEvent the entity to save.
     * @return the persisted entity.
     */
    public WorshipEvent update(WorshipEvent worshipEvent) {
        LOG.debug("Request to update WorshipEvent : {}", worshipEvent);
        return worshipEventRepository.save(worshipEvent);
    }

    /**
     * Partially update a worshipEvent.
     *
     * @param worshipEvent the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorshipEvent> partialUpdate(WorshipEvent worshipEvent) {
        LOG.debug("Request to partially update WorshipEvent : {}", worshipEvent);

        return worshipEventRepository
            .findById(worshipEvent.getId())
            .map(existingWorshipEvent -> {
                if (worshipEvent.getDate() != null) {
                    existingWorshipEvent.setDate(worshipEvent.getDate());
                }
                if (worshipEvent.getTitle() != null) {
                    existingWorshipEvent.setTitle(worshipEvent.getTitle());
                }
                if (worshipEvent.getDescription() != null) {
                    existingWorshipEvent.setDescription(worshipEvent.getDescription());
                }
                if (worshipEvent.getWorshipType() != null) {
                    existingWorshipEvent.setWorshipType(worshipEvent.getWorshipType());
                }

                return existingWorshipEvent;
            })
            .map(worshipEventRepository::save);
    }

    /**
     * Get all the worshipEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorshipEvent> findAll(Pageable pageable) {
        LOG.debug("Request to get all WorshipEvents");
        return worshipEventRepository.findAll(pageable);
    }

    /**
     * Get all the worshipEvents with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WorshipEvent> findAllWithEagerRelationships(Pageable pageable) {
        return worshipEventRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one worshipEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorshipEvent> findOne(Long id) {
        LOG.debug("Request to get WorshipEvent : {}", id);
        return worshipEventRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the worshipEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WorshipEvent : {}", id);
        worshipEventRepository.deleteById(id);
    }
}
