package liquid.accounting.facade;

import liquid.accounting.web.domain.ReceivableSummary;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public interface ReceivableFacade {
    ReceivableSummary save(ReceivableSummary receivableSummary);
}
