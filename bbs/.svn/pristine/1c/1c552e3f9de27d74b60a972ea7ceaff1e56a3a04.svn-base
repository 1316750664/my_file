package com.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * on 2014/11/27.
 */
public class MySendMailAuthenticator extends Authenticator {
    String username = null;
    String password = null;

    public MySendMailAuthenticator() {
    }

    public MySendMailAuthenticator(String user, String pass) {
        this.username = user;
        this.password = pass;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
