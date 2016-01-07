package com.gregswebserver.catan.common.crypto;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by greg on 1/7/16.
 * This class stores all of the cryptographic and session details for a client that has connected to the server.
 */
public class UserAccount implements Serializable {

    private final Username username;
    private String passwordHash;
    private transient AuthToken token;

    public UserAccount(Username username) {
        this.username = username;
        invalidateSession();
    }

    public boolean validateToken(AuthToken token) {
        return (this.token != null) && this.token.equals(token);
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

    public AuthToken generateAuthToken() {
        return token = new AuthToken(username, new SecureRandom().nextInt());
    }

    public void invalidateSession() {
        token = null;
    }
}
