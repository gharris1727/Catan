package com.gregswebserver.catan.common.crypto;

import java.util.Random;

/**
 * Created by Greg on 10/17/2014.
 * A password that is passed over the network to login to a server.
 */
public class Password {

    private int hash;
    private int salt;

    public Password(String pass) {
        salt = new Random().nextInt();
    }
}
