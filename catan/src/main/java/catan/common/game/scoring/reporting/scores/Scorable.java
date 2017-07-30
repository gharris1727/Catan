package catan.common.game.scoring.reporting.scores;

import catan.common.game.scoring.reporting.ScoreException;
import catan.common.game.scoring.rules.GameRules;

/**
 * Created by greg on 5/27/16.
 * An object that can generate a score report based on a set of rules.
 */
@FunctionalInterface
public interface Scorable {

    ScoreReport score(GameRules rules) throws ScoreException;
}
