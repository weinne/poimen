package br.com.poimen.service;

import br.com.poimen.domain.Transaction;
import br.com.poimen.repository.TransactionRepository;
import br.com.poimen.repository.search.TransactionSearchRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.Transaction}.
 */
@Service
@Transactional
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final TransactionSearchRepository transactionSearchRepository;

    public TransactionService(TransactionRepository transactionRepository, TransactionSearchRepository transactionSearchRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionSearchRepository = transactionSearchRepository;
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    public Transaction save(Transaction transaction) {
        LOG.debug("Request to save Transaction : {}", transaction);
        transaction = transactionRepository.save(transaction);
        transactionSearchRepository.index(transaction);
        return transaction;
    }

    /**
     * Update a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    public Transaction update(Transaction transaction) {
        LOG.debug("Request to update Transaction : {}", transaction);
        transaction = transactionRepository.save(transaction);
        transactionSearchRepository.index(transaction);
        return transaction;
    }

    /**
     * Partially update a transaction.
     *
     * @param transaction the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Transaction> partialUpdate(Transaction transaction) {
        LOG.debug("Request to partially update Transaction : {}", transaction);

        return transactionRepository
            .findById(transaction.getId())
            .map(existingTransaction -> {
                if (transaction.getDescription() != null) {
                    existingTransaction.setDescription(transaction.getDescription());
                }
                if (transaction.getAmount() != null) {
                    existingTransaction.setAmount(transaction.getAmount());
                }
                if (transaction.getDate() != null) {
                    existingTransaction.setDate(transaction.getDate());
                }
                if (transaction.getPaymentMethod() != null) {
                    existingTransaction.setPaymentMethod(transaction.getPaymentMethod());
                }
                if (transaction.getType() != null) {
                    existingTransaction.setType(transaction.getType());
                }
                if (transaction.getSupplierOrClient() != null) {
                    existingTransaction.setSupplierOrClient(transaction.getSupplierOrClient());
                }
                if (transaction.getInvoiceFile() != null) {
                    existingTransaction.setInvoiceFile(transaction.getInvoiceFile());
                }

                return existingTransaction;
            })
            .map(transactionRepository::save)
            .map(savedTransaction -> {
                transactionSearchRepository.index(savedTransaction);
                return savedTransaction;
            });
    }

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        LOG.debug("Request to get all Transactions");
        return transactionRepository.findAll(pageable);
    }

    /**
     * Get all the transactions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Transaction> findAllWithEagerRelationships(Pageable pageable) {
        return transactionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one transaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        LOG.debug("Request to get Transaction : {}", id);
        return transactionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
        transactionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the transaction corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Transaction> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Transactions for query {}", query);
        return transactionSearchRepository.search(query, pageable);
    }
}
