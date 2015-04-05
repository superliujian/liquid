package liquid.operation.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 4/5/15.
 */
@Entity(name = "OPS_LOCATION_TYPE")
public class LocationType {
    public static final Byte CITY = 1;
    public static final Byte STATION = 2;
    public static final Byte PORT = 3;
    public static final Byte YARD = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Byte id;

    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    public LocationType() { }

    public LocationType(Byte id) {
        this.id = id;
    }

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
