package org.tools.hqlbuilder.demo;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@XmlRootElement
@Entity
@Table(name = "users")
public class User extends EntityERHAdapter implements UserProperties {
    private static final long serialVersionUID = 8069035546088771672L;

    private Boolean enabled = Boolean.TRUE;

    @XmlTransient
    @Basic(fetch = FetchType.LAZY)
    private String password;

    private String firstName;

    private String lastName;

    private String email;

    @OneToOne
    @JoinColumn(name = "username")
    private Member member;

    @ManyToOne
    @XmlElement
    private Lang language;

    public User(String firstName, String lastName, String email) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public User() {
        super();
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Lang getLanguage() {
        return language;
    }

    public void setLanguage(Lang language) {
        this.language = language;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
