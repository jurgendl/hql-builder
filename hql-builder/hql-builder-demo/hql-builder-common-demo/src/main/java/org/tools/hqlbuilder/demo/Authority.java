package org.tools.hqlbuilder.demo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@XmlRootElement
@Entity
@Table(name = "authorities")
public class Authority extends EntityERHAdapter implements AuthorityProperties {
    private static final long serialVersionUID = -5692615727091910274L;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "username")
    @XmlTransient
    private Member member;

    @NotNull
    private String authority;

    public Authority() {
        super();
    }

    public Authority(String authority, Member member) {
        setAuthority(authority);
        setMember(member);
    }

    @Override
    public String toString() {
        return getAuthority();
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        erh.moSet(MEMBER, member);
    }
}
