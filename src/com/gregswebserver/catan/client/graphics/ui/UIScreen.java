package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.common.resources.ConfigSource;

/**
 * Created by greg on 3/22/16.
 * Screen region that has a style and a layout.
 */
public abstract class UIScreen extends StyledScreenRegion {

    private final UIConfig config;

    protected UIScreen(int priority, UIConfig config) {
        super(priority);
        this.config = config;
    }

    protected UIScreen(int priority, UIScreen parent, String prefix) {
        super(priority);
        this.config = parent.config.narrow(prefix);
    }

    public ConfigSource getLayout() {
        return config.getLayout();
    }

    public String getLocalization(String key) {
        return config.getLocalization(key);
    }
}
