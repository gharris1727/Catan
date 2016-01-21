package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;

/**
 * Created by greg on 1/21/16.
 * A top-level screen that is supposed to access the client's information directly.
 */
public abstract class ClientScreen extends UIScreenRegion {

    protected final Client client;

    public ClientScreen(Client client) {
        super(0);
        this.client = client;
    }
}
