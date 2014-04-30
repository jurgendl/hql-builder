package org.tools.hqlbuilder.test;

import java.util.Date;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Registration extends EntityParent {
    private static final long serialVersionUID = -787192560827992242L;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String regkey;

    private Date verification;

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getVerification() {
        return this.verification;
    }

    public void setVerification(Date verification) {
        this.verification = verification;
    }

    public String getRegkey() {
        return this.regkey;
    }

    public void setRegkey(String regkey) {
        this.regkey = regkey;
    }
}
