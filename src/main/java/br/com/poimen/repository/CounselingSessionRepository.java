package br.com.poimen.repository;

import br.com.poimen.domain.CounselingSession;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CounselingSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CounselingSessionRepository extends JpaRepository<CounselingSession, Long> {
    @Query("select counselingSession from CounselingSession counselingSession where counselingSession.user.login = ?#{authentication.name}")
    List<CounselingSession> findByUserIsCurrentUser();
}
