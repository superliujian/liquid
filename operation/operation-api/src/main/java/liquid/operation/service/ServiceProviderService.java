package liquid.operation.service;

import liquid.operation.domain.ServiceProvider;

import java.util.List;

/**
 * Created by Tao Ma on 4/2/15.
 */
public interface ServiceProviderService {
    ServiceProvider find(Long id);

    Iterable<ServiceProvider> findByType(Long typeId);

    List<ServiceProvider> findByServiceSubtypeId(Long serviceSubtypeId);

    Iterable<ServiceProvider> findByQueryNameLike(String name);

    @Deprecated
    Iterable<ServiceProvider> findAll();
}
