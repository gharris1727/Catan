package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.Client;

/**
 * Created by greg on 1/21/16.
 * A top-level screen that is supposed to access the client's information directly.
 */
public abstract class ClientScreen extends UIScreen {

    protected final Client client;

    protected ClientScreen(Client client, String layoutKey) {
        super(0, client.getUIConfiguration().narrow(layoutKey));
        this.client = client;
    }

    public abstract void update();
}
