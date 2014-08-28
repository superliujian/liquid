package liquid.charge.persistence.domain;

import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.BaseIdEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;

import javax.persistence.*;

/**
 * Created by redbrick9 on 8/28/14.
 */
@Entity(name = "PAY_SUM")
public class PayableSummaryEntity extends BaseIdEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "SRV_SUBTYPE_ID")
    private ServiceSubtypeEntity serviceSubtype;

    @OneToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProviderEntity serviceProvider;

    @Column(name = "WAY")
    private int way = 1;

    @Column(name = "UNIT_PRICE_CNY")
    private Long unitPriceCny;

    @Column(name = "UNIT_PRICE_USD")
    private Long unitPriceUsd;

    @Column(name = "REM_BAL_SUM_CNY")
    private Long remainingBalanceSumCny;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public ServiceSubtypeEntity getServiceSubtype() {
        return serviceSubtype;
    }

    public void setServiceSubtype(ServiceSubtypeEntity serviceSubtype) {
        this.serviceSubtype = serviceSubtype;
    }

    public ServiceProviderEntity getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProviderEntity serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public Long getUnitPriceCny() {
        return unitPriceCny;
    }

    public void setUnitPriceCny(Long unitPriceCny) {
        this.unitPriceCny = unitPriceCny;
    }

    public Long getUnitPriceUsd() {
        return unitPriceUsd;
    }

    public void setUnitPriceUsd(Long unitPriceUsd) {
        this.unitPriceUsd = unitPriceUsd;
    }

    public Long getRemainingBalanceSumCny() {
        return remainingBalanceSumCny;
    }

    public void setRemainingBalanceSumCny(Long remainingBalanceSumCny) {
        this.remainingBalanceSumCny = remainingBalanceSumCny;
    }
}
