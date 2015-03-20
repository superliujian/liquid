package liquid.user.domain;


import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Tao Ma on 3/17/15.
 */
@Entity(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "group_name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    private Collection<GroupMember> members;

    public Group() { }

    public Group(Long id) {
        this.id = id;
    }

    public Group(String name) {
        this.name = name;
    }

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(Collection<GroupMember> members) {
        this.members = members;
    }
}
