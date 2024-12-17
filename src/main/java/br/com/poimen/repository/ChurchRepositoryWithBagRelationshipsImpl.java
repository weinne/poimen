package br.com.poimen.repository;

import br.com.poimen.domain.Church;
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
public class ChurchRepositoryWithBagRelationshipsImpl implements ChurchRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CHURCHES_PARAMETER = "churches";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Church> fetchBagRelationships(Optional<Church> church) {
        return church.map(this::fetchUsers);
    }

    @Override
    public Page<Church> fetchBagRelationships(Page<Church> churches) {
        return new PageImpl<>(fetchBagRelationships(churches.getContent()), churches.getPageable(), churches.getTotalElements());
    }

    @Override
    public List<Church> fetchBagRelationships(List<Church> churches) {
        return Optional.of(churches).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Church fetchUsers(Church result) {
        return entityManager
            .createQuery("select church from Church church left join fetch church.users where church.id = :id", Church.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Church> fetchUsers(List<Church> churches) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, churches.size()).forEach(index -> order.put(churches.get(index).getId(), index));
        List<Church> result = entityManager
            .createQuery("select church from Church church left join fetch church.users where church in :churches", Church.class)
            .setParameter(CHURCHES_PARAMETER, churches)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
