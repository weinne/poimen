package br.com.poimen.repository;

import br.com.poimen.domain.Church;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ChurchRepositoryWithBagRelationships {
    Optional<Church> fetchBagRelationships(Optional<Church> church);

    List<Church> fetchBagRelationships(List<Church> churches);

    Page<Church> fetchBagRelationships(Page<Church> churches);
}
