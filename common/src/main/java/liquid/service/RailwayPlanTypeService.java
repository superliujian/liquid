package liquid.service;

import liquid.persistence.domain.RailwayPlanTypeEntity;
import liquid.persistence.repository.RailwayPlanTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 8/23/14.
 */
@Service
public class RailwayPlanTypeService extends AbstractService<RailwayPlanTypeEntity, RailwayPlanTypeRepository> {
    @Autowired
    private RailwayPlanTypeRepository railwayPlanTypeRepository;

    @Override
    public void doSaveBefore(RailwayPlanTypeEntity entity) {}

    public Iterable<RailwayPlanTypeEntity> findAll() {
        return railwayPlanTypeRepository.findAll();
    }

    public RailwayPlanTypeEntity find(Long id) {
        return railwayPlanTypeRepository.findOne(id);
    }
}
