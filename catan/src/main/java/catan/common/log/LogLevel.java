package catan.common.log;

/**
 * Created by Greg on 8/11/2014.
 * Level enum indicating the relative severity of a logged event.
 */
public enum LogLevel {

    DEBUG, INFO, WARN, POPUP, ERROR, UNKNOWN;

    public boolean contains(LogLevel other) {
        return other.compareTo(this) >= 0;
    }
}
