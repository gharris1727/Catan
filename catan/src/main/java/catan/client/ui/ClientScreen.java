package catan.client.ui;

import catan.client.graphics.ui.ConfigurableScreenRegion;
import catan.client.graphics.ui.Updatable;

/**
 * Created by greg on 1/21/16.
 * A top-level screen that is supposed to access the client's information directly.
 */
public abstract class ClientScreen extends ConfigurableScreenRegion implements Updatable {

    protected ClientScreen(String name, String configKey) {
        super(name, 0, configKey);
    }
}
