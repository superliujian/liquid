package liquid.service;

import liquid.persistence.domain.GoodsEntity;
import liquid.persistence.repository.GoodsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 7/1/14.
 */
@Service
public class GoodsService extends AbstractCachedService<GoodsEntity, GoodsRepository> {
    @Override
    public void doSaveBefore(GoodsEntity entity) { }

    public Page<GoodsEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
