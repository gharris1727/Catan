package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.common.config.ConfigurationException;

/**
 * Created by greg on 4/2/16.
 * A screen region that can take on qualities specified in the configuration.
 */
public abstract class DefaultConfigurableScreenRegion extends ConfigurableScreenRegion {

    protected DefaultConfigurableScreenRegion(int priority, String configKey) {
        super(priority, configKey);
    }

    @Override
    public void loadConfig(UIConfig config) {
        try {
            setPosition(config.getLayout().getPoint("position"));
        } catch (ConfigurationException ignored) {
        }
        try {
            setMask(RenderMask.parseMask(config.getLayout().narrow("mask")));
        } catch (ConfigurationException ignored) {
        }
    }
}
