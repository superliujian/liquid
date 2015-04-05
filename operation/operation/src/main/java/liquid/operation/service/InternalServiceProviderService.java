package liquid.operation.service;

import liquid.operation.domain.ServiceProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface InternalServiceProviderService extends ServiceProviderService {

    Page<ServiceProvider> findAll(Pageable pageable);

    Map<Long, String> getSpTypes();

    ServiceProvider save(ServiceProvider serviceProvider);
}
