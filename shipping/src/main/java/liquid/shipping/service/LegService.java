package liquid.shipping.service;

import liquid.service.AbstractService;
import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.LegRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by redbrick9 on 9/4/14.
 */
@Service
public class LegService extends AbstractService<LegEntity, LegRepository> {
    @Override
    public void doSaveBefore(LegEntity legEntity) {}

    public List<LegEntity> findByRouteAndTransMode(RouteEntity route, int transMode) {
        return repository.findByRouteAndTransMode(route, transMode);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
