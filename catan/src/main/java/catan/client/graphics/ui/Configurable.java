package catan.client.graphics.ui;

import catan.client.graphics.screen.Graphical;

/**
 * Created by greg on 1/15/16.
 * Abstraction for components that have a UIConfig associated with them.
 */
public interface Configurable extends Graphical {

    void setConfig(UIConfig config);

    UIConfig getConfig();

    default void loadConfig(UIConfig config) {}
}
