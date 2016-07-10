package catan.server.structure;

import catan.common.config.PropertiesFile;
import catan.common.crypto.*;
import catan.common.resources.PropertiesFileInfo;
import catan.common.resources.ResourceLoader;
import catan.common.structure.UserInfo;
import catan.common.structure.UserLogin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 1/8/2015.
 * A database of users known to the server.
 */
public class UserDatabase {

    private final PropertiesFileInfo databaseFile;
    private final PropertiesFile database;
    private final Map<Username, UserAccount> accounts;

    public UserDatabase(PropertiesFileInfo databaseFile) {
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
            database.setEntry(entry.getKey().username, entry.getValue().toString());
        ResourceLoader.savePropertiesFile(databaseFile);
    }

    public void registerAccount(UserLogin login) throws RegistrationException {
        if (accounts.containsKey(login.username))
            throw new RegistrationException("Unable to add user account.");
        UserAccount user = new UserAccount(login.username, login.password);
        accounts.put(login.username, user);
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
