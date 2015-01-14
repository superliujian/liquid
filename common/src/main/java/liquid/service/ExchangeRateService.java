package liquid.service;

import liquid.persistence.domain.ExchangeRateEntity;
import liquid.persistence.repository.ExchangeRateRepository;

/**
 * Created by Tao Ma on 1/14/15.
 */
public class ExchangeRateService extends AbstractCachedService<ExchangeRateEntity, ExchangeRateRepository> {
    @Override
    public void doSaveBefore(ExchangeRateEntity entity) {}

    public double getExchangeRate() {
        ExchangeRateEntity exchangeRate = find(1L);
        return null == exchangeRate ? 0.00 : exchangeRate.getValue();
    }
}
