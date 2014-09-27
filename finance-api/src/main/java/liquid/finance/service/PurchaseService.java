package liquid.finance.service;

import org.springframework.stereotype.Service;

/**
 * Created by mat on 9/27/14.
 */
@Service
public interface PurchaseService {

    void deleteByLegId(Long legId);
}
