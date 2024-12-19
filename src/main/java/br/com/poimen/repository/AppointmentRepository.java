package br.com.poimen.repository;

import br.com.poimen.domain.Appointment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Appointment entity.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    default Optional<Appointment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Appointment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Appointment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select appointment from Appointment appointment left join fetch appointment.church left join fetch appointment.member left join fetch appointment.service left join fetch appointment.group left join fetch appointment.counselingSession left join fetch appointment.user",
        countQuery = "select count(appointment) from Appointment appointment"
    )
    Page<Appointment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select appointment from Appointment appointment left join fetch appointment.church left join fetch appointment.member left join fetch appointment.service left join fetch appointment.group left join fetch appointment.counselingSession left join fetch appointment.user"
    )
    List<Appointment> findAllWithToOneRelationships();

    @Query(
        "select appointment from Appointment appointment left join fetch appointment.church left join fetch appointment.member left join fetch appointment.service left join fetch appointment.group left join fetch appointment.counselingSession left join fetch appointment.user where appointment.id =:id"
    )
    Optional<Appointment> findOneWithToOneRelationships(@Param("id") Long id);
}
