package liquid.accounting.web.domain;

/**
 * Created by mat on 11/14/14.
 */
public class PayableSettlement {
    private String appliedAt;
    private String invoiceNo;
    private Long cnyOfInvoice;
    private Long usdOfInvoice;
    private Long payeeId;
    private String payeeName;
    private Long cny;
    private Long usd;
    private String paidAt;

    public String getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(String appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Long getCnyOfInvoice() {
        return cnyOfInvoice;
    }

    public void setCnyOfInvoice(Long cnyOfInvoice) {
        this.cnyOfInvoice = cnyOfInvoice;
    }

    public Long getUsdOfInvoice() {
        return usdOfInvoice;
    }

    public void setUsdOfInvoice(Long usdOfInvoice) {
        this.usdOfInvoice = usdOfInvoice;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public Long getCny() {
        return cny;
    }

    public void setCny(Long cny) {
        this.cny = cny;
    }

    public Long getUsd() {
        return usd;
    }

    public void setUsd(Long usd) {
        this.usd = usd;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=PayableSettlement");
        sb.append(", appliedAt='").append(appliedAt).append('\'');
        sb.append(", invoiceNo='").append(invoiceNo).append('\'');
        sb.append(", cnyOfInvoice=").append(cnyOfInvoice);
        sb.append(", usdOfInvoice=").append(usdOfInvoice);
        sb.append(", payeeId=").append(payeeId);
        sb.append(", payeeName='").append(payeeName).append('\'');
        sb.append(", cny=").append(cny);
        sb.append(", usd=").append(usd);
        sb.append(", paidAt='").append(paidAt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
