package liquid.facade;

import liquid.persistence.domain.BaseUpdateEntity;

/**
 * Created by redbrick9 on 6/9/14.
 */
public interface Facade<D, E extends BaseUpdateEntity> {
    E convert(D domain);

    D convert(E entity);
}
