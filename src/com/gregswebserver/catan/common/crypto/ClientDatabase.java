package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.lobby.ServerClient;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.NetID;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.security.SecureRandom;
import java.util.HashMap;

import static com.gregswebserver.catan.server.resources.ObjectStoreInfo.Authentication;
import static com.gregswebserver.catan.server.resources.ObjectStoreInfo.UserInfo;

/**
 * Created by Greg on 1/8/2015.
 * A database of clients known to the server.
 */
public class ClientDatabase {

    private Logger logger;
    private HashMap<Identity, String> passwords;
    private HashMap<Identity, ServerClient> clients;
    private HashMap<Identity, Integer> sessionIDs;
    private HashMap<Integer, Identity> identities;

    public ClientDatabase(Logger logger) {
        this.logger = logger;
        sessionIDs = new HashMap<>();
        identities = new HashMap<>();
    }

    public void loadResources() {
        passwords = (HashMap<Identity, String>) ResourceLoader.getObjectStore(Authentication);
        clients = (HashMap<Identity, ServerClient>) ResourceLoader.getObjectStore(UserInfo);
    }

    public void dumpResources() {
        ResourceLoader.saveObjectStore(Authentication);
        ResourceLoader.saveObjectStore(UserInfo);
    }

    public boolean isRegistered(Identity identity) {
        return passwords.containsKey(identity);
    }

    public int authenticate(NetID remote, int connectionID, UserLogin login) {
        if (isRegistered(login.identity) && authenticate(login))
            return generateSessionID(login.identity);
        return 0;
    }

    private boolean authenticate(UserLogin login) {
        String correctHash = passwords.get(login.identity);
        try {
            return PasswordHash.validatePassword(login.password.getPassword(), correctHash);
        } catch (Exception e) {
            logger.log(e, LogLevel.ERROR);
            return false;
        }
    }

    private int generateSessionID(Identity identity) {
        revokeSessionID(identity);
        int sessionID = new SecureRandom().nextInt();
        sessionIDs.put(identity, sessionID);
        identities.put(sessionID, identity);
        return sessionID;
    }

    private void revokeSessionID(Identity identity) {
        identities.remove(sessionIDs.remove(identity));
    }

    private void revokeSessionID(int sessionID) {
        sessionIDs.remove(identities.remove(sessionIDs));
    }

    public boolean registerLogin(UserLogin login) {
        try {
            String correctHash = PasswordHash.createHash(login.password.getPassword());
            passwords.put(login.identity, correctHash);
            return true;
        } catch (Exception e) {
            logger.log(e, LogLevel.ERROR);
            return false;
        }
    }

    public boolean removeLogin(Identity user) {
        passwords.remove(user);
        return true;
    }

    public boolean passChange(Identity identity, Password password) {
        return removeLogin(identity) && registerLogin(new UserLogin(identity, password));
    }


    public ServerClient getServerClient(int sessionID) {
        Identity identity = identities.get(sessionID);
        if (identity == null) return null;
        return clients.get(identity);
    }

    public boolean validateSessionID(int sessionID, Identity origin) {
        return sessionIDs.get(origin) == sessionID;
    }
}
