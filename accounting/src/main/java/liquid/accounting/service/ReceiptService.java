package liquid.accounting.service;

import liquid.accounting.persistence.domain.ReceiptEntity;
import liquid.accounting.persistence.repository.ReceiptRepository;
import liquid.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Service
public class ReceiptService extends AbstractService<ReceiptEntity, ReceiptRepository> {

    @Override
    public void doSaveBefore(ReceiptEntity entity) {}

    public Iterable<ReceiptEntity> findByPayerId(Long payerId) {
        return repository.findByPayerId(payerId);
    }
}
