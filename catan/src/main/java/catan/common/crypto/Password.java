package catan.common.crypto;

import catan.common.event.EventPayload;

/**
 * Created by Greg on 10/17/2014.
 * A password that is passed over the network to login to a server.
 */
public final class Password extends EventPayload {

    private final String password;

    public Password(String password) {
        this.password = password;
    }

    String getPassword() {
        return password;
    }

    public String getHiddenPassword() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            out.append("*");
        }
        return out.toString();
    }

    public String toString() {
        return "Password: " + getHiddenPassword();
    }
}
