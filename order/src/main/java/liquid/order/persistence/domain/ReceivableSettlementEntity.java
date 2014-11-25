package liquid.order.persistence.domain;

import liquid.persistence.domain.BaseUpdateEntity;
import liquid.persistence.domain.CustomerEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Entity(name = "ACT_RECEIVABLE_SETTLE")
public class ReceivableSettlementEntity extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "CNY_OF_INVOICE")
    private Long cnyOfInvoice;

    @Column(name = "USD_OF_INVOICE")
    private Long usdOfInvoice;

    @Column(name = "DATE_OF_INVOICE")
    private Date dateOfInvoice;

    @ManyToOne
    @JoinColumn(name = "PAYER_ID")
    private CustomerEntity payer;

    @Column(name = "EX_DATE_OF_RECEIVABLE")
    private Date expectedDateOfReceivable;

    @Column(name = "CNY")
    private Long cny;

    @Column(name = "USD")
    private Long usd;

    @Column(name = "DATE_OF_RECEIVABLE")
    private Date dateOfReceivable;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
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

    public Date getDateOfInvoice() {
        return dateOfInvoice;
    }

    public void setDateOfInvoice(Date dateOfInvoice) {
        this.dateOfInvoice = dateOfInvoice;
    }

    public CustomerEntity getPayer() {
        return payer;
    }

    public void setPayer(CustomerEntity payer) {
        this.payer = payer;
    }

    public Date getExpectedDateOfReceivable() {
        return expectedDateOfReceivable;
    }

    public void setExpectedDateOfReceivable(Date expectedDateOfReceivable) {
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

    public Date getDateOfReceivable() {
        return dateOfReceivable;
    }

    public void setDateOfReceivable(Date dateOfReceivable) {
        this.dateOfReceivable = dateOfReceivable;
    }
}
