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

    public static final GameRulesEqualityTester INSTANCE = new GameRulesEqualityTester();

    private GameRulesEqualityTester() {

    }

    @Override
    public void assertEquals(GameRules a, GameRules b) {
        if (a == b)
            return;
        Assert.assertEquals(a.getSettlementResources(), b.getSettlementResources());
        Assert.assertEquals(a.getCityResources(), b.getCityResources());
        Assert.assertEquals(a.getPathPoints(), b.getPathPoints());
        Assert.assertEquals(a.getSettlementPoints(), b.getSettlementPoints());
        Assert.assertEquals(a.getCityPoints(), b.getCityPoints());

        for (AchievementCard ac : AchievementCard.values()) {
            Assert.assertEquals(a.getAchievementPoints(ac), b.getAchievementPoints(ac));
            Assert.assertEquals(a.getAchievementMinimum(ac), b.getAchievementMinimum(ac));
        }

        for (DevelopmentCard dc : DevelopmentCard.values()) {
            Assert.assertEquals(a.getDevelopmentCardPoints(dc), b.getDevelopmentCardPoints(dc));
            Assert.assertEquals(a.getDevelopmentCardCount(dc), b.getDevelopmentCardCount(dc));
        }

        Assert.assertEquals(a.getMinimumPoints(), b.getMinimumPoints());
        Assert.assertEquals(a.getLeadPoints(), b.getLeadPoints());
        Assert.assertEquals(a.getMaxCards(), b.getMaxCards());
        Assert.assertEquals(a.getMaxPaths(), b.getMaxPaths());
        Assert.assertEquals(a.getMaxSettlements(), b.getMaxSettlements());
        Assert.assertEquals(a.getMaxCities(), b.getMaxCities());
    }
}
