package liquid.service;

import liquid.metadata.ContainerStatus;
import liquid.persistence.domain.*;
import liquid.persistence.repository.ContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;


/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 1:11 AM
 */
@Service
public class ContainerService {

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private LocationService locationService;

    public Iterable<ContainerEntity> findAll() {
        return containerRepository.findAll();
    }

    public Iterable<ContainerEntity> findAllInStock(int type) {
        return containerRepository.findByStatusAndSubtype(ContainerStatus.IN_STOCK.getValue(), type);
    }

    public Page<ContainerEntity> findAll(Pageable pageable) {
        return containerRepository.findAll(pageable);
    }

    public Page<ContainerEntity> findAll(final long containerSubtypeId, final long ownerId, final long yardId, final Pageable pageable) {
        Specifications<ContainerEntity> specifications = where(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.subtype), ContainerSubtypeEntity.newInstance(ContainerSubtypeEntity.class, containerSubtypeId));
            }
        });

        // status
        specifications = specifications.and(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.status), ContainerStatus.IN_STOCK.getValue());
            }
        });

        // owner
        if (ownerId > 0L) specifications = specifications.and(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.owner), ServiceProviderEntity.newInstance(ServiceProviderEntity.class, ownerId));
            }
        });

        // yard
        if (yardId > 0L) specifications = specifications.and(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.yard), LocationEntity.newInstance(LocationEntity.class, yardId));
            }
        });

        return containerRepository.findAll(specifications, pageable);
    }

    public ContainerEntity find(long id) {
        ContainerEntity containerEntity = containerRepository.findOne(id);
        return containerEntity;
    }

    public void save(ContainerEntity containerEntity) {
        containerRepository.save(containerEntity);
    }

    public void save(List<ContainerEntity> containers) {
        containerRepository.save(containers);
    }
}
