package catan.common.locale;

import catan.common.config.ConfigSource;

/**
 * Created by greg on 5/28/16.
 * An object responsible for printing out something according to locale information
 */
public abstract class LocalizedPrinter<T> {

    private ConfigSource locale;

    protected LocalizedPrinter(ConfigSource locale) {
        this.locale = locale;
    }

    public void setLocale(ConfigSource locale) {
        this.locale = locale;
    }

    protected String getLocalization(String key) {
        try {
            return locale.get(key);
        } catch (Exception ignored) {
            return key;
        }
    }

    public abstract String getLocalization(T instance);

    public String toString() {
        return "LocalizedPrinter: " + locale;
    }
}
