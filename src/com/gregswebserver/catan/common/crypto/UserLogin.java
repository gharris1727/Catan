package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.network.Identity;

import java.io.Serializable;

/**
 * Created by Greg on 10/17/2014.
 * A set of details pertaining to connecting to a remote server.
 */
public class UserLogin implements Serializable {

    public final Identity identity;
    public final Password password;

    public UserLogin(Identity name, String pass) {
        identity = name;
        password = new Password(pass);
    }
}
