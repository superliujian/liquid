package liquid.order.domain;

/**
 * Created by redbrick9 on 8/29/14.
 */
public class ReceivableSettlement {
    private String invoiceNo;
    private Long cnyOfInvoice;
    private Long usdOfInvoice;
    private String dateOfInvoice;
    private Long payerId;
    private String payerName;
    private String expectedDateOfReceivable;
    private Long cny;
    private Long usd;
    private String dateOfReceivable;

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

    public String getDateOfInvoice() {
        return dateOfInvoice;
    }

    public void setDateOfInvoice(String dateOfInvoice) {
        this.dateOfInvoice = dateOfInvoice;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getExpectedDateOfReceivable() {
        return expectedDateOfReceivable;
    }

    public void setExpectedDateOfReceivable(String expectedDateOfReceivable) {
        this.expectedDateOfReceivable = expectedDateOfReceivable;
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

    public String getDateOfReceivable() {
        return dateOfReceivable;
    }

    public void setDateOfReceivable(String dateOfReceivable) {
        this.dateOfReceivable = dateOfReceivable;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ReceivableSettlement{");
        sb.append("invoiceNo='").append(invoiceNo).append('\'');
        sb.append(", cnyOfInvoice=").append(cnyOfInvoice);
        sb.append(", usdOfInvoice=").append(usdOfInvoice);
        sb.append(", dateOfInvoice='").append(dateOfInvoice).append('\'');
        sb.append(", payerId=").append(payerId);
        sb.append(", payerName='").append(payerName).append('\'');
        sb.append(", expectedDateOfReceivable='").append(expectedDateOfReceivable).append('\'');
        sb.append(", cny=").append(cny);
        sb.append(", usd=").append(usd);
        sb.append(", dateOfReceivable='").append(dateOfReceivable).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
