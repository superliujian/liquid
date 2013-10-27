package liquid.dto;

import java.io.Serializable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/25/13
 * Time: 11:39 PM
 */
public class EarningDto implements Serializable {
    private long cost;

    private long salesPrice;

    private long grossMargin;

    private long netProfit;

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public long getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(long salesPrice) {
        this.salesPrice = salesPrice;
    }

    public long getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(long grossMargin) {
        this.grossMargin = grossMargin;
    }

    public long getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(long netProfit) {
        this.netProfit = netProfit;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("EarningDto{");
        sb.append("cost=").append(cost);
        sb.append(", salesPrice=").append(salesPrice);
        sb.append(", grossMargin=").append(grossMargin);
        sb.append(", netProfit=").append(netProfit);
        sb.append('}');
        return sb.toString();
    }
}
