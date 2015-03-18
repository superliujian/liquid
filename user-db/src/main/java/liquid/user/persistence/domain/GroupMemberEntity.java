package liquid.user.persistence.domain;

import javax.persistence.*;

/**
 * Created by Tao Ma on 3/18/15.
 */
@Entity(name = "group_members")
public class GroupMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    public GroupMemberEntity() { }

    public GroupMemberEntity(String username, GroupEntity group) {
        this.username = username;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }
}
