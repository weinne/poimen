package br.com.poimen.service;

import br.com.poimen.domain.Hymn;
import br.com.poimen.repository.HymnRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.Hymn}.
 */
@Service
@Transactional
public class HymnService {

    private static final Logger LOG = LoggerFactory.getLogger(HymnService.class);

    private final HymnRepository hymnRepository;

    public HymnService(HymnRepository hymnRepository) {
        this.hymnRepository = hymnRepository;
    }

    /**
     * Save a hymn.
     *
     * @param hymn the entity to save.
     * @return the persisted entity.
     */
    public Hymn save(Hymn hymn) {
        LOG.debug("Request to save Hymn : {}", hymn);
        return hymnRepository.save(hymn);
    }

    /**
     * Update a hymn.
     *
     * @param hymn the entity to save.
     * @return the persisted entity.
     */
    public Hymn update(Hymn hymn) {
        LOG.debug("Request to update Hymn : {}", hymn);
        return hymnRepository.save(hymn);
    }

    /**
     * Partially update a hymn.
     *
     * @param hymn the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Hymn> partialUpdate(Hymn hymn) {
        LOG.debug("Request to partially update Hymn : {}", hymn);

        return hymnRepository
            .findById(hymn.getId())
            .map(existingHymn -> {
                if (hymn.getTitle() != null) {
                    existingHymn.setTitle(hymn.getTitle());
                }
                if (hymn.getAuthor() != null) {
                    existingHymn.setAuthor(hymn.getAuthor());
                }
                if (hymn.getHymnNumber() != null) {
                    existingHymn.setHymnNumber(hymn.getHymnNumber());
                }
                if (hymn.getLyrics() != null) {
                    existingHymn.setLyrics(hymn.getLyrics());
                }

                return existingHymn;
            })
            .map(hymnRepository::save);
    }

    /**
     * Get all the hymns.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Hymn> findAll() {
        LOG.debug("Request to get all Hymns");
        return hymnRepository.findAll();
    }

    /**
     * Get one hymn by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Hymn> findOne(Long id) {
        LOG.debug("Request to get Hymn : {}", id);
        return hymnRepository.findById(id);
    }

    /**
     * Delete the hymn by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Hymn : {}", id);
        hymnRepository.deleteById(id);
    }
}
