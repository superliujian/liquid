package liquid.user.db.persistence.domain;


import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Tao Ma on 3/17/15.
 */
@Entity(name = "groups")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "group_name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    private Collection<GroupMemberEntity> members;

    public GroupEntity() { }

    public GroupEntity(Long id) {
        this.id = id;
    }

    public GroupEntity(String name) {
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

    public Collection<GroupMemberEntity> getMembers() {
        return members;
    }

    public void setMembers(Collection<GroupMemberEntity> members) {
        this.members = members;
    }
}
