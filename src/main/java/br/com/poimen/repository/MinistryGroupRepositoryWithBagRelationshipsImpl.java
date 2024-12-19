package br.com.poimen.repository;

import br.com.poimen.domain.MinistryGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MinistryGroupRepositoryWithBagRelationshipsImpl implements MinistryGroupRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String MINISTRYGROUPS_PARAMETER = "ministryGroups";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<MinistryGroup> fetchBagRelationships(Optional<MinistryGroup> ministryGroup) {
        return ministryGroup.map(this::fetchMembers);
    }

    @Override
    public Page<MinistryGroup> fetchBagRelationships(Page<MinistryGroup> ministryGroups) {
        return new PageImpl<>(
            fetchBagRelationships(ministryGroups.getContent()),
            ministryGroups.getPageable(),
            ministryGroups.getTotalElements()
        );
    }

    @Override
    public List<MinistryGroup> fetchBagRelationships(List<MinistryGroup> ministryGroups) {
        return Optional.of(ministryGroups).map(this::fetchMembers).orElse(Collections.emptyList());
    }

    MinistryGroup fetchMembers(MinistryGroup result) {
        return entityManager
            .createQuery(
                "select ministryGroup from MinistryGroup ministryGroup left join fetch ministryGroup.members where ministryGroup.id = :id",
                MinistryGroup.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<MinistryGroup> fetchMembers(List<MinistryGroup> ministryGroups) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ministryGroups.size()).forEach(index -> order.put(ministryGroups.get(index).getId(), index));
        List<MinistryGroup> result = entityManager
            .createQuery(
                "select ministryGroup from MinistryGroup ministryGroup left join fetch ministryGroup.members where ministryGroup in :ministryGroups",
                MinistryGroup.class
            )
            .setParameter(MINISTRYGROUPS_PARAMETER, ministryGroups)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
