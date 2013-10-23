package liquid.dto;

import java.io.Serializable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/21/13
 * Time: 8:14 PM
 */
public class ExtraDto implements Serializable {
    private long extExp = 0L;

    private String extExpComment;

    public long getExtExp() {
        return extExp;
    }

    public void setExtExp(long extExp) {
        this.extExp = extExp;
    }

    public String getExtExpComment() {
        return extExpComment;
    }

    public void setExtExpComment(String extExpComment) {
        this.extExpComment = extExpComment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ExtraDto{");
        sb.append("extExp=").append(extExp);
        sb.append(", extExpComment='").append(extExpComment).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
