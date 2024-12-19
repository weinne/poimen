package br.com.poimen.service;

import br.com.poimen.domain.ApplicationUser;
import br.com.poimen.domain.User;
import br.com.poimen.repository.ApplicationUserRepository;
import br.com.poimen.repository.UserRepository;
import br.com.poimen.repository.search.ApplicationUserSearchRepository;
import br.com.poimen.security.SecurityUtils;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.ApplicationUser}.
 */
@Service
@Transactional
public class ApplicationUserService {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationUserService.class);

    private final ApplicationUserRepository applicationUserRepository;

    private final ApplicationUserSearchRepository applicationUserSearchRepository;

    private final UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    public ApplicationUserService(
        ApplicationUserRepository applicationUserRepository,
        ApplicationUserSearchRepository applicationUserSearchRepository,
        UserRepository userRepository
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserSearchRepository = applicationUserSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a applicationUser.
     *
     * @param applicationUser the entity to save.
     * @return the persisted entity.
     */
    public ApplicationUser save(ApplicationUser applicationUser) {
        LOG.debug("Request to save ApplicationUser : {}", applicationUser);

        // Get the current logged-in user
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("User not logged in"));
        User currentUser = userRepository.findOneByLogin(currentUserLogin).orElseThrow(() -> new IllegalStateException("User not found"));

        // Check if the user already has an ApplicationUser
        Optional<ApplicationUser> existingApplicationUser = applicationUserRepository.findByInternalUserId(currentUser.getId());
        if (existingApplicationUser.isPresent()) {
            throw new IllegalStateException("User already has an ApplicationUser");
        }

        // Set the internal user to the current user
        applicationUser.setInternalUser(currentUser);

        // Save the ApplicationUser
        applicationUser = applicationUserRepository.save(applicationUser);
        applicationUserSearchRepository.index(applicationUser);
        return applicationUser;
    }

    /**
     * Update a applicationUser.
     *
     * @param applicationUser the entity to save.
     * @return the persisted entity.
     */
    public ApplicationUser update(ApplicationUser applicationUser) {
        LOG.debug("Request to update ApplicationUser : {}", applicationUser);
        applicationUser = applicationUserRepository.save(applicationUser);
        applicationUserSearchRepository.index(applicationUser);
        return applicationUser;
    }

    /**
     * Partially update a applicationUser.
     *
     * @param applicationUser the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicationUser> partialUpdate(ApplicationUser applicationUser) {
        LOG.debug("Request to partially update ApplicationUser : {}", applicationUser);

        return applicationUserRepository
            .findById(applicationUser.getId())
            .map(existingApplicationUser -> {
                if (applicationUser.getName() != null) {
                    existingApplicationUser.setName(applicationUser.getName());
                }
                if (applicationUser.getDescription() != null) {
                    existingApplicationUser.setDescription(applicationUser.getDescription());
                }
                if (applicationUser.getStatus() != null) {
                    existingApplicationUser.setStatus(applicationUser.getStatus());
                }

                return existingApplicationUser;
            })
            .map(applicationUserRepository::save)
            .map(savedApplicationUser -> {
                applicationUserSearchRepository.index(savedApplicationUser);
                return savedApplicationUser;
            });
    }

    /**
     * Get all the applicationUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationUser> findAll() {
        LOG.debug("Request to get all ApplicationUsers");

        // Get the current logged-in user
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("User not logged in"));
        User currentUser = userRepository.findOneByLogin(currentUserLogin).orElseThrow(() -> new IllegalStateException("User not found"));

        // Activate the filter
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("userFilter").setParameter("userId", currentUser.getId());

        return applicationUserRepository.findAll();
    }

    /**
     * Get all the applicationUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable) {
        return applicationUserRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one applicationUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationUser> findOne(Long id) {
        LOG.debug("Request to get ApplicationUser : {}", id);

        // Get the current logged-in user
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("User not logged in"));
        User currentUser = userRepository.findOneByLogin(currentUserLogin).orElseThrow(() -> new IllegalStateException("User not found"));

        // Activate the filter
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("userFilter").setParameter("userId", currentUser.getId());

        return applicationUserRepository.findById(id);
    }

    /**
     * Delete the applicationUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
        applicationUserSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the applicationUser corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationUser> search(String query) {
        LOG.debug("Request to search ApplicationUsers for query {}", query);
        try {
            return StreamSupport.stream(applicationUserSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
