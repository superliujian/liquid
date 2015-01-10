package liquid.accounting.facade;

import liquid.accounting.web.domain.ReceivableSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public interface ReceivableFacade {
    Page<ReceivableSummary> findAll(Pageable pageable);

    ReceivableSummary save(ReceivableSummary receivableSummary);
}
