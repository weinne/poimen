package br.com.poimen.service;

import br.com.poimen.domain.WorshipEvent;
import br.com.poimen.repository.WorshipEventRepository;
import br.com.poimen.repository.search.WorshipEventSearchRepository;
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

    private final WorshipEventSearchRepository worshipEventSearchRepository;

    public WorshipEventService(WorshipEventRepository worshipEventRepository, WorshipEventSearchRepository worshipEventSearchRepository) {
        this.worshipEventRepository = worshipEventRepository;
        this.worshipEventSearchRepository = worshipEventSearchRepository;
    }

    /**
     * Save a worshipEvent.
     *
     * @param worshipEvent the entity to save.
     * @return the persisted entity.
     */
    public WorshipEvent save(WorshipEvent worshipEvent) {
        LOG.debug("Request to save WorshipEvent : {}", worshipEvent);
        worshipEvent = worshipEventRepository.save(worshipEvent);
        worshipEventSearchRepository.index(worshipEvent);
        return worshipEvent;
    }

    /**
     * Update a worshipEvent.
     *
     * @param worshipEvent the entity to save.
     * @return the persisted entity.
     */
    public WorshipEvent update(WorshipEvent worshipEvent) {
        LOG.debug("Request to update WorshipEvent : {}", worshipEvent);
        worshipEvent = worshipEventRepository.save(worshipEvent);
        worshipEventSearchRepository.index(worshipEvent);
        return worshipEvent;
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
                if (worshipEvent.getGuestPreacher() != null) {
                    existingWorshipEvent.setGuestPreacher(worshipEvent.getGuestPreacher());
                }
                if (worshipEvent.getDescription() != null) {
                    existingWorshipEvent.setDescription(worshipEvent.getDescription());
                }
                if (worshipEvent.getCallToWorshipText() != null) {
                    existingWorshipEvent.setCallToWorshipText(worshipEvent.getCallToWorshipText());
                }
                if (worshipEvent.getConfessionOfSinText() != null) {
                    existingWorshipEvent.setConfessionOfSinText(worshipEvent.getConfessionOfSinText());
                }
                if (worshipEvent.getAssuranceOfPardonText() != null) {
                    existingWorshipEvent.setAssuranceOfPardonText(worshipEvent.getAssuranceOfPardonText());
                }
                if (worshipEvent.getLordSupperText() != null) {
                    existingWorshipEvent.setLordSupperText(worshipEvent.getLordSupperText());
                }
                if (worshipEvent.getBenedictionText() != null) {
                    existingWorshipEvent.setBenedictionText(worshipEvent.getBenedictionText());
                }
                if (worshipEvent.getConfessionalText() != null) {
                    existingWorshipEvent.setConfessionalText(worshipEvent.getConfessionalText());
                }
                if (worshipEvent.getSermonText() != null) {
                    existingWorshipEvent.setSermonText(worshipEvent.getSermonText());
                }
                if (worshipEvent.getSermonFile() != null) {
                    existingWorshipEvent.setSermonFile(worshipEvent.getSermonFile());
                }
                if (worshipEvent.getSermonFileContentType() != null) {
                    existingWorshipEvent.setSermonFileContentType(worshipEvent.getSermonFileContentType());
                }
                if (worshipEvent.getSermonLink() != null) {
                    existingWorshipEvent.setSermonLink(worshipEvent.getSermonLink());
                }
                if (worshipEvent.getYoutubeLink() != null) {
                    existingWorshipEvent.setYoutubeLink(worshipEvent.getYoutubeLink());
                }
                if (worshipEvent.getBulletinFile() != null) {
                    existingWorshipEvent.setBulletinFile(worshipEvent.getBulletinFile());
                }
                if (worshipEvent.getBulletinFileContentType() != null) {
                    existingWorshipEvent.setBulletinFileContentType(worshipEvent.getBulletinFileContentType());
                }
                if (worshipEvent.getWorshipType() != null) {
                    existingWorshipEvent.setWorshipType(worshipEvent.getWorshipType());
                }

                return existingWorshipEvent;
            })
            .map(worshipEventRepository::save)
            .map(savedWorshipEvent -> {
                worshipEventSearchRepository.index(savedWorshipEvent);
                return savedWorshipEvent;
            });
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
        worshipEventSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the worshipEvent corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorshipEvent> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of WorshipEvents for query {}", query);
        return worshipEventSearchRepository.search(query, pageable);
    }
}
