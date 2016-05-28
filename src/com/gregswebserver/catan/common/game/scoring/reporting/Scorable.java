package com.gregswebserver.catan.common.game.scoring.reporting;

import com.gregswebserver.catan.common.game.scoring.rules.GameRules;

/**
 * Created by greg on 5/27/16.
 * An object that can generate a score report based on a set of rules.
 */
public interface Scorable {

    ScoreReport score(GameRules rules);
}
