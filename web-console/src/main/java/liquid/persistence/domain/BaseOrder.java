package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/14/13
 * Time: 8:34 PM
 */
@MappedSuperclass
public class BaseOrder extends BaseEntity {
    @Transient
    private long serviceTypeId;

    @ManyToOne
    @JoinColumn(name = "SERVICE_TYPE_ID")
    private ServiceTypeEntity serviceType;

    @Column(name = "ORDER_NO")
    private String orderNo;

    @Transient
    private long customerId;

    /**
     * Can't use customerName, there is a conflict with Repository
     */
    @NotNull
    @NotEmpty
    @Transient
    private String customerName0;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "SRC_LOC_ID")
    private LocationEntity srcLoc;

    @Transient
    private long origination;

    @ManyToOne
    @JoinColumn(name = "DST_LOC_ID")
    private LocationEntity dstLoc;

    @Transient
    private long destination;

    @Column(name = "CONSIGNEE")
    private String consignee;

    @Column(name = "CONSIGNEE_PHONE")
    private String consigneePhone;

    @Column(name = "CONSIGNEE_ADDR")
    private String consigneeAddress;

    @Transient
    private long goodsId;

    @ManyToOne
    @JoinColumn(name = "GOODS_ID")
    private GoodsEntity goods;

    /**
     * unit kilogram
     */
    @Min(1)
    @Column(name = "WEIGHT")
    private int goodsWeight;

    @Column(name = "CONTAINER_TYPE")
    private int containerType;

    @Transient
    private long containerSubtypeId;

    @Transient
    private long railContainerSubtypeId;

    @Transient
    private long ownContainerSubtypeId;

    @ManyToOne
    @JoinColumn(name = "CONTAINER_SUBTYPE_ID")
    private ContainerSubtypeEntity containerSubtype;

    @Column(name = "CONTAINER_CAP")
    private int containerCap;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    /**
     * unit yuan
     */
    @Min(1)
    @Column(name = "SALES_PRICE_CNY")
    private long salesPriceCny;

    @Column(name = "DISTY_PRICE")
    private long distyPrice;

    @Column(name = "GRAND_TOTAL")
    private long grandTotal;

    @Column(name = "USD_TOTAL")
    private long usdTotal;

    @Column(name = "CNY_TOTAL")
    private long cnyTotal;

    @Column(name = "CREATE_ROLE")
    private String createRole;

    // 1 saved; 2: submitted
    @Column(name = "STATUS")
    private int status;

    @Transient
    private List<ServiceItemEntity> serviceItems = new ArrayList<>();

    public long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public ServiceTypeEntity getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeEntity serviceType) {
        this.serviceType = serviceType;
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

    public String getCustomerName0() {
        return customerName0;
    }

    public void setCustomerName0(String customerName0) {
        this.customerName0 = customerName0;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public LocationEntity getSrcLoc() {
        return srcLoc;
    }

    public void setSrcLoc(LocationEntity srcLoc) {
        this.srcLoc = srcLoc;
    }

    public long getOrigination() {
        return origination;
    }

    public void setOrigination(long origination) {
        this.origination = origination;
    }

    public LocationEntity getDstLoc() {
        return dstLoc;
    }

    public void setDstLoc(LocationEntity dstLoc) {
        this.dstLoc = dstLoc;
    }

    public long getDestination() {
        return destination;
    }

    public void setDestination(long destination) {
        this.destination = destination;
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

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public GoodsEntity getGoods() {
        return goods;
    }

    public void setGoods(GoodsEntity goods) {
        this.goods = goods;
    }

    public int getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(int goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public int getContainerType() {
        return containerType;
    }

    public void setContainerType(int containerType) {
        this.containerType = containerType;
    }

    public long getContainerSubtypeId() {
        return containerSubtypeId;
    }

    public void setContainerSubtypeId(long containerSubtypeId) {
        this.containerSubtypeId = containerSubtypeId;
    }

    public long getRailContainerSubtypeId() {
        return railContainerSubtypeId;
    }

    public void setRailContainerSubtypeId(long railContainerSubtypeId) {
        this.railContainerSubtypeId = railContainerSubtypeId;
    }

    public long getOwnContainerSubtypeId() {
        return ownContainerSubtypeId;
    }

    public void setOwnContainerSubtypeId(long ownContainerSubtypeId) {
        this.ownContainerSubtypeId = ownContainerSubtypeId;
    }

    public ContainerSubtypeEntity getContainerSubtype() {
        return containerSubtype;
    }

    public void setContainerSubtype(ContainerSubtypeEntity containerSubtype) {
        this.containerSubtype = containerSubtype;
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

    public long getSalesPriceCny() {
        return salesPriceCny;
    }

    public void setSalesPriceCny(long salesPriceCny) {
        this.salesPriceCny = salesPriceCny;
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

    public String getCreateRole() {
        return createRole;
    }

    public void setCreateRole(String createRole) {
        this.createRole = createRole;
    }

    public long getUsdTotal() {
        return usdTotal;
    }

    public void setUsdTotal(long usdTotal) {
        this.usdTotal = usdTotal;
    }

    public long getCnyTotal() {
        return cnyTotal;
    }

    public void setCnyTotal(long cnyTotal) {
        this.cnyTotal = cnyTotal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ServiceItemEntity> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<ServiceItemEntity> serviceItems) {
        this.serviceItems = serviceItems;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseOrder{");
        sb.append("serviceTypeId=").append(serviceTypeId);
        sb.append(", serviceType=").append(serviceType);
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", customerId=").append(customerId);
        sb.append(", customerName0='").append(customerName0).append('\'');
        sb.append(", customer=").append(customer);
        sb.append(", srcLoc=").append(srcLoc);
        sb.append(", origination=").append(origination);
        sb.append(", dstLoc=").append(dstLoc);
        sb.append(", destination=").append(destination);
        sb.append(", consignee='").append(consignee).append('\'');
        sb.append(", consigneePhone='").append(consigneePhone).append('\'');
        sb.append(", consigneeAddress='").append(consigneeAddress).append('\'');
        sb.append(", goodsId=").append(goodsId);
        sb.append(", goods=").append(goods);
        sb.append(", goodsWeight=").append(goodsWeight);
        sb.append(", containerType=").append(containerType);
        sb.append(", containerSubtypeId=").append(containerSubtypeId);
        sb.append(", railContainerSubtypeId=").append(railContainerSubtypeId);
        sb.append(", ownContainerSubtypeId=").append(ownContainerSubtypeId);
        sb.append(", containerSubtype=").append(containerSubtype);
        sb.append(", containerCap=").append(containerCap);
        sb.append(", containerQty=").append(containerQty);
        sb.append(", salesPriceCny=").append(salesPriceCny);
        sb.append(", distyPrice=").append(distyPrice);
        sb.append(", grandTotal=").append(grandTotal);
        sb.append(", createRole='").append(createRole).append('\'');
        sb.append(", usdTotal=").append(usdTotal);
        sb.append(", cnyTotal=").append(cnyTotal);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
