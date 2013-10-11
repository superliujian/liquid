package liquid.persistence.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/10/13
 * Time: 9:15 PM
 */
@Entity(name = "ROUTE")
public class Route extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "PLANNING_ID")
    private Planning planning;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    @Transient
    private Collection<Leg> legs;

    @Transient
    private Collection<ShippingContainer> containers;

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public int getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(int containerQty) {
        this.containerQty = containerQty;
    }

    public Collection<Leg> getLegs() {
        return legs;
    }

    public void setLegs(Collection<Leg> legs) {
        this.legs = legs;
    }

    public Collection<ShippingContainer> getContainers() {
        return containers;
    }

    public void setContainers(Collection<ShippingContainer> containers) {
        this.containers = containers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Route{");
        sb.append("planning=").append(planning);
        sb.append(", containerQty=").append(containerQty);
        sb.append('}');
        return sb.toString();
    }
}
