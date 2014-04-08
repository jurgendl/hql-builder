package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;

public class LoginForm implements Serializable {
    private static final long serialVersionUID = 5585041908424613045L;

    private String username;

    private String password;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
