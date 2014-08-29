package liquid.purchasing.persistence.repository;

import liquid.purchasing.persistence.domain.PayableSummaryEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 8/28/14.
 */
public interface PayableSummaryRepository extends CrudRepository<PayableSummaryEntity, Long> {}
