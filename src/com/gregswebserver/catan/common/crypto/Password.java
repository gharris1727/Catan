package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.event.EventPayload;

/**
 * Created by Greg on 10/17/2014.
 * A password that is passed over the network to login to a server.
 */
public final class Password extends EventPayload {

    private final String password;

    public Password(String password) {
        this.password = password;
    }

    protected String getPassword() {
        return password;
    }

    public String getHiddenPassword() {
        String out = "";
        for (int i = 0; i < password.length(); i++) {
            out += "*";
        }
        return out;
    }

    public String toString() {
        return "Password: " + getHiddenPassword();
    }
}
