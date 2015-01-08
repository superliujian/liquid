package liquid.accounting.service;

import liquid.accounting.persistence.domain.InvoiceEntity;
import liquid.accounting.persistence.domain.InvoiceEntity_;
import liquid.accounting.persistence.repository.InvoiceRepository;
import liquid.service.AbstractService;
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

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Service
public class InvoiceService extends AbstractService<InvoiceEntity, InvoiceRepository> {
    @Override
    public void doSaveBefore(InvoiceEntity entity) {}

    public Page<InvoiceEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<InvoiceEntity> findAll(Date start, Date end, Long buyerId, Pageable pageable) {
        Specification<InvoiceEntity> dateSpec = new Specification<InvoiceEntity>() {
            @Override
            public Predicate toPredicate(Root<InvoiceEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get(InvoiceEntity_.issuedAt), start, end);
            }
        };
        Specifications<InvoiceEntity> specifications = where(dateSpec);

        if (null != buyerId) {
            Specification<InvoiceEntity> buyerSpec = new Specification<InvoiceEntity>() {
                @Override
                public Predicate toPredicate(Root<InvoiceEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get(InvoiceEntity_.buyerId), buyerId);
                }
            };
            specifications.and(buyerSpec);
        }

        return repository.findAll(specifications, pageable);
    }

    public Iterable<InvoiceEntity> findByBuyerId(Long buyerId) {
        return repository.findByBuyerId(buyerId);
    }
}
