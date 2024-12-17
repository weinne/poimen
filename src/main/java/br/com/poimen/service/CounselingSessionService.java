package br.com.poimen.service;

import br.com.poimen.domain.CounselingSession;
import br.com.poimen.repository.CounselingSessionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.CounselingSession}.
 */
@Service
@Transactional
public class CounselingSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(CounselingSessionService.class);

    private final CounselingSessionRepository counselingSessionRepository;

    public CounselingSessionService(CounselingSessionRepository counselingSessionRepository) {
        this.counselingSessionRepository = counselingSessionRepository;
    }

    /**
     * Save a counselingSession.
     *
     * @param counselingSession the entity to save.
     * @return the persisted entity.
     */
    public CounselingSession save(CounselingSession counselingSession) {
        LOG.debug("Request to save CounselingSession : {}", counselingSession);
        return counselingSessionRepository.save(counselingSession);
    }

    /**
     * Update a counselingSession.
     *
     * @param counselingSession the entity to save.
     * @return the persisted entity.
     */
    public CounselingSession update(CounselingSession counselingSession) {
        LOG.debug("Request to update CounselingSession : {}", counselingSession);
        return counselingSessionRepository.save(counselingSession);
    }

    /**
     * Partially update a counselingSession.
     *
     * @param counselingSession the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CounselingSession> partialUpdate(CounselingSession counselingSession) {
        LOG.debug("Request to partially update CounselingSession : {}", counselingSession);

        return counselingSessionRepository
            .findById(counselingSession.getId())
            .map(existingCounselingSession -> {
                if (counselingSession.getDate() != null) {
                    existingCounselingSession.setDate(counselingSession.getDate());
                }
                if (counselingSession.getNotes() != null) {
                    existingCounselingSession.setNotes(counselingSession.getNotes());
                }

                return existingCounselingSession;
            })
            .map(counselingSessionRepository::save);
    }

    /**
     * Get all the counselingSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CounselingSession> findAll(Pageable pageable) {
        LOG.debug("Request to get all CounselingSessions");
        return counselingSessionRepository.findAll(pageable);
    }

    /**
     * Get one counselingSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CounselingSession> findOne(Long id) {
        LOG.debug("Request to get CounselingSession : {}", id);
        return counselingSessionRepository.findById(id);
    }

    /**
     * Delete the counselingSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CounselingSession : {}", id);
        counselingSessionRepository.deleteById(id);
    }
}
