package liquid.persistence.repository;

import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.ServiceItem;
import liquid.persistence.domain.ServiceProviderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by tao on 12/25/13.
 */
public interface ServiceItemRepository extends CrudRepository<ServiceItem, Long> {
    Iterable<ServiceItem> findByChargeType(ChargeType chargeType);

    Collection<ServiceItem> findByServiceProvider(ServiceProviderEntity serviceProviderEntity);

    Collection<ServiceItem> findByName(String name);
}