package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.server.Server;

import java.util.HashMap;

/**
 * Created by Greg on 12/28/2014.
 * Class for managing user accounts and authenticating users logging in.
 */
public class Authenticator {

    private final Logger logger;
    private HashMap<Identity, String> passwords;

    public Authenticator(Server server) {
        logger = server.logger;
        //TODO: add some method of loading external passwords.
        passwords = new HashMap<>();
    }

    public boolean authLogin(UserLogin login) {
        return true;
        //TODO: actually authenticate passwords properly.
//        String correctHash = passwords.get(login.identity);
//        try {
//            return PasswordHash.validatePassword(login.password.getPassword(), correctHash);
//        } catch (Exception e) {
//            logger.log(e, LogLevel.ERROR);
//            return false;
//        }
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

    public boolean changeLogin(Identity identity, Password password) {
        return removeLogin(identity) && registerLogin(new UserLogin(identity, password));
    }

    public boolean removeLogin(Identity user) {
        passwords.remove(user);
        return true;
    }
}
