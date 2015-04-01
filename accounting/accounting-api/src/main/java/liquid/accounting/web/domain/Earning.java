package liquid.accounting.web.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/25/13
 * Time: 11:39 PM
 */
public class Earning implements Serializable {
    private BigDecimal salesPriceCny;

    private BigDecimal salesPriceUsd;

    private BigDecimal distyPrice;

    private BigDecimal grandTotal;

    private BigDecimal grossMargin;

    private BigDecimal salesProfit;

    private BigDecimal distyProfit;

    public Earning() { }

    public BigDecimal getSalesPriceCny() {
        return salesPriceCny;
    }

    public void setSalesPriceCny(BigDecimal salesPriceCny) {
        this.salesPriceCny = salesPriceCny;
    }

    public BigDecimal getSalesPriceUsd() {
        return salesPriceUsd;
    }

    public void setSalesPriceUsd(BigDecimal salesPriceUsd) {
        this.salesPriceUsd = salesPriceUsd;
    }

    public BigDecimal getDistyPrice() {
        return distyPrice;
    }

    public void setDistyPrice(BigDecimal distyPrice) {
        this.distyPrice = distyPrice;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public BigDecimal getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(BigDecimal grossMargin) {
        this.grossMargin = grossMargin;
    }

    public BigDecimal getSalesProfit() {
        return salesProfit;
    }

    public void setSalesProfit(BigDecimal salesProfit) {
        this.salesProfit = salesProfit;
    }

    public BigDecimal getDistyProfit() {
        return distyProfit;
    }

    public void setDistyProfit(BigDecimal distyProfit) {
        this.distyProfit = distyProfit;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("EarningDto{");
        sb.append("salesPriceCny=").append(salesPriceCny);
        sb.append(", salesPriceUsd=").append(salesPriceUsd);
        sb.append(", distyPrice=").append(distyPrice);
        sb.append(", grandTotal=").append(grandTotal);
        sb.append(", grossMargin=").append(grossMargin);
        sb.append(", salesProfit=").append(salesProfit);
        sb.append(", distyProfit=").append(distyProfit);
        sb.append('}');
        return sb.toString();
    }
}
