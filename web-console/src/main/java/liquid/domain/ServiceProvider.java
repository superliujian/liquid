package liquid.domain;

import java.util.Arrays;

/**
 * Created by redbrick9 on 6/9/14.
 */
public class ServiceProvider extends BaseIdObject {
    private String code;
    private String name;
    private Long typeId;
    private String address;
    private String postcode;
    private String contact;
    private String phone;
    private String cell;
    private Long[] subtypeIds;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
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

    public Long[] getSubtypeIds() {
        return subtypeIds;
    }

    public void setSubtypeIds(Long[] subtypeIds) {
        this.subtypeIds = subtypeIds;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceProvider{");
        sb.append("super=").append(super.toString());
        sb.append(", code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", typeId=").append(typeId);
        sb.append(", address='").append(address).append('\'');
        sb.append(", postcode='").append(postcode).append('\'');
        sb.append(", contact='").append(contact).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", cell='").append(cell).append('\'');
        sb.append(", subtypeIds=").append(Arrays.toString(subtypeIds));
        sb.append('}');
        return sb.toString();
    }
}
