package liquid.order.persistence.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 2:59 PM
 */
@Entity(name = "ORDER_BASE")
public class OrderEntity extends BaseOrder {
    // 0: domestic; 1: foreign
    @Column(name = "TRADE_TYPE")
    private int tradeType;

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

    @Column(name = "HAS_DELIVERY")
    private boolean hasDelivery;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private List<ServiceItemEntity> serviceItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    private ReceivableSummaryEntity receivableSummary;

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

    public boolean isHasDelivery() {
        return hasDelivery;
    }

    public void setHasDelivery(boolean hasDelivery) {
        this.hasDelivery = hasDelivery;
    }

    public List<ServiceItemEntity> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<ServiceItemEntity> serviceItems) {
        this.serviceItems = serviceItems;
    }

    public ReceivableSummaryEntity getReceivableSummary() {
        return receivableSummary;
    }

    public void setReceivableSummary(ReceivableSummaryEntity receivableSummary) {
        this.receivableSummary = receivableSummary;
    }
}
