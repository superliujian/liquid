package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 2:59 PM
 */
@Entity(name = "ORDER_BASE")
public class Order extends BaseEntity {
    @Transient
    private long customerId;

    @OneToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(name = "ORIGIN")
    @NotNull
    @NotEmpty
    private String origin;

    @Column(name = "DESTINATION")
    @NotNull
    @NotEmpty
    private String destination;

    @Column(name = "CONSIGNEE")
    @NotNull
    @NotEmpty
    private String consignee;

    @Transient
    private long cargoId;

    @OneToOne
    @JoinColumn(name = "CARGO_ID")
    private Cargo cargo;

    // unit kilogram
    @Column(name = "WEIGHT")
    private int cargoWeight;

    // 0: domestic; 1: foreign
    @Column(name = "TRADE_TYPE")
    private int tradeType;

    // 0: yard; 1: truck
    @Column(name = "LOADING_TYPE")
    private int loadingType;

    @Column(name = "HAS_DELIVERY")
    private boolean hasDelivery;

    // unit yuan
    @Column(name = "PRICE")
    private long price;

    // 1 saved; 2: submitted
    @Column(name = "STATUS")
    private int status;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public int getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(int cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getLoadingType() {
        return loadingType;
    }

    public void setLoadingType(int loadingType) {
        this.loadingType = loadingType;
    }

    public boolean isHasDelivery() {
        return hasDelivery;
    }

    public void setHasDelivery(boolean hasDelivery) {
        this.hasDelivery = hasDelivery;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Transient

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getCargoId() {
        return cargoId;
    }

    public void setCargoId(long cargoId) {
        this.cargoId = cargoId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Order{");
        sb.append("customerId=").append(customerId);
        sb.append(", customer=").append(customer);
        sb.append(", origin='").append(origin).append('\'');
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", consignee='").append(consignee).append('\'');
        sb.append(", cargoId=").append(cargoId);
        sb.append(", cargo=").append(cargo);
        sb.append(", cargoWeight=").append(cargoWeight);
        sb.append(", tradeType=").append(tradeType);
        sb.append(", loadingType=").append(loadingType);
        sb.append(", hasDelivery=").append(hasDelivery);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
