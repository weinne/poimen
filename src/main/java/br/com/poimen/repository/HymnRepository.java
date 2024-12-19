package br.com.poimen.repository;

import br.com.poimen.domain.Hymn;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hymn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HymnRepository extends JpaRepository<Hymn, Long> {}
