package br.com.poimen.repository;

import br.com.poimen.domain.MinistryGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MinistryGroupRepositoryWithBagRelationships {
    Optional<MinistryGroup> fetchBagRelationships(Optional<MinistryGroup> ministryGroup);

    List<MinistryGroup> fetchBagRelationships(List<MinistryGroup> ministryGroups);

    Page<MinistryGroup> fetchBagRelationships(Page<MinistryGroup> ministryGroups);
}
