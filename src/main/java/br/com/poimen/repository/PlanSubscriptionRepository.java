package br.com.poimen.repository;

import br.com.poimen.domain.PlanSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlanSubscription entity.
 */
@Repository
public interface PlanSubscriptionRepository extends JpaRepository<PlanSubscription, Long> {
    default Optional<PlanSubscription> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PlanSubscription> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PlanSubscription> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select planSubscription from PlanSubscription planSubscription left join fetch planSubscription.church left join fetch planSubscription.plan left join fetch planSubscription.user",
        countQuery = "select count(planSubscription) from PlanSubscription planSubscription"
    )
    Page<PlanSubscription> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select planSubscription from PlanSubscription planSubscription left join fetch planSubscription.church left join fetch planSubscription.plan left join fetch planSubscription.user"
    )
    List<PlanSubscription> findAllWithToOneRelationships();

    @Query(
        "select planSubscription from PlanSubscription planSubscription left join fetch planSubscription.church left join fetch planSubscription.plan left join fetch planSubscription.user where planSubscription.id =:id"
    )
    Optional<PlanSubscription> findOneWithToOneRelationships(@Param("id") Long id);
}
