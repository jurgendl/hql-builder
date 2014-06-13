package org.tools.hqlbuilder.test;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.tools.hqlbuilder.common.EntityI;
import org.tools.hqlbuilder.common.EntityRelationHelper;

@XmlRootElement
@Entity
@Table(name = "group_members")
public class Member implements EntityI, MemberProperties {
    private static final long serialVersionUID = -6012619912508524393L;

    @Version
    private Integer version;

    @Id
    private String username;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @NotNull
    private Group group;

    @OneToMany(mappedBy = "member")
    private List<Authority> authorities;

    @Transient
    protected transient final EntityRelationHelper erh;

    public Member() {
        erh = new EntityRelationHelper(this);
    }

    public Member(String username, Group group) {
        this.username = username;
        this.group = group;
        erh = new EntityRelationHelper(this);
    }

    @Override
    public String toString() {
        return getUsername();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
