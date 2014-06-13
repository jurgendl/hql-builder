package org.tools.hqlbuilder.test;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.tools.hqlbuilder.common.EntityExtAdapter;

@XmlRootElement
@Entity
@Table(name = "groups")
public class Group extends EntityExtAdapter implements GroupProperties {
    private static final long serialVersionUID = 3647597053577793581L;

    @Column(name = "group_name")
    private String name;

    @OneToMany(mappedBy = "group")
    @XmlTransient
    private List<Member> members;

    @OneToMany(mappedBy = "group")
    private List<GroupAuthority> authorities;

    public Group() {
        super();
    }

    public Group(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return this.name;
    }

    public List<GroupAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthorities(List<GroupAuthority> authorities) {
        this.authorities = authorities;
    }

    public List<Member> getMembers() {
        return this.members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
