package catan.common.game.scoring;

import catan.common.event.ReversibleEventConsumer;
import catan.common.game.scoring.reporting.scores.Scorable;

/**
 * Created by greg on 5/27/16.
 *
 */
public interface ScoreKeeper extends ReversibleEventConsumer<ScoreEvent>, Scorable {
}
