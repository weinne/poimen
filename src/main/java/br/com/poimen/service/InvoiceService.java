package br.com.poimen.service;

import br.com.poimen.domain.Invoice;
import br.com.poimen.repository.InvoiceRepository;
import br.com.poimen.repository.search.InvoiceSearchRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.Invoice}.
 */
@Service
@Transactional
public class InvoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceSearchRepository invoiceSearchRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceSearchRepository invoiceSearchRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceSearchRepository = invoiceSearchRepository;
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice save(Invoice invoice) {
        LOG.debug("Request to save Invoice : {}", invoice);
        invoice = invoiceRepository.save(invoice);
        invoiceSearchRepository.index(invoice);
        return invoice;
    }

    /**
     * Update a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice update(Invoice invoice) {
        LOG.debug("Request to update Invoice : {}", invoice);
        invoice = invoiceRepository.save(invoice);
        invoiceSearchRepository.index(invoice);
        return invoice;
    }

    /**
     * Partially update a invoice.
     *
     * @param invoice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Invoice> partialUpdate(Invoice invoice) {
        LOG.debug("Request to partially update Invoice : {}", invoice);

        return invoiceRepository
            .findById(invoice.getId())
            .map(existingInvoice -> {
                if (invoice.getNumber() != null) {
                    existingInvoice.setNumber(invoice.getNumber());
                }
                if (invoice.getIssueDate() != null) {
                    existingInvoice.setIssueDate(invoice.getIssueDate());
                }
                if (invoice.getTotalAmount() != null) {
                    existingInvoice.setTotalAmount(invoice.getTotalAmount());
                }
                if (invoice.getType() != null) {
                    existingInvoice.setType(invoice.getType());
                }
                if (invoice.getSupplier() != null) {
                    existingInvoice.setSupplier(invoice.getSupplier());
                }
                if (invoice.getInvoiceFile() != null) {
                    existingInvoice.setInvoiceFile(invoice.getInvoiceFile());
                }
                if (invoice.getInvoiceFileContentType() != null) {
                    existingInvoice.setInvoiceFileContentType(invoice.getInvoiceFileContentType());
                }

                return existingInvoice;
            })
            .map(invoiceRepository::save)
            .map(savedInvoice -> {
                invoiceSearchRepository.index(savedInvoice);
                return savedInvoice;
            });
    }

    /**
     * Get all the invoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Invoice> findAll(Pageable pageable) {
        LOG.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable);
    }

    /**
     * Get all the invoices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Invoice> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        LOG.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
        invoiceSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the invoice corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Invoice> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Invoices for query {}", query);
        return invoiceSearchRepository.search(query, pageable);
    }
}
