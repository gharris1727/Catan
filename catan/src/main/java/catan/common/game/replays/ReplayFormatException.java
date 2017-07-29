package catan.common.game.replays;

/**
 * Created by greg on 7/4/17.
 * Exception thrown when a loaded replay has a formatting issue.
 */
public class ReplayFormatException extends Exception {
    public ReplayFormatException(String message) {
        super(message);
    }

    public ReplayFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
