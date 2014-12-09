package liquid.service;

import liquid.persistence.domain.BaseUpdateEntity;
import liquid.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by redbrick9 on 6/3/14.
 */
public abstract class AbstractService<E extends BaseUpdateEntity, R extends CrudRepository<E, Long>> {
    @Autowired
    protected R repository;

    public abstract void doSaveBefore(E entity);

    @Transactional("transactionManager")
    public E save(E entity) {
        doSaveBefore(entity);

        entity.setUpdatedBy(SecurityContext.getInstance().getUsername());
        entity.setUpdatedAt(new Date());
        // new entity
        if (null == entity.getId()) {
            entity.setCreatedBy(entity.getUpdatedBy());
            entity.setCreatedAt(entity.getUpdatedAt());
        } else {
            E oldOne = repository.findOne(entity.getId());
            entity.setCreatedBy(oldOne.getCreatedBy());
            entity.setCreatedAt(oldOne.getCreatedAt());
        }
        return repository.save(entity);
    }

    public Iterable<E> save(Iterable<E> entities) {
        for (E entity : entities) {
            entity.setUpdatedBy(SecurityContext.getInstance().getUsername());
            entity.setUpdatedAt(new Date());
            // new entity
            if (null == entity.getId()) {
                entity.setCreatedBy(entity.getUpdatedBy());
                entity.setCreatedAt(entity.getUpdatedAt());
            } else {
                E oldOne = repository.findOne(entity.getId());
                entity.setCreatedBy(oldOne.getCreatedBy());
                entity.setCreatedAt(oldOne.getCreatedAt());
            }
        }
        return repository.save(entities);
    }

    public Iterable<E> findAll() {
        return repository.findAll();
    }

    public E find(Long id) {
        return repository.findOne(id);
    }
}
