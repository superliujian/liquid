package liquid.order.persistence.domain;

import liquid.persistence.domain.BaseUpdateEntity;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/14/13
 * Time: 8:34 PM
 */
@MappedSuperclass
public class BaseOrder extends BaseUpdateEntity {
    @Column(name = "SERVICE_TYPE_ID")
    private Long serviceTypeId;

    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "CUSTOMER_ID")
    private long customerId;

    @Column(name = "SRC_LOC_ID")
    private Long srcLocId;

    @Column(name = "DST_LOC_ID")
    private Long dstLocId;

    @Column(name = "CONSIGNEE")
    private String consignee;

    @Column(name = "CONSIGNEE_PHONE")
    private String consigneePhone;

    @Column(name = "CONSIGNEE_ADDR")
    private String consigneeAddress;

    @Column(name = "GOODS_ID")
    private Long goodsId;

    /**
     * unit kilogram
     */
    @Min(1)
    @Column(name = "WEIGHT")
    private int goodsWeight;

    @Column(name = "DIMENSION")
    private String goodsDimension;

    @Column(name = "CONTAINER_TYPE")
    private int containerType;

    // The following three filed are used for list order
    @Column(name = "CONTAINER_SUBTYPE_ID")
    private Long containerSubtypeId;

    @Column(name = "CONTAINER_CAP")
    private int containerCap;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    @Column(name = "CONTAINER_ATTR")
    private String containerAttribute;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    @JoinColumn(name = "RAILWAY_ID")
    private OrderRailEntity railway;

    @Column(name = "TOTAL_CNY")
    private Long totalCny = 0L;

    @Column(name = "TOTAL_USD")
    private Long totalUsd = 0L;

    @Column(name = "DISTY_PRICE")
    private Long distyPrice = 0L;

    @Column(name = "DISTY_USD")
    private Long distyUsd = 0L;

    @Column(name = "GRAND_TOTAL")
    private Long grandTotal = 0L;

    @Column(name = "CREATE_ROLE")
    private String createRole;

    // 1 saved; 2: submitted
    @Column(name = "STATUS")
    private int status;

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Long getSrcLocId() {
        return srcLocId;
    }

    public void setSrcLocId(Long srcLocId) {
        this.srcLocId = srcLocId;
    }

    public Long getDstLocId() {
        return dstLocId;
    }

    public void setDstLocId(Long dstLocId) {
        this.dstLocId = dstLocId;
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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(int goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public String getGoodsDimension() {
        return goodsDimension;
    }

    public void setGoodsDimension(String goodsDimension) {
        this.goodsDimension = goodsDimension;
    }

    public int getContainerType() {
        return containerType;
    }

    public void setContainerType(int containerType) {
        this.containerType = containerType;
    }

    public Long getContainerSubtypeId() {
        return containerSubtypeId;
    }

    public void setContainerSubtypeId(Long containerSubtypeId) {
        this.containerSubtypeId = containerSubtypeId;
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

    public String getContainerAttribute() {
        return containerAttribute;
    }

    public void setContainerAttribute(String containerAttribute) {
        this.containerAttribute = containerAttribute;
    }

    public OrderRailEntity getRailway() {
        return railway;
    }

    public void setRailway(OrderRailEntity railway) {
        this.railway = railway;
    }

    public Long getTotalCny() {
        return totalCny;
    }

    public void setTotalCny(Long totalCny) {
        this.totalCny = totalCny;
    }

    public Long getTotalUsd() {
        return totalUsd;
    }

    public void setTotalUsd(Long totalUsd) {
        this.totalUsd = totalUsd;
    }

    public Long getDistyPrice() {
        return distyPrice;
    }

    public void setDistyPrice(Long distyPrice) {
        this.distyPrice = distyPrice;
    }

    public Long getDistyUsd() {
        return distyUsd;
    }

    public void setDistyUsd(Long distyUsd) {
        this.distyUsd = distyUsd;
    }

    public Long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Long grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getCreateRole() {
        return createRole;
    }

    public void setCreateRole(String createRole) {
        this.createRole = createRole;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
