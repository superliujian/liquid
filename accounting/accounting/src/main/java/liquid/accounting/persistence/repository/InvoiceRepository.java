package liquid.accounting.persistence.repository;

import liquid.accounting.persistence.domain.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 1/8/15.
 */
public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Long> {
    Iterable<InvoiceEntity> findByOrderId(Long orderId);

    Page<InvoiceEntity> findAll(Pageable pageable);

    Iterable<InvoiceEntity> findByBuyerId(Long buyerId);

    Page<InvoiceEntity> findAll(Specification<InvoiceEntity> specification, Pageable pageable);
}
