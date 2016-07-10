package catan.client.graphics.ui;

import catan.client.graphics.masks.RenderMask;
import catan.common.config.ConfigurationException;

/**
 * Created by greg on 4/2/16.
 * A screen region that can take on qualities specified in the configuration.
 */
public abstract class DefaultConfigurableScreenRegion extends ConfigurableScreenRegion {

    protected DefaultConfigurableScreenRegion(String name, int priority, String configKey) {
        super(name, priority, configKey);
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
