package liquid.persistence.domain;

/**
 * Created by redbrick9 on 5/1/14.
 */
public class PasswordPolicy {
    private int pwdMaxFailure;

    public int getPwdMaxFailure() {
        return pwdMaxFailure;
    }

    public void setPwdMaxFailure(int pwdMaxFailure) {
        this.pwdMaxFailure = pwdMaxFailure;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PasswordPolicy{");
        sb.append("pwdMaxFailure=").append(pwdMaxFailure);
        sb.append('}');
        return sb.toString();
    }
}
