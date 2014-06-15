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

        entity.setUpdateUser(SecurityContext.getInstance().getUsername());
        entity.setUpdateTime(new Date());
        // new entity
        if (null == entity.getId()) {
            entity.setCreateUser(entity.getUpdateUser());
            entity.setCreateTime(entity.getUpdateTime());
        } else {
            E oldOne = repository.findOne(entity.getId());
            entity.setCreateUser(oldOne.getCreateUser());
            entity.setCreateTime(oldOne.getCreateTime());
        }
        return repository.save(entity);
    }

    public Iterable<E> save(Iterable<E> entities) {
        for (E entity : entities) {
            entity.setUpdateUser(SecurityContext.getInstance().getUsername());
            entity.setUpdateTime(new Date());
            // new entity
            if (null == entity.getId()) {
                entity.setCreateUser(entity.getUpdateUser());
                entity.setCreateTime(entity.getUpdateTime());
            } else {
                E oldOne = repository.findOne(entity.getId());
                entity.setCreateUser(oldOne.getCreateUser());
                entity.setCreateTime(oldOne.getCreateTime());
            }
        }
        return repository.save(entities);
    }
}
