package com.gregswebserver.catan.common.game.scoring;

import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.scoring.reporting.Scorable;

/**
 * Created by greg on 5/27/16.
 *
 */
public interface ScoreKeeper extends ReversibleEventConsumer<ScoreEvent>, Scorable {
}
