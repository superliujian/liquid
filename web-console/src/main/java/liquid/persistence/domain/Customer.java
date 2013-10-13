package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 10:10 PM
 */
@Entity(name = "CUSTOMER")
public class Customer extends BaseEntity {
    @Column(name = "NAME")
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    @Column(name = "ADDRESS")
    private String address;

    @NotNull
    @NotEmpty
    @Column(name = "POSTCODE")
    private String postcode;

    @NotNull
    @NotEmpty
    @Column(name = "CONTACT")
    private String contact;

    @NotNull
    @NotEmpty
    @Column(name = "PHONE")
    private String phone;

    @NotNull
    @NotEmpty
    @Column(name = "CELL")
    private String cell;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Customer{");
        sb.append("name='").append(name).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", postcode='").append(postcode).append('\'');
        sb.append(", contact='").append(contact).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", cell='").append(cell).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
