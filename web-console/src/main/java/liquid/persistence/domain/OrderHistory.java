package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 11/12/13
 * Time: 10:59 PM
 */
@Entity(name = "ORDER_HISTORY")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "ORDER_ID")
    private long orderId;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "SRC_LOC_ID")
    private Location srcLoc;

    @ManyToOne
    @JoinColumn(name = "DST_LOC_ID")
    private Location dstLoc;

    @NotNull @NotEmpty
    @Column(name = "CONSIGNEE")
    private String consignee;

    @NotNull @NotEmpty
    @Column(name = "CONSIGNEE_PHONE")
    private String consigneePhone;

    @NotNull @NotEmpty
    @Column(name = "CONSIGNEE_ADDR")
    private String consigneeAddress;

    @ManyToOne
    @JoinColumn(name = "GOODS_ID")
    private Goods goods;

    /**
     * unit kilogram
     */
    @Min(1)
    @Column(name = "WEIGHT")
    private int goodsWeight;

    // 0: domestic; 1: foreign
    @Column(name = "TRADE_TYPE")
    private int tradeType;

    @Column(name = "CONTAINER_TYPE")
    private int containerType;

    @Column(name = "CONTAINER_CAP")
    private int containerCap;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    // 0: yard; 1: truck
    @Column(name = "LOADING_TYPE")
    private int loadingType;

    // For loading by truck
    @Column(name = "LOADING_ADDR")
    private String loadingAddress;

    @Column(name = "LOADING_CONTACT")
    private String loadingContact;

    @Column(name = "LOADING_PHONE")
    private String loadingPhone;

    /**
     * Estimated Time of Loading
     */
    @Column(name = "LOADING_ET")
    private Date loadingEt;

    @Column(name = "SALES_PRICE_CNY")
    private long salesPriceCny;

    /**
     * unit $
     */
    @Column(name = "SALES_PRICE_USD")
    private long salesPriceUsd;

    @Column(name = "DISTY_PRICE")
    private long distyPrice;

    @Column(name = "GRAND_TOTAL")
    private long grandTotal;

    @Column(name = "GROSS_MARGIN")
    private long grossMargin;

    @Column(name = "SALES_PROFIT")
    private long salesProfit;

    @Column(name = "DISTY_PROFIT")
    private long distyProfit;

    @Column(name = "CREATE_ROLE")
    private String createRole;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "FINISH_TIME")
    private Date finishTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Location getSrcLoc() {
        return srcLoc;
    }

    public void setSrcLoc(Location srcLoc) {
        this.srcLoc = srcLoc;
    }

    public Location getDstLoc() {
        return dstLoc;
    }

    public void setDstLoc(Location dstLoc) {
        this.dstLoc = dstLoc;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(int goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getContainerType() {
        return containerType;
    }

    public void setContainerType(int containerType) {
        this.containerType = containerType;
    }

    public int getContainerCap() {
        return containerCap;
    }

    public void setContainerCap(int containerCap) {
        this.containerCap = containerCap;
    }

    public int getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(int containerQty) {
        this.containerQty = containerQty;
    }

    public int getLoadingType() {
        return loadingType;
    }

    public void setLoadingType(int loadingType) {
        this.loadingType = loadingType;
    }

    public String getLoadingAddress() {
        return loadingAddress;
    }

    public void setLoadingAddress(String loadingAddress) {
        this.loadingAddress = loadingAddress;
    }

    public String getLoadingContact() {
        return loadingContact;
    }

    public void setLoadingContact(String loadingContact) {
        this.loadingContact = loadingContact;
    }

    public String getLoadingPhone() {
        return loadingPhone;
    }

    public void setLoadingPhone(String loadingPhone) {
        this.loadingPhone = loadingPhone;
    }

    public Date getLoadingEt() {
        return loadingEt;
    }

    public void setLoadingEt(Date loadingEt) {
        this.loadingEt = loadingEt;
    }

    public long getSalesPriceCny() {
        return salesPriceCny;
    }

    public void setSalesPriceCny(long salesPriceCny) {
        this.salesPriceCny = salesPriceCny;
    }

    public long getSalesPriceUsd() {
        return salesPriceUsd;
    }

    public void setSalesPriceUsd(long salesPriceUsd) {
        this.salesPriceUsd = salesPriceUsd;
    }

    public long getDistyPrice() {
        return distyPrice;
    }

    public void setDistyPrice(long distyPrice) {
        this.distyPrice = distyPrice;
    }

    public long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(long grandTotal) {
        this.grandTotal = grandTotal;
    }

    public long getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(long grossMargin) {
        this.grossMargin = grossMargin;
    }

    public long getSalesProfit() {
        return salesProfit;
    }

    public void setSalesProfit(long salesProfit) {
        this.salesProfit = salesProfit;
    }

    public long getDistyProfit() {
        return distyProfit;
    }

    public void setDistyProfit(long distyProfit) {
        this.distyProfit = distyProfit;
    }

    public String getCreateRole() {
        return createRole;
    }

    public void setCreateRole(String createRole) {
        this.createRole = createRole;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("OrderHistory{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", customer=").append(customer);
        sb.append(", srcLoc=").append(srcLoc);
        sb.append(", dstLoc=").append(dstLoc);
        sb.append(", consignee='").append(consignee).append('\'');
        sb.append(", consigneePhone='").append(consigneePhone).append('\'');
        sb.append(", consigneeAddress='").append(consigneeAddress).append('\'');
        sb.append(", goods=").append(goods);
        sb.append(", goodsWeight=").append(goodsWeight);
        sb.append(", tradeType=").append(tradeType);
        sb.append(", containerType=").append(containerType);
        sb.append(", containerCap=").append(containerCap);
        sb.append(", containerQty=").append(containerQty);
        sb.append(", loadingType=").append(loadingType);
        sb.append(", loadingAddress='").append(loadingAddress).append('\'');
        sb.append(", loadingContact='").append(loadingContact).append('\'');
        sb.append(", loadingPhone='").append(loadingPhone).append('\'');
        sb.append(", loadingEt=").append(loadingEt);
        sb.append(", salesPriceCny=").append(salesPriceCny);
        sb.append(", salesPriceUsd=").append(salesPriceUsd);
        sb.append(", distyPrice=").append(distyPrice);
        sb.append(", grandTotal=").append(grandTotal);
        sb.append(", grossMargin=").append(grossMargin);
        sb.append(", salesProfit=").append(salesProfit);
        sb.append(", distyProfit=").append(distyProfit);
        sb.append(", createRole='").append(createRole).append('\'');
        sb.append(", createUser='").append(createUser).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", finishTime=").append(finishTime);
        sb.append('}');
        return sb.toString();
    }
}
