package br.com.poimen.repository;

import br.com.poimen.domain.PlanSubscription;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlanSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanSubscriptionRepository extends JpaRepository<PlanSubscription, Long> {
    @Query("select planSubscription from PlanSubscription planSubscription where planSubscription.user.login = ?#{authentication.name}")
    List<PlanSubscription> findByUserIsCurrentUser();
}
