package liquid.persistence.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/29/13
 * Time: 9:06 PM
 */
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPDATE_USER")
    private String updateUser;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    public static <E extends BaseEntity> E newInstance(Class<E> clazz, long id) {
        try {
            E entity = clazz.newInstance();
            entity.setId(id);
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new EntityInstantiationException(e);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isNew() {
        return (this.id == null);
    }

    public BaseEntity() {
        this(null);
    }

    public BaseEntity(String createUser) {
        this.createUser = createUser;
        this.createTime = new Date();
        this.updateUser = this.createUser;
        this.updateTime = this.createTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("BaseEntity{");
        sb.append("id=").append(id);
        sb.append(", createUser='").append(createUser).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser='").append(updateUser).append('\'');
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}
