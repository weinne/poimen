package br.com.poimen.repository;

import br.com.poimen.domain.MinistryGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MinistryGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MinistryGroupRepository extends JpaRepository<MinistryGroup, Long> {}
