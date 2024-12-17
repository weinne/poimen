package br.com.poimen.repository;

import br.com.poimen.domain.MinistryMembership;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MinistryMembership entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MinistryMembershipRepository extends JpaRepository<MinistryMembership, Long> {}
