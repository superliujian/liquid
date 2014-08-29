package liquid.domain;

import liquid.persistence.domain.TaxRateEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by redbrick9 on 8/29/14.
 */
public class TaxRates {
    private Collection<TaxRateEntity> list = new ArrayList<>();

    public Collection<TaxRateEntity> getList() {
        return list;
    }

    public void setList(Collection<TaxRateEntity> list) {
        this.list = list;
    }
}
