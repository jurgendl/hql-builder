package org.tools.hqlbuilder.demo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@XmlRootElement
@Entity
@Table(name = "groups")
public class Group extends EntityERHAdapter implements GroupProperties {
    private static final long serialVersionUID = 3647597053577793581L;

    @Column(name = "group_name")
    private String name;

    @OneToMany(mappedBy = Member.GROUP)
    @XmlTransient
    private List<Member> members;

    @OneToMany(mappedBy = GroupAuthority.GROUP)
    private List<GroupAuthority> authorities;

    public Group() {
        super();
    }

    public Group(String name) {
        setName(name);
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return erh.omGet(MEMBERS, members);
    }

    public void addMember(Member member) {
        erh.omAdd(MEMBERS, member);
    }

    public void removeMember(Member member) {
        erh.omRemove(MEMBERS, member);
    }

    public void setMembers(List<Member> members) {
        erh.omSet(MEMBERS, members);
    }

    public List<GroupAuthority> getAuthorities() {
        return erh.omGet(AUTHORITIES, authorities);
    }

    public void setAuthorities(List<GroupAuthority> authorities) {
        erh.omSet(AUTHORITIES, authorities);
    }

    public void addAuthority(Authority authority) {
        erh.omAdd(AUTHORITIES, authority);
    }

    public void removeAuthority(Authority authority) {
        erh.omRemove(AUTHORITIES, authority);
    }
}
