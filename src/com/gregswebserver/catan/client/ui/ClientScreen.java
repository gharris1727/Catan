package com.gregswebserver.catan.client.ui;

import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;

/**
 * Created by greg on 1/21/16.
 * A top-level screen that is supposed to access the client's information directly.
 */
public abstract class ClientScreen extends ConfigurableScreenRegion {

    protected ClientScreen(String configKey) {
        super(0, configKey);
    }

    public abstract void refresh();
}
