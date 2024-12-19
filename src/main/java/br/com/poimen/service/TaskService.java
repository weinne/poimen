package br.com.poimen.service;

import br.com.poimen.domain.Task;
import br.com.poimen.repository.TaskRepository;
import br.com.poimen.repository.search.TaskSearchRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.Task}.
 */
@Service
@Transactional
public class TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    private final TaskSearchRepository taskSearchRepository;

    public TaskService(TaskRepository taskRepository, TaskSearchRepository taskSearchRepository) {
        this.taskRepository = taskRepository;
        this.taskSearchRepository = taskSearchRepository;
    }

    /**
     * Save a task.
     *
     * @param task the entity to save.
     * @return the persisted entity.
     */
    public Task save(Task task) {
        LOG.debug("Request to save Task : {}", task);
        task = taskRepository.save(task);
        taskSearchRepository.index(task);
        return task;
    }

    /**
     * Update a task.
     *
     * @param task the entity to save.
     * @return the persisted entity.
     */
    public Task update(Task task) {
        LOG.debug("Request to update Task : {}", task);
        task = taskRepository.save(task);
        taskSearchRepository.index(task);
        return task;
    }

    /**
     * Partially update a task.
     *
     * @param task the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Task> partialUpdate(Task task) {
        LOG.debug("Request to partially update Task : {}", task);

        return taskRepository
            .findById(task.getId())
            .map(existingTask -> {
                if (task.getTitle() != null) {
                    existingTask.setTitle(task.getTitle());
                }
                if (task.getDescription() != null) {
                    existingTask.setDescription(task.getDescription());
                }
                if (task.getDueDate() != null) {
                    existingTask.setDueDate(task.getDueDate());
                }
                if (task.getStatus() != null) {
                    existingTask.setStatus(task.getStatus());
                }
                if (task.getPriority() != null) {
                    existingTask.setPriority(task.getPriority());
                }
                if (task.getNotes() != null) {
                    existingTask.setNotes(task.getNotes());
                }

                return existingTask;
            })
            .map(taskRepository::save)
            .map(savedTask -> {
                taskSearchRepository.index(savedTask);
                return savedTask;
            });
    }

    /**
     * Get all the tasks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        LOG.debug("Request to get all Tasks");
        return taskRepository.findAll();
    }

    /**
     * Get all the tasks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Task> findAllWithEagerRelationships(Pageable pageable) {
        return taskRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one task by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Task> findOne(Long id) {
        LOG.debug("Request to get Task : {}", id);
        return taskRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the task by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
        taskSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the task corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Task> search(String query) {
        LOG.debug("Request to search Tasks for query {}", query);
        try {
            return StreamSupport.stream(taskSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
