package liquid.accounting.service;

import liquid.accounting.persistence.domain.GrossProfitEntity;
import liquid.accounting.persistence.domain.GrossProfitEntity_;
import liquid.accounting.persistence.repository.GrossProfitRepository;
import liquid.order.persistence.domain.OrderEntity_;
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
import java.util.Date;

/**
 * Created by Tao Ma on 2/7/15.
 */
@Service
public class GrossProfitService {

    @Autowired
    private GrossProfitRepository repository;

    public GrossProfitEntity save(GrossProfitEntity grossProfitEntity) {
        return repository.save(grossProfitEntity);
    }

    public Page<GrossProfitEntity> findAll(final Date start, final Date end, final Long orderId, final Long customerId, final Pageable pageable) {
        Specification<GrossProfitEntity> dateRangeSpec = new Specification<GrossProfitEntity>() {
            @Override
            public Predicate toPredicate(Root<GrossProfitEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get(GrossProfitEntity_.createdAt), start, end);
            }
        };
        Specifications<GrossProfitEntity> specifications = Specifications.where(dateRangeSpec);

        if (null != orderId) {
            Specification<GrossProfitEntity> orderIdSpec = new Specification<GrossProfitEntity>() {
                @Override
                public Predicate toPredicate(Root<GrossProfitEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get(GrossProfitEntity_.order).get(OrderEntity_.id), orderId);
                }
            };
            specifications = specifications.and(orderIdSpec);
        }

        if (null != customerId) {
            Specification<GrossProfitEntity> customerIdSpec = new Specification<GrossProfitEntity>() {
                @Override
                public Predicate toPredicate(Root<GrossProfitEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get(GrossProfitEntity_.order).get(OrderEntity_.customerId), customerId);
                }
            };
            specifications = specifications.and(customerIdSpec);
        }
        return repository.findAll(specifications, pageable);
    }
}
