package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.lobby.UserInfo;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

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

    public void registerAccount(UserLogin login) {
        try {
            UserAccount user = new UserAccount(login.username);
            user.setPassword(login.password);
            users.put(login.username, user);
        } catch (Exception e) {
            logger.log(e, LogLevel.ERROR);
        }
    }

    public AuthToken authenticate(UserLogin login) throws UserNotFoundException, AuthenticationException {
        UserAccount user = users.get(login.username);
        if (user == null)
            throw new UserNotFoundException();
        if (!user.validatePassword(login.password))
            throw new AuthenticationException();
        return user.generateAuthToken();
    }

    private void validateAuthToken(AuthToken token) throws UserNotFoundException, AuthenticationException {
        UserAccount user = users.get(token.username);
        if (user == null)
            throw new UserNotFoundException();
        if (!user.validateToken(token))
            throw new AuthenticationException();
    }

    private void invalidateSession(Username username) throws UserNotFoundException {
        UserAccount user = users.get(username);
        if (user == null)
            throw new UserNotFoundException();
        user.invalidateSession();
    }

    public void removeAccount(Username username) throws UserNotFoundException {
        if (users.remove(username) == null)
            throw new UserNotFoundException();
    }

    public void changePassword(Username username, Password password) throws UserNotFoundException {
        UserAccount user = users.get(username);
        if (user == null)
            throw new UserNotFoundException();
        user.setPassword(password);
    }

    public UserInfo getUserInfo(Username username) throws UserNotFoundException {
        UserAccount user = users.get(username);
        if (user == null)
            throw new UserNotFoundException();
        return user.getUserInfo();
    }
    public void changeDisplayName(Username username, String displayName) throws UserNotFoundException {
        UserAccount user = users.get(username);
        if (user == null)
            throw new UserNotFoundException();
        user.setDisplayName(displayName);
    }
}
