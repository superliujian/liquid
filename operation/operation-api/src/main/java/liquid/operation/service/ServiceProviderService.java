package liquid.operation.service;

import liquid.operation.domain.ServiceProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Tao Ma on 4/2/15.
 */
@Service
public interface ServiceProviderService {
    ServiceProvider find(Long id);

    Iterable<ServiceProvider> findByType(Long typeId);

    List<ServiceProvider> findByServiceSubtypeId(Long serviceSubtypeId);

    Iterable<ServiceProvider> findByQueryNameLike(String name);

    @Deprecated
    Iterable<ServiceProvider> findAll();

    Page<ServiceProvider> findAll(Pageable pageable);

    Map<Long, String> getSpTypes();

    ServiceProvider save(ServiceProvider serviceProvider);
}
