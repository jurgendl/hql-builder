package org.tools.hqlbuilder.test;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.tools.hqlbuilder.common.EntityExtAdapter;

@XmlRootElement
@Entity
@Table(name = "group_authorities")
public class GroupAuthority extends EntityExtAdapter implements GroupAuthorityProperties {
    private static final long serialVersionUID = -5692615727091910274L;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @NotNull
    @XmlTransient
    private Group group;

    @NotNull
    private String authority;

    public GroupAuthority() {
        super();
    }

    public GroupAuthority(String authority, Group group) {
        this.authority = authority;
        this.group = group;
    }

    @Override
    public String toString() {
        return getAuthority();
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
