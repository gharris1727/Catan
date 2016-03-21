package com.gregswebserver.catan.server.structure;

import com.gregswebserver.catan.common.crypto.*;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.resources.PropertiesFile;
import com.gregswebserver.catan.common.resources.PropertiesFileInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.structure.UserInfo;
import com.gregswebserver.catan.common.structure.UserLogin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 1/8/2015.
 * A database of users known to the server.
 */
public class UserDatabase {

    private final Logger logger;
    private final PropertiesFileInfo databaseFile;
    private final PropertiesFile database;
    private final Map<Username, UserAccount> accounts;

    public UserDatabase(Logger logger, PropertiesFileInfo databaseFile) {
        this.logger = logger;
        this.databaseFile = databaseFile;
        this.database = ResourceLoader.getPropertiesFile(databaseFile);
        accounts = new HashMap<>();
        for (Map.Entry<String, String> entry : database) {
            Username username = new Username(entry.getKey());
            UserAccount account = new UserAccount(username, entry.getValue());
            accounts.put(username, account);
        }
    }

    public void save() {
        for (Map.Entry<Username, UserAccount> entry : accounts.entrySet())
            database.set(entry.getKey().username, entry.getValue().toString());
        ResourceLoader.savePropertiesFile(databaseFile);
    }

    public void registerAccount(UserLogin login) {
        try {
            UserAccount user = new UserAccount(login.username, login.password);
            accounts.put(login.username, user);
        } catch (Exception e) {
            logger.log(e, LogLevel.ERROR);
        }
    }

    public AuthToken authenticate(UserLogin login) throws UserNotFoundException, AuthenticationException {
        UserAccount user = accounts.get(login.username);
        if (user == null)
            throw new UserNotFoundException();
        if (!user.validatePassword(login.password))
            throw new AuthenticationException();
        return user.generateAuthToken();
    }

    public void validateAuthToken(AuthToken token) throws UserNotFoundException, AuthenticationException {
        UserAccount user = accounts.get(token.username);
        if (user == null)
            throw new UserNotFoundException();
        if (!user.validateToken(token))
            throw new AuthenticationException();
    }

    public void invalidateSession(Username username) throws UserNotFoundException {
        UserAccount user = accounts.get(username);
        if (user == null)
            throw new UserNotFoundException();
        user.invalidateSession();
    }

    public void removeAccount(Username username) throws UserNotFoundException {
        if (accounts.remove(username) == null)
            throw new UserNotFoundException();
    }

    public void changePassword(Username username, Password password) throws UserNotFoundException {
        UserAccount user = accounts.get(username);
        if (user == null)
            throw new UserNotFoundException();
        user.setPassword(password);
    }

    public UserInfo getUserInfo(Username username) throws UserNotFoundException {
        UserAccount user = accounts.get(username);
        if (user == null)
            throw new UserNotFoundException();
        return user.getUserInfo();
    }
    public void changeDisplayName(Username username, String displayName) throws UserNotFoundException {
        UserAccount user = accounts.get(username);
        if (user == null)
            throw new UserNotFoundException();
        user.setDisplayName(displayName);
    }
}
