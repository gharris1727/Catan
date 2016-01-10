package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.lobby.UserInfo;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by greg on 1/7/16.
 * This class stores all of the cryptographic and session details for a client that has connected to the server.
 * Needs to be serialized for long-term storage.
 */
public class UserAccount implements Serializable {

    private final Username username;
    private String passwordHash;
    private String displayName;
    private transient AuthToken token;

    public UserAccount(Username username) {
        this.username = username;
        this.displayName = username.username;
        invalidateSession();
    }

    public void setPassword(Password password) {
        try {
            passwordHash = PasswordHash.createHash(password.getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            passwordHash = "";
        }
    }

    public boolean validatePassword(Password password) {
        try {
            return PasswordHash.validatePassword(password.getPassword(), passwordHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return false;
        }
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean validateToken(AuthToken token) {
        return (this.token != null) && this.token.equals(token);
    }

    public AuthToken generateAuthToken() {
        return token = new AuthToken(username, new SecureRandom().nextInt());
    }

    public void invalidateSession() {
        token = null;
    }

    public UserInfo getUserInfo() {
        return new UserInfo(username, displayName);
    }
}
