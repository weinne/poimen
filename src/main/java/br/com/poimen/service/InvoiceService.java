package br.com.poimen.service;

import br.com.poimen.domain.Invoice;
import br.com.poimen.repository.InvoiceRepository;
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

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice save(Invoice invoice) {
        LOG.debug("Request to save Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Update a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice update(Invoice invoice) {
        LOG.debug("Request to update Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
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

                return existingInvoice;
            })
            .map(invoiceRepository::save);
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
     * Get one invoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        LOG.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }
}
