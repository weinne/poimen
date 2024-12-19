package br.com.poimen.service;

import br.com.poimen.domain.CounselingSession;
import br.com.poimen.repository.CounselingSessionRepository;
import br.com.poimen.repository.search.CounselingSessionSearchRepository;
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

    private final CounselingSessionSearchRepository counselingSessionSearchRepository;

    public CounselingSessionService(
        CounselingSessionRepository counselingSessionRepository,
        CounselingSessionSearchRepository counselingSessionSearchRepository
    ) {
        this.counselingSessionRepository = counselingSessionRepository;
        this.counselingSessionSearchRepository = counselingSessionSearchRepository;
    }

    /**
     * Save a counselingSession.
     *
     * @param counselingSession the entity to save.
     * @return the persisted entity.
     */
    public CounselingSession save(CounselingSession counselingSession) {
        LOG.debug("Request to save CounselingSession : {}", counselingSession);
        counselingSession = counselingSessionRepository.save(counselingSession);
        counselingSessionSearchRepository.index(counselingSession);
        return counselingSession;
    }

    /**
     * Update a counselingSession.
     *
     * @param counselingSession the entity to save.
     * @return the persisted entity.
     */
    public CounselingSession update(CounselingSession counselingSession) {
        LOG.debug("Request to update CounselingSession : {}", counselingSession);
        counselingSession = counselingSessionRepository.save(counselingSession);
        counselingSessionSearchRepository.index(counselingSession);
        return counselingSession;
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
                if (counselingSession.getSubject() != null) {
                    existingCounselingSession.setSubject(counselingSession.getSubject());
                }
                if (counselingSession.getDate() != null) {
                    existingCounselingSession.setDate(counselingSession.getDate());
                }
                if (counselingSession.getStartTime() != null) {
                    existingCounselingSession.setStartTime(counselingSession.getStartTime());
                }
                if (counselingSession.getEndTime() != null) {
                    existingCounselingSession.setEndTime(counselingSession.getEndTime());
                }
                if (counselingSession.getNotes() != null) {
                    existingCounselingSession.setNotes(counselingSession.getNotes());
                }
                if (counselingSession.getCounselingTasks() != null) {
                    existingCounselingSession.setCounselingTasks(counselingSession.getCounselingTasks());
                }
                if (counselingSession.getStatus() != null) {
                    existingCounselingSession.setStatus(counselingSession.getStatus());
                }

                return existingCounselingSession;
            })
            .map(counselingSessionRepository::save)
            .map(savedCounselingSession -> {
                counselingSessionSearchRepository.index(savedCounselingSession);
                return savedCounselingSession;
            });
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
     * Get all the counselingSessions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CounselingSession> findAllWithEagerRelationships(Pageable pageable) {
        return counselingSessionRepository.findAllWithEagerRelationships(pageable);
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
        return counselingSessionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the counselingSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CounselingSession : {}", id);
        counselingSessionRepository.deleteById(id);
        counselingSessionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the counselingSession corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CounselingSession> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of CounselingSessions for query {}", query);
        return counselingSessionSearchRepository.search(query, pageable);
    }
}
