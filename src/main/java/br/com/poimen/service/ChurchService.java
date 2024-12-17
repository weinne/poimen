package br.com.poimen.service;

import br.com.poimen.domain.Church;
import br.com.poimen.repository.ChurchRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ChurchService(ChurchRepository churchRepository) {
        this.churchRepository = churchRepository;
    }

    /**
     * Save a church.
     *
     * @param church the entity to save.
     * @return the persisted entity.
     */
    public Church save(Church church) {
        LOG.debug("Request to save Church : {}", church);
        return churchRepository.save(church);
    }

    /**
     * Update a church.
     *
     * @param church the entity to save.
     * @return the persisted entity.
     */
    public Church update(Church church) {
        LOG.debug("Request to update Church : {}", church);
        return churchRepository.save(church);
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

                return existingChurch;
            })
            .map(churchRepository::save);
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
     * Get all the churches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Church> findAllWithEagerRelationships(Pageable pageable) {
        return churchRepository.findAllWithEagerRelationships(pageable);
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
        return churchRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the church by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Church : {}", id);
        churchRepository.deleteById(id);
    }
}
