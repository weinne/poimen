package br.com.poimen.repository;

import br.com.poimen.domain.MinistryGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MinistryGroup entity.
 *
 * When extending this class, extend MinistryGroupRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MinistryGroupRepository extends MinistryGroupRepositoryWithBagRelationships, JpaRepository<MinistryGroup, Long> {
    default Optional<MinistryGroup> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<MinistryGroup> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<MinistryGroup> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select ministryGroup from MinistryGroup ministryGroup left join fetch ministryGroup.church left join fetch ministryGroup.president left join fetch ministryGroup.supervisor",
        countQuery = "select count(ministryGroup) from MinistryGroup ministryGroup"
    )
    Page<MinistryGroup> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select ministryGroup from MinistryGroup ministryGroup left join fetch ministryGroup.church left join fetch ministryGroup.president left join fetch ministryGroup.supervisor"
    )
    List<MinistryGroup> findAllWithToOneRelationships();

    @Query(
        "select ministryGroup from MinistryGroup ministryGroup left join fetch ministryGroup.church left join fetch ministryGroup.president left join fetch ministryGroup.supervisor where ministryGroup.id =:id"
    )
    Optional<MinistryGroup> findOneWithToOneRelationships(@Param("id") Long id);
}
