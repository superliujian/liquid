package liquid.accounting.persistence.repository;

import liquid.accounting.persistence.domain.ReceivableSummaryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 8/28/14.
 */
public interface ReceivableSummaryRepository extends CrudRepository<ReceivableSummaryEntity, Long> {
    ReceivableSummaryEntity findByOrderId(Long orderId);

    Page<ReceivableSummaryEntity> findAll(Pageable pageable);
}
