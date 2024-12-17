package br.com.poimen.repository;

import br.com.poimen.domain.Schedule;
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
public class ScheduleRepositoryWithBagRelationshipsImpl implements ScheduleRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SCHEDULES_PARAMETER = "schedules";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Schedule> fetchBagRelationships(Optional<Schedule> schedule) {
        return schedule.map(this::fetchMembers).map(this::fetchWorshipEvents);
    }

    @Override
    public Page<Schedule> fetchBagRelationships(Page<Schedule> schedules) {
        return new PageImpl<>(fetchBagRelationships(schedules.getContent()), schedules.getPageable(), schedules.getTotalElements());
    }

    @Override
    public List<Schedule> fetchBagRelationships(List<Schedule> schedules) {
        return Optional.of(schedules).map(this::fetchMembers).map(this::fetchWorshipEvents).orElse(Collections.emptyList());
    }

    Schedule fetchMembers(Schedule result) {
        return entityManager
            .createQuery("select schedule from Schedule schedule left join fetch schedule.members where schedule.id = :id", Schedule.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Schedule> fetchMembers(List<Schedule> schedules) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, schedules.size()).forEach(index -> order.put(schedules.get(index).getId(), index));
        List<Schedule> result = entityManager
            .createQuery(
                "select schedule from Schedule schedule left join fetch schedule.members where schedule in :schedules",
                Schedule.class
            )
            .setParameter(SCHEDULES_PARAMETER, schedules)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Schedule fetchWorshipEvents(Schedule result) {
        return entityManager
            .createQuery(
                "select schedule from Schedule schedule left join fetch schedule.worshipEvents where schedule.id = :id",
                Schedule.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Schedule> fetchWorshipEvents(List<Schedule> schedules) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, schedules.size()).forEach(index -> order.put(schedules.get(index).getId(), index));
        List<Schedule> result = entityManager
            .createQuery(
                "select schedule from Schedule schedule left join fetch schedule.worshipEvents where schedule in :schedules",
                Schedule.class
            )
            .setParameter(SCHEDULES_PARAMETER, schedules)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
