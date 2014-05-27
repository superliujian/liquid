package liquid.persistence.domain;

import javax.persistence.*;

/**
 * Created by redbrick9 on 5/19/14.
 */
public class ContainerAllocation extends BaseEntity {
    @OneToMany
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @OneToMany
    @JoinColumn(name = "LEG_ID")
    private Leg leg;

    @Column(name = "CONTAINER_TYPE")
    private int containerType;

    @Column(name = "CONTAINER_SUBTYPE")
    private int containerSubtype;

    @Column(name = "BIC_CODE")
    private String bicCode;

    @OneToOne
    @JoinColumn(name = "CONTAINER_ID")
    private ContainerEntity containerEntity;
}
