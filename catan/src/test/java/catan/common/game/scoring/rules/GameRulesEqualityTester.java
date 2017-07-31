package catan.common.game.scoring.rules;

import catan.common.game.EqualityTester;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.scoring.achievement.AchievementCard;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * Equality Tester for GameRules objects.
 */
public final class GameRulesEqualityTester implements EqualityTester<GameRules> {

    @Override
    public void assertEquals(GameRules expected, GameRules actual) {
        if (expected == actual)
            return;
        Assert.assertEquals(expected.getSettlementResources(), actual.getSettlementResources());
        Assert.assertEquals(expected.getCityResources(), actual.getCityResources());
        Assert.assertEquals(expected.getPathPoints(), actual.getPathPoints());
        Assert.assertEquals(expected.getSettlementPoints(), actual.getSettlementPoints());
        Assert.assertEquals(expected.getCityPoints(), actual.getCityPoints());

        for (AchievementCard ac : AchievementCard.values()) {
            Assert.assertEquals(expected.getAchievementPoints(ac), actual.getAchievementPoints(ac));
            Assert.assertEquals(expected.getAchievementMinimum(ac), actual.getAchievementMinimum(ac));
        }

        for (DevelopmentCard dc : DevelopmentCard.values()) {
            Assert.assertEquals(expected.getDevelopmentCardPoints(dc), actual.getDevelopmentCardPoints(dc));
            Assert.assertEquals(expected.getDevelopmentCardCount(dc), actual.getDevelopmentCardCount(dc));
        }

        Assert.assertEquals(expected.getMinimumPoints(), actual.getMinimumPoints());
        Assert.assertEquals(expected.getLeadPoints(), actual.getLeadPoints());
        Assert.assertEquals(expected.getMaxCards(), actual.getMaxCards());
        Assert.assertEquals(expected.getMaxPaths(), actual.getMaxPaths());
        Assert.assertEquals(expected.getMaxSettlements(), actual.getMaxSettlements());
        Assert.assertEquals(expected.getMaxCities(), actual.getMaxCities());
    }
}
