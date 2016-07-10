package catan.common.game.scoring.rules;

import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.scoring.achievement.AchievementCard;

/**
 * Created by greg on 5/27/16.
 * Set of rules that a scoring system uses to score the game.
 */
public interface GameRules {
    int getSettlementResources();

    int getCityResources();

    int getPathPoints();

    int getSettlementPoints();

    int getCityPoints();

    int getAchievementPoints(AchievementCard card);

    int getAchievementMinimum(AchievementCard card);

    int getDevelopmentCardPoints(DevelopmentCard card);

    int getDevelopmentCardCount(DevelopmentCard card);

    int getMinimumPoints();

    int getLeadPoints();

    int getMaxCards();

    int getMaxPaths();

    int getMaxSettlements();

    int getMaxCities();


}
