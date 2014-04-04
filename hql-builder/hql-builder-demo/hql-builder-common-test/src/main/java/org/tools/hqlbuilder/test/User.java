package org.tools.hqlbuilder.test;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "UserTable")
public class User extends Parent {
    private static final long serialVersionUID = 8069035546088771672L;

    private String firstName;

    private String lastName;

    private String email;

    @ManyToOne
    @XmlElement
    private Lang language;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User() {
        super();
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Lang getLanguage() {
        return this.language;
    }

    public void setLanguage(Lang language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
