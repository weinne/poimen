package br.com.poimen.repository;

import br.com.poimen.domain.WorshipEvent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface WorshipEventRepositoryWithBagRelationships {
    Optional<WorshipEvent> fetchBagRelationships(Optional<WorshipEvent> worshipEvent);

    List<WorshipEvent> fetchBagRelationships(List<WorshipEvent> worshipEvents);

    Page<WorshipEvent> fetchBagRelationships(Page<WorshipEvent> worshipEvents);
}
