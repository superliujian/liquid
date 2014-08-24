package liquid.order.service;

import liquid.order.persistence.domain.RailwayEntity;
import liquid.order.persistence.repository.RailwayRepository;
import liquid.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 8/22/14.
 */
@Service
public class RailwayService extends AbstractService<RailwayEntity, RailwayRepository> {

    @Autowired
    private RailwayRepository railwayRepository;

    @Override
    public void doSaveBefore(RailwayEntity railwayEntity) { }

    public RailwayEntity findByOrderId(Long orderId) {
        return railwayRepository.findByOrderId(orderId);
    }
}
