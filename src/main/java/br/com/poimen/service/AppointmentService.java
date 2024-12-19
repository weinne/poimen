package br.com.poimen.service;

import br.com.poimen.domain.Appointment;
import br.com.poimen.repository.AppointmentRepository;
import br.com.poimen.repository.search.AppointmentSearchRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.Appointment}.
 */
@Service
@Transactional
public class AppointmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;

    private final AppointmentSearchRepository appointmentSearchRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentSearchRepository appointmentSearchRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentSearchRepository = appointmentSearchRepository;
    }

    /**
     * Save a appointment.
     *
     * @param appointment the entity to save.
     * @return the persisted entity.
     */
    public Appointment save(Appointment appointment) {
        LOG.debug("Request to save Appointment : {}", appointment);
        appointment = appointmentRepository.save(appointment);
        appointmentSearchRepository.index(appointment);
        return appointment;
    }

    /**
     * Update a appointment.
     *
     * @param appointment the entity to save.
     * @return the persisted entity.
     */
    public Appointment update(Appointment appointment) {
        LOG.debug("Request to update Appointment : {}", appointment);
        appointment = appointmentRepository.save(appointment);
        appointmentSearchRepository.index(appointment);
        return appointment;
    }

    /**
     * Partially update a appointment.
     *
     * @param appointment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Appointment> partialUpdate(Appointment appointment) {
        LOG.debug("Request to partially update Appointment : {}", appointment);

        return appointmentRepository
            .findById(appointment.getId())
            .map(existingAppointment -> {
                if (appointment.getSubject() != null) {
                    existingAppointment.setSubject(appointment.getSubject());
                }
                if (appointment.getStartTime() != null) {
                    existingAppointment.setStartTime(appointment.getStartTime());
                }
                if (appointment.getEndTime() != null) {
                    existingAppointment.setEndTime(appointment.getEndTime());
                }
                if (appointment.getNotes() != null) {
                    existingAppointment.setNotes(appointment.getNotes());
                }
                if (appointment.getLocal() != null) {
                    existingAppointment.setLocal(appointment.getLocal());
                }
                if (appointment.getAppointmentType() != null) {
                    existingAppointment.setAppointmentType(appointment.getAppointmentType());
                }

                return existingAppointment;
            })
            .map(appointmentRepository::save)
            .map(savedAppointment -> {
                appointmentSearchRepository.index(savedAppointment);
                return savedAppointment;
            });
    }

    /**
     * Get all the appointments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Appointment> findAll(Pageable pageable) {
        LOG.debug("Request to get all Appointments");
        return appointmentRepository.findAll(pageable);
    }

    /**
     * Get all the appointments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Appointment> findAllWithEagerRelationships(Pageable pageable) {
        return appointmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one appointment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Appointment> findOne(Long id) {
        LOG.debug("Request to get Appointment : {}", id);
        return appointmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the appointment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Appointment : {}", id);
        appointmentRepository.deleteById(id);
        appointmentSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the appointment corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Appointment> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Appointments for query {}", query);
        return appointmentSearchRepository.search(query, pageable);
    }
}
