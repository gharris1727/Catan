package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

import javax.naming.AuthenticationException;
import java.util.HashMap;

/**
 * Created by Greg on 1/8/2015.
 * A database of users known to the server.
 */
public class UserDatabase {

    private Logger logger;
    private HashMap<Username, UserAccount> users;

    public UserDatabase(Logger logger) {
        this.logger = logger;
        loadResources();
    }

    public void loadResources() {
        //users = (HashMap<Username, UserAccount>) ResourceLoader.getObjectStore(UserInfo);
        //TODO: handle this better, remove dummy data
        users = new HashMap<>();
        registerAccount(new UserLogin(new Username("Greg"),new Password("password")));
    }

    public void dumpResources() {
        //TODO: save the database properly.
        //ResourceLoader.saveObjectStore(UserInfo);
    }

    public AuthToken authenticate(UserLogin login) throws AuthenticationException {
        UserAccount user = users.get(login.username);
        if (user == null)
            throw new AuthenticationException("Username not registered");
        if (!user.validatePassword(login.password))
            throw new AuthenticationException("Password invalid");
        return user.generateAuthToken();
    }

    private void validateAuthToken(AuthToken token) throws AuthenticationException {
        UserAccount user = users.get(token.username);
        if (user == null)
            throw new AuthenticationException("Username not registered");
        if (!user.validateToken(token))
            throw new AuthenticationException("AuthToken invalid");
    }

    private void invalidateSession(Username username) {
        UserAccount user = users.get(username);
        if (user != null) user.invalidateSession();
    }

    public boolean registerAccount(UserLogin login) {
        try {
            UserAccount user = new UserAccount(login.username);
            user.setPassword(login.password);
            users.put(login.username, user);
            return true;
        } catch (Exception e) {
            logger.log(e, LogLevel.ERROR);
            return false;
        }
    }

    public void removeAccount(Username username) {
        users.remove(username);
    }

    public void changePassword(Username username, Password password) {
        UserAccount user = users.get(username);
        user.setPassword(password);
    }
}
