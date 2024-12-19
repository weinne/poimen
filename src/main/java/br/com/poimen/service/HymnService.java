package br.com.poimen.service;

import br.com.poimen.domain.Hymn;
import br.com.poimen.repository.HymnRepository;
import br.com.poimen.repository.search.HymnSearchRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
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

    private final HymnSearchRepository hymnSearchRepository;

    public HymnService(HymnRepository hymnRepository, HymnSearchRepository hymnSearchRepository) {
        this.hymnRepository = hymnRepository;
        this.hymnSearchRepository = hymnSearchRepository;
    }

    /**
     * Save a hymn.
     *
     * @param hymn the entity to save.
     * @return the persisted entity.
     */
    public Hymn save(Hymn hymn) {
        LOG.debug("Request to save Hymn : {}", hymn);
        hymn = hymnRepository.save(hymn);
        hymnSearchRepository.index(hymn);
        return hymn;
    }

    /**
     * Update a hymn.
     *
     * @param hymn the entity to save.
     * @return the persisted entity.
     */
    public Hymn update(Hymn hymn) {
        LOG.debug("Request to update Hymn : {}", hymn);
        hymn = hymnRepository.save(hymn);
        hymnSearchRepository.index(hymn);
        return hymn;
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
                if (hymn.getLyricsAuthor() != null) {
                    existingHymn.setLyricsAuthor(hymn.getLyricsAuthor());
                }
                if (hymn.getMusicAuthor() != null) {
                    existingHymn.setMusicAuthor(hymn.getMusicAuthor());
                }
                if (hymn.getHymnary() != null) {
                    existingHymn.setHymnary(hymn.getHymnary());
                }
                if (hymn.getHymnNumber() != null) {
                    existingHymn.setHymnNumber(hymn.getHymnNumber());
                }
                if (hymn.getLink() != null) {
                    existingHymn.setLink(hymn.getLink());
                }
                if (hymn.getYoutubeLink() != null) {
                    existingHymn.setYoutubeLink(hymn.getYoutubeLink());
                }
                if (hymn.getSheetMusic() != null) {
                    existingHymn.setSheetMusic(hymn.getSheetMusic());
                }
                if (hymn.getSheetMusicContentType() != null) {
                    existingHymn.setSheetMusicContentType(hymn.getSheetMusicContentType());
                }
                if (hymn.getMidi() != null) {
                    existingHymn.setMidi(hymn.getMidi());
                }
                if (hymn.getMidiContentType() != null) {
                    existingHymn.setMidiContentType(hymn.getMidiContentType());
                }
                if (hymn.getTone() != null) {
                    existingHymn.setTone(hymn.getTone());
                }
                if (hymn.getLyrics() != null) {
                    existingHymn.setLyrics(hymn.getLyrics());
                }

                return existingHymn;
            })
            .map(hymnRepository::save)
            .map(savedHymn -> {
                hymnSearchRepository.index(savedHymn);
                return savedHymn;
            });
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
        hymnSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the hymn corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Hymn> search(String query) {
        LOG.debug("Request to search Hymns for query {}", query);
        try {
            return StreamSupport.stream(hymnSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
