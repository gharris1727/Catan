package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.common.resources.ConfigSource;

/**
 * Created by greg on 3/25/16.
 * An interface configuration, that contains information necessary to configure the user interface.
 */
public class UIConfig {

    private final ConfigSource layout;
    private final ConfigSource locale;

    public UIConfig(ConfigSource layout, ConfigSource locale) {
        this.layout = layout;
        this.locale = locale;
    }

    public ConfigSource getLayout() {
        return layout;
    }

    public String getLocalization(String key) {
        try {
            return locale.get(key);
        } catch (Exception ignored) {
            return key;
        }
    }

    public UIConfig narrow(String prefix) {
        return new UIConfig(layout.narrow(prefix), locale.narrow(prefix));
    }
}
