package catan.common.game.scoring.reporting;

/**
 * Created by greg on 5/28/16.
 * An exception that occurs when generating a score report.
 */
public class ScoreException extends Exception {

    public ScoreException(String message) {
        super(message);
    }

    public ScoreException(String message, Exception cause) {
        super(message, cause);
    }
}
