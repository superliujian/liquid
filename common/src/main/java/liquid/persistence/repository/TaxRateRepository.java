package liquid.persistence.repository;

import liquid.persistence.domain.TaxRateEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by redbrick9 on 8/29/14.
 */
public interface TaxRateRepository extends CrudRepository<TaxRateEntity, Long> {
    public Collection<TaxRateEntity> findAll();
}
