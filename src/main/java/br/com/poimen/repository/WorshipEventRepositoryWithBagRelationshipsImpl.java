package br.com.poimen.repository;

import br.com.poimen.domain.WorshipEvent;
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
public class WorshipEventRepositoryWithBagRelationshipsImpl implements WorshipEventRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String WORSHIPEVENTS_PARAMETER = "worshipEvents";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<WorshipEvent> fetchBagRelationships(Optional<WorshipEvent> worshipEvent) {
        return worshipEvent.map(this::fetchHymns).map(this::fetchMusicians).map(this::fetchParticipants);
    }

    @Override
    public Page<WorshipEvent> fetchBagRelationships(Page<WorshipEvent> worshipEvents) {
        return new PageImpl<>(
            fetchBagRelationships(worshipEvents.getContent()),
            worshipEvents.getPageable(),
            worshipEvents.getTotalElements()
        );
    }

    @Override
    public List<WorshipEvent> fetchBagRelationships(List<WorshipEvent> worshipEvents) {
        return Optional.of(worshipEvents)
            .map(this::fetchHymns)
            .map(this::fetchMusicians)
            .map(this::fetchParticipants)
            .orElse(Collections.emptyList());
    }

    WorshipEvent fetchHymns(WorshipEvent result) {
        return entityManager
            .createQuery(
                "select worshipEvent from WorshipEvent worshipEvent left join fetch worshipEvent.hymns where worshipEvent.id = :id",
                WorshipEvent.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<WorshipEvent> fetchHymns(List<WorshipEvent> worshipEvents) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, worshipEvents.size()).forEach(index -> order.put(worshipEvents.get(index).getId(), index));
        List<WorshipEvent> result = entityManager
            .createQuery(
                "select worshipEvent from WorshipEvent worshipEvent left join fetch worshipEvent.hymns where worshipEvent in :worshipEvents",
                WorshipEvent.class
            )
            .setParameter(WORSHIPEVENTS_PARAMETER, worshipEvents)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    WorshipEvent fetchMusicians(WorshipEvent result) {
        return entityManager
            .createQuery(
                "select worshipEvent from WorshipEvent worshipEvent left join fetch worshipEvent.musicians where worshipEvent.id = :id",
                WorshipEvent.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<WorshipEvent> fetchMusicians(List<WorshipEvent> worshipEvents) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, worshipEvents.size()).forEach(index -> order.put(worshipEvents.get(index).getId(), index));
        List<WorshipEvent> result = entityManager
            .createQuery(
                "select worshipEvent from WorshipEvent worshipEvent left join fetch worshipEvent.musicians where worshipEvent in :worshipEvents",
                WorshipEvent.class
            )
            .setParameter(WORSHIPEVENTS_PARAMETER, worshipEvents)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    WorshipEvent fetchParticipants(WorshipEvent result) {
        return entityManager
            .createQuery(
                "select worshipEvent from WorshipEvent worshipEvent left join fetch worshipEvent.participants where worshipEvent.id = :id",
                WorshipEvent.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<WorshipEvent> fetchParticipants(List<WorshipEvent> worshipEvents) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, worshipEvents.size()).forEach(index -> order.put(worshipEvents.get(index).getId(), index));
        List<WorshipEvent> result = entityManager
            .createQuery(
                "select worshipEvent from WorshipEvent worshipEvent left join fetch worshipEvent.participants where worshipEvent in :worshipEvents",
                WorshipEvent.class
            )
            .setParameter(WORSHIPEVENTS_PARAMETER, worshipEvents)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
