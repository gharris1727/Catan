package catan.common.crypto;

import java.io.Serializable;

/**
 * Created by Greg on 1/18/2015.
 * A secret token generated from the server to verify the client's identity.
 */
public final class AuthToken implements Serializable {

    public final Username username;
    private final int sessionID;

    public AuthToken(Username username, int sessionID) {
        this.username = username;
        this.sessionID = sessionID;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof AuthToken) {
            AuthToken at = (AuthToken) o;
            return username.equals(at.username) && (sessionID == at.sessionID);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + sessionID;
        return result;
    }

    public String toString() {
        return "AuthToken: " + username.username;
    }
}
