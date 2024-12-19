package br.com.poimen.service;

import br.com.poimen.domain.Church;
import br.com.poimen.repository.ChurchRepository;
import br.com.poimen.repository.search.ChurchSearchRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.Church}.
 */
@Service
@Transactional
public class ChurchService {

    private static final Logger LOG = LoggerFactory.getLogger(ChurchService.class);

    private final ChurchRepository churchRepository;

    private final ChurchSearchRepository churchSearchRepository;

    public ChurchService(ChurchRepository churchRepository, ChurchSearchRepository churchSearchRepository) {
        this.churchRepository = churchRepository;
        this.churchSearchRepository = churchSearchRepository;
    }

    /**
     * Save a church.
     *
     * @param church the entity to save.
     * @return the persisted entity.
     */
    public Church save(Church church) {
        LOG.debug("Request to save Church : {}", church);
        church = churchRepository.save(church);
        churchSearchRepository.index(church);
        return church;
    }

    /**
     * Update a church.
     *
     * @param church the entity to save.
     * @return the persisted entity.
     */
    public Church update(Church church) {
        LOG.debug("Request to update Church : {}", church);
        church = churchRepository.save(church);
        churchSearchRepository.index(church);
        return church;
    }

    /**
     * Partially update a church.
     *
     * @param church the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Church> partialUpdate(Church church) {
        LOG.debug("Request to partially update Church : {}", church);

        return churchRepository
            .findById(church.getId())
            .map(existingChurch -> {
                if (church.getName() != null) {
                    existingChurch.setName(church.getName());
                }
                if (church.getCnpj() != null) {
                    existingChurch.setCnpj(church.getCnpj());
                }
                if (church.getAddress() != null) {
                    existingChurch.setAddress(church.getAddress());
                }
                if (church.getCity() != null) {
                    existingChurch.setCity(church.getCity());
                }
                if (church.getDateFoundation() != null) {
                    existingChurch.setDateFoundation(church.getDateFoundation());
                }
                if (church.getPhone() != null) {
                    existingChurch.setPhone(church.getPhone());
                }
                if (church.getEmail() != null) {
                    existingChurch.setEmail(church.getEmail());
                }
                if (church.getWebsite() != null) {
                    existingChurch.setWebsite(church.getWebsite());
                }
                if (church.getFacebook() != null) {
                    existingChurch.setFacebook(church.getFacebook());
                }
                if (church.getInstagram() != null) {
                    existingChurch.setInstagram(church.getInstagram());
                }
                if (church.getTwitter() != null) {
                    existingChurch.setTwitter(church.getTwitter());
                }
                if (church.getYoutube() != null) {
                    existingChurch.setYoutube(church.getYoutube());
                }
                if (church.getAbout() != null) {
                    existingChurch.setAbout(church.getAbout());
                }

                return existingChurch;
            })
            .map(churchRepository::save)
            .map(savedChurch -> {
                churchSearchRepository.index(savedChurch);
                return savedChurch;
            });
    }

    /**
     * Get all the churches.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Church> findAll() {
        LOG.debug("Request to get all Churches");
        return churchRepository.findAll();
    }

    /**
     * Get one church by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Church> findOne(Long id) {
        LOG.debug("Request to get Church : {}", id);
        return churchRepository.findById(id);
    }

    /**
     * Delete the church by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Church : {}", id);
        churchRepository.deleteById(id);
        churchSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the church corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Church> search(String query) {
        LOG.debug("Request to search Churches for query {}", query);
        try {
            return StreamSupport.stream(churchSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
