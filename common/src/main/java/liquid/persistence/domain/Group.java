package liquid.persistence.domain;

import java.util.Set;
import java.util.TreeSet;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/6/13
 * Time: 12:34 PM
 */
public class Group {
    private String name;

    private Set uniqueMembers = new TreeSet();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set getUniqueMembers() {
        return uniqueMembers;
    }

    public void setUniqueMembers(Set uniqueMembers) {
        this.uniqueMembers = uniqueMembers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Group{");
        sb.append("name='").append(name).append('\'');
        sb.append(", uniqueMembers=").append(uniqueMembers);
        sb.append('}');
        return sb.toString();
    }
}
