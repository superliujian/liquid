package liquid.user.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/6/13
 * Time: 11:36 AM
 */
public class User {
    @NotNull
    @NotEmpty
    private String uid;

    private Boolean enabled;

    @NotNull
    @NotEmpty
    private String surname;

    @NotNull
    @NotEmpty
    private String givenName;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String password2;

    @NotNull
    @NotEmpty
    private String email;

    private String cell;

    private String phone;

    private String group;

    private String description;

    private String pwdAccountLockedTime;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPwdAccountLockedTime() {
        return pwdAccountLockedTime;
    }

    public void setPwdAccountLockedTime(String pwdAccountLockedTime) {
        this.pwdAccountLockedTime = pwdAccountLockedTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("uid='").append(uid).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", givenName='").append(givenName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", password2='").append(password2).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", cell='").append(cell).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", pwdAccountLockedTime='").append(pwdAccountLockedTime).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
