package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.screen.Graphical;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;

/**
 * Created by greg on 2/28/16.
 * Class representing a graphic object that depends on a UIConfig
 */
public abstract class ConfigurableGraphicObject extends GraphicObject implements Configurable, Graphical {

    private final String configKey;
    private UIConfig config;

    protected ConfigurableGraphicObject(int priority, String configKey) {
        super(priority);
        this.configKey = configKey;
    }

    @Override
    public void setConfig(UIConfig config) {
        this.config = config.narrow(configKey);
        loadConfig(this.config);
    }

    @Override
    public UIConfig getConfig() {
        return config;
    }

    @Override
    public void forceRender() { }

    @Override
    public boolean isRenderable() {
        return config != null;
    }

    @Override
    public void assertRenderable() {
        if (config == null)
            throw new NotYetRenderableException(this + " has no config.");
    }
}
