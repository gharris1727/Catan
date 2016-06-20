package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;

/**
 * Created by Greg on 1/16/2015.
 * A screen region used as a base for the user interface.
 */
public abstract class ConfigurableScreenRegion extends ScreenRegion implements Configurable {

    private final String configKey;
    private UIConfig config;

    protected ConfigurableScreenRegion(String name, int priority, String configKey) {
        super(name, priority);
        this.configKey = configKey;
    }

    @Override
    public void setConfig(UIConfig config) {
        this.config = config.narrow(configKey);
        loadConfig(this.config);
        for (ScreenObject o : this) {
            if (o instanceof Configurable)
                ((Configurable) o).setConfig(this.config);
        }
        forceRender();
    }

    @Override
    public UIConfig getConfig() {
        return config;
    }

    public String getConfigKey() {
        return configKey;
    }

    @Override
    public boolean isRenderable() {
        return super.isRenderable() && config != null;
    }

    @Override
    public void assertRenderable() {
        super.assertRenderable();
        if (config == null)
            throw new NotYetRenderableException(this + " has no config.");
    }
}
