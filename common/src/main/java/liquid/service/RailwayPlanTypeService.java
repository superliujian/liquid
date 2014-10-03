package liquid.service;

import liquid.persistence.domain.RailPlanTypeEntity;
import liquid.persistence.repository.RailPlanTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 8/23/14.
 */
@Service
public class RailwayPlanTypeService extends AbstractService<RailPlanTypeEntity, RailPlanTypeRepository> {
    @Autowired
    private RailPlanTypeRepository railPlanTypeRepository;

    @Override
    public void doSaveBefore(RailPlanTypeEntity entity) {}

    public Iterable<RailPlanTypeEntity> findAll() {
        return railPlanTypeRepository.findAll();
    }

    public RailPlanTypeEntity find(Long id) {
        return railPlanTypeRepository.findOne(id);
    }
}
