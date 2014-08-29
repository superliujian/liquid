package liquid.service;

import liquid.persistence.domain.TaxRateEntity;
import liquid.persistence.repository.TaxRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Service
public class TaxRateService extends AbstractService<TaxRateEntity, TaxRateRepository> {

    @Autowired
    private TaxRateRepository taxRateRepository;

    @Override
    public void doSaveBefore(TaxRateEntity entity) {}

    public Collection<TaxRateEntity> findAll() {
        return taxRateRepository.findAll();
    }
}
