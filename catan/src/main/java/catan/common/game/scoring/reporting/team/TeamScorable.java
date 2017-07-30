package catan.common.game.scoring.reporting.team;

import catan.common.game.scoring.reporting.ScoreException;
import catan.common.game.scoring.rules.GameRules;

/**
 * Created by greg on 5/27/16.
 * An object that can generate a TeamScoreReport.
 */
@FunctionalInterface
public interface TeamScorable {

    TeamScoreReport score(GameRules rules) throws ScoreException;
}
