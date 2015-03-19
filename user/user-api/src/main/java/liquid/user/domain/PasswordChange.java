package liquid.user.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by redbrick9 on 5/2/14.
 */
public class PasswordChange {
    private String uid;

    @NotNull
    @NotEmpty
    private String oldPassword;

    @NotNull
    @NotEmpty
    private String newPassword;

    @NotNull
    @NotEmpty
    private String newPassword2;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PasswordChange{");
        sb.append("uid='").append(uid).append('\'');
        sb.append(", oldPassword='").append(oldPassword).append('\'');
        sb.append(", newPassword='").append(newPassword).append('\'');
        sb.append(", newPassword2='").append(newPassword2).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
