package com.gregswebserver.catan.common.game.scoring.rules;

import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.scoring.achievement.AchievementCard;
import com.gregswebserver.catan.test.common.game.AssertEqualsTestable;
import com.gregswebserver.catan.test.common.game.EqualityException;

/**
 * Created by greg on 5/27/16.
 * Set of rules that a scoring system uses to score the game.
 */
public interface GameRules extends AssertEqualsTestable<GameRules>{
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

    @Override
    default void assertEquals(GameRules other) throws EqualityException {
        if (other == this)
            return;
        if (getSettlementResources() != other.getSettlementResources())
            throw new EqualityException("RulesSettlementResources", getSettlementResources(), other.getSettlementResources());
        if (getCityResources() != other.getCityResources())
            throw new EqualityException("RulesCityResources", getCityResources(), other.getCityResources());
        if (getPathPoints() != other.getPathPoints())
            throw new EqualityException("RulesPathPoints", getPathPoints(), other.getPathPoints());
        if (getSettlementPoints() != other.getSettlementPoints())
            throw new EqualityException("RulesSettlementPoints", getSettlementPoints(), other.getSettlementPoints());
        if (getCityPoints() != other.getCityPoints())
            throw new EqualityException("RulesCityPoints", getCityPoints(), other.getCityPoints());

        for (AchievementCard ac : AchievementCard.values()) {
            if (getAchievementPoints(ac) != other.getAchievementPoints(ac))
                throw new EqualityException("RulesCityPoints", getAchievementPoints(ac), other.getAchievementPoints(ac));
            if (getAchievementMinimum(ac) != other.getAchievementMinimum(ac))
                throw new EqualityException("RulesCityPoints", getAchievementMinimum(ac), other.getAchievementMinimum(ac));
        }

        for (DevelopmentCard dc : DevelopmentCard.values()) {
            if (getDevelopmentCardPoints(dc) != other.getDevelopmentCardPoints(dc))
                throw new EqualityException("RulesCityPoints", getDevelopmentCardPoints(dc), other.getDevelopmentCardPoints(dc));
            if (getDevelopmentCardCount(dc) != other.getDevelopmentCardCount(dc))
                throw new EqualityException("RulesCityPoints", getDevelopmentCardCount(dc), other.getDevelopmentCardCount(dc));
        }

        if (getMinimumPoints() != other.getMinimumPoints())
            throw new EqualityException("RulesMinimumPoints", getMinimumPoints(), other.getMinimumPoints());
        if (getLeadPoints() != other.getLeadPoints())
            throw new EqualityException("RulesLeadPoints", getLeadPoints(), other.getLeadPoints());
        if (getMaxCards() != other.getMaxCards())
            throw new EqualityException("RulesMaxCards", getMaxCards(), other.getMaxCards());
        if (getMaxPaths() != other.getMaxPaths())
            throw new EqualityException("RulesMaxPaths", getMaxPaths(), other.getMaxPaths());
        if (getMaxSettlements() != other.getMaxSettlements())
            throw new EqualityException("RulesMaxSettlements", getMaxSettlements(), other.getMaxSettlements());
        if (getMaxCities() != other.getMaxCities())
            throw new EqualityException("RulesMaxCities", getMaxCities(), other.getMaxCities());
    }
}
