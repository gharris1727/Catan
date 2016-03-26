package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.screen.Renderable;

/**
 * Created by greg on 1/15/16.
 * Abstraction for components that have a UIConfig associated with them.
 */
public interface Configurable extends Renderable {

    void setConfig(UIConfig config);

    UIConfig getConfig();

    default void loadConfig(UIConfig config) {}
}
