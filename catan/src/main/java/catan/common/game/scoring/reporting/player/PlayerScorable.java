package catan.common.game.scoring.reporting.player;

import catan.common.game.scoring.reporting.ScoreException;
import catan.common.game.scoring.rules.GameRules;

/**
 * Created by greg on 5/27/16.
 * An object that can generate a PlayerScoreReport
 */
@FunctionalInterface
public interface PlayerScorable {

    PlayerScoreReport score(GameRules rules) throws ScoreException;
}
