package br.com.poimen.repository;

import br.com.poimen.domain.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    default Optional<Invoice> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Invoice> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Invoice> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select invoice from Invoice invoice left join fetch invoice.church left join fetch invoice.transaction",
        countQuery = "select count(invoice) from Invoice invoice"
    )
    Page<Invoice> findAllWithToOneRelationships(Pageable pageable);

    @Query("select invoice from Invoice invoice left join fetch invoice.church left join fetch invoice.transaction")
    List<Invoice> findAllWithToOneRelationships();

    @Query("select invoice from Invoice invoice left join fetch invoice.church left join fetch invoice.transaction where invoice.id =:id")
    Optional<Invoice> findOneWithToOneRelationships(@Param("id") Long id);
}
