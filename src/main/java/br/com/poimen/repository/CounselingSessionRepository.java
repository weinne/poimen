package br.com.poimen.repository;

import br.com.poimen.domain.CounselingSession;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CounselingSession entity.
 */
@Repository
public interface CounselingSessionRepository extends JpaRepository<CounselingSession, Long> {
    default Optional<CounselingSession> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CounselingSession> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CounselingSession> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select counselingSession from CounselingSession counselingSession left join fetch counselingSession.church left join fetch counselingSession.member left join fetch counselingSession.user",
        countQuery = "select count(counselingSession) from CounselingSession counselingSession"
    )
    Page<CounselingSession> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select counselingSession from CounselingSession counselingSession left join fetch counselingSession.church left join fetch counselingSession.member left join fetch counselingSession.user"
    )
    List<CounselingSession> findAllWithToOneRelationships();

    @Query(
        "select counselingSession from CounselingSession counselingSession left join fetch counselingSession.church left join fetch counselingSession.member left join fetch counselingSession.user where counselingSession.id =:id"
    )
    Optional<CounselingSession> findOneWithToOneRelationships(@Param("id") Long id);
}
