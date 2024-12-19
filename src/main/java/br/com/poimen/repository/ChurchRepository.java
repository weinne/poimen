package br.com.poimen.repository;

import br.com.poimen.domain.Church;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Church entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChurchRepository extends JpaRepository<Church, Long> {}
