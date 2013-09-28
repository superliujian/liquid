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
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID") private int id;
    @OneToOne @JoinColumn(name = "CUSTOMER_ID", nullable = true) private Customer customer;
    @Transient private int customerId;
    @Transient private String customerName;
    @Column(name = "ORIGIN") @NotNull @NotEmpty private String origin;
    @Column(name = "DESTINATION") @NotNull @NotEmpty private String destination;
    @Column(name = "CONSIGNEE") @NotNull @NotEmpty private String consignee;
    @OneToOne @JoinColumn(name = "CARGO_ID") private Cargo cargo;
    @Transient private int cargoId;
    @Transient private String cargoName;
    // unit kilogram
    @Column(name = "WEIGHT") @NotNull @NotEmpty private int cargoWeight;
    // 0: domestic; 1: foreign
    @Column(name = "TRADE_TYPE") private int tradeType;
    @Transient private String tradeTypeName;
    // 0: yard; 1: truck
    @Column(name = "LOADING_TYPE") private int loadingType;
    @Transient private String loadingTypeName;
    @Column(name = "HAS_DELIVERY") private boolean hasDelivery;
    // unit yuan
    @Column(name = "PRICE") @NotNull @NotEmpty private long price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    // Transient

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public String getLoadingTypeName() {
        return loadingTypeName;
    }

    public void setLoadingTypeName(String loadingTypeName) {
        this.loadingTypeName = loadingTypeName;
    }

    public int getCargoId() {
        return cargoId;
    }

    public void setCargoId(int cargoId) {
        this.cargoId = cargoId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", customer=").append(customer);
        sb.append(", origin='").append(origin).append('\'');
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", consignee='").append(consignee).append('\'');
        sb.append(", cargo=").append(cargo);
        sb.append(", cargoWeight=").append(cargoWeight);
        sb.append(", tradeType=").append(tradeType);
        sb.append(", loadingType=").append(loadingType);
        sb.append(", hasDelivery=").append(hasDelivery);
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}
