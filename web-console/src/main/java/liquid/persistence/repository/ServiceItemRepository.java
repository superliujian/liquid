package liquid.persistence.repository;

import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.ServiceItemEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by tao on 12/25/13.
 */
public interface ServiceItemRepository extends CrudRepository<ServiceItemEntity, Long> {
    Iterable<ServiceItemEntity> findByChargeType(ChargeType chargeType);

    Collection<ServiceItemEntity> findByServiceProvider(ServiceProviderEntity serviceProviderEntity);

    Collection<ServiceItemEntity> findByName(String name);
}