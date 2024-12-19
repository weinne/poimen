package br.com.poimen.repository;

import br.com.poimen.domain.WorshipEvent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorshipEvent entity.
 *
 * When extending this class, extend WorshipEventRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface WorshipEventRepository extends WorshipEventRepositoryWithBagRelationships, JpaRepository<WorshipEvent, Long> {
    default Optional<WorshipEvent> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<WorshipEvent> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<WorshipEvent> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select worshipEvent from WorshipEvent worshipEvent left join fetch worshipEvent.church left join fetch worshipEvent.preacher left join fetch worshipEvent.liturgist",
        countQuery = "select count(worshipEvent) from WorshipEvent worshipEvent"
    )
    Page<WorshipEvent> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select worshipEvent from WorshipEvent worshipEvent left join fetch worshipEvent.church left join fetch worshipEvent.preacher left join fetch worshipEvent.liturgist"
    )
    List<WorshipEvent> findAllWithToOneRelationships();

    @Query(
        "select worshipEvent from WorshipEvent worshipEvent left join fetch worshipEvent.church left join fetch worshipEvent.preacher left join fetch worshipEvent.liturgist where worshipEvent.id =:id"
    )
    Optional<WorshipEvent> findOneWithToOneRelationships(@Param("id") Long id);
}
