package catan.common.game.scoring.rules;

import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.scoring.achievement.AchievementCard;
import catan.common.resources.PropertiesFile;
import catan.common.resources.PropertiesFileInfo;
import catan.common.resources.ResourceLoader;

import java.io.Serializable;

/**
 * Created by greg on 1/24/16.
 * A set of rules regarding playing the game and determining win conditions.
 */
public class StaticGameRules implements Serializable, GameRules {

    private final int settlementResources;
    private final int cityResources;
    private final int pathPoints;
    private final int settlementPoints;
    private final int cityPoints;
    private final int armyPoints;
    private final int roadPoints;
    private final int marketPoints;
    private final int parliamentPoints;
    private final int universityPoints;
    private final int chapelPoints;
    private final int libraryPoints;
    private final int minimumPoints;
    private final int leadPoints;
    private final int soldierCount;
    private final int marketCount;
    private final int parliamentCount;
    private final int universityCount;
    private final int chapelCount;
    private final int libraryCount;
    private final int monopolyCount;
    private final int roadbuildingCount;
    private final int plentyCount;
    private final int maxCards;
    private final int maxPaths;
    private final int maxSettlements;
    private final int maxCities;
    private final int armyMinimum;
    private final int roadMinimum;

    public StaticGameRules(String path) {
        PropertiesFile file = ResourceLoader.getPropertiesFile(new PropertiesFileInfo(path,"Game Rules"));
        settlementResources = file.getInt("rules.resources.settlement");
        cityResources = file.getInt("rules.resources.city");
        pathPoints = file.getInt("rules.points.build.path");
        settlementPoints = file.getInt("rules.points.build.settlement");
        cityPoints = file.getInt("rules.points.build.city");
        armyPoints = file.getInt("rules.points.achievement.army");
        roadPoints = file.getInt("rules.points.achievement.road");
        armyMinimum = file.getInt("rules.min.achievement.army");
        roadMinimum = file.getInt("rules.min.achievement.road");
        marketPoints = file.getInt("rules.points.card.market");
        parliamentPoints = file.getInt("rules.points.card.parliament");
        universityPoints = file.getInt("rules.points.card.university");
        chapelPoints = file.getInt("rules.points.card.chapel");
        libraryPoints = file.getInt("rules.points.card.library");
        minimumPoints = file.getInt("rules.points.win.minimum");
        leadPoints = file.getInt("rules.points.win.lead");
        soldierCount = file.getInt("rules.cards.soldier");
        marketCount = file.getInt("rules.cards.market");
        parliamentCount = file.getInt("rules.cards.parliament");
        universityCount = file.getInt("rules.cards.university");
        chapelCount = file.getInt("rules.cards.chapel");
        libraryCount = file.getInt("rules.cards.library");
        monopolyCount = file.getInt("rules.cards.monopoly");
        roadbuildingCount = file.getInt("rules.cards.roadbuilding");
        plentyCount = file.getInt("rules.cards.plenty");
        maxCards = file.getInt("rules.max.cards");
        maxPaths = file.getInt("rules.max.paths");
        maxSettlements = file.getInt("rules.max.settlements");
        maxCities = file.getInt("rules.max.cities");
    }

    @Override
    public int getSettlementResources() {
        return settlementResources;
    }

    @Override
    public int getCityResources() {
        return cityResources;
    }

    @Override
    public int getPathPoints() {
        return pathPoints;
    }

    @Override
    public int getSettlementPoints() {
        return settlementPoints;
    }

    @Override
    public int getCityPoints() {
        return cityPoints;
    }

    @Override
    public int getAchievementPoints(AchievementCard card) {
        switch (card) {
            case LargestArmy:
                return armyPoints;
            case LongestRoad:
                return roadPoints;
            default:
                return 0;
        }
    }

    @Override
    public int getAchievementMinimum(AchievementCard card) {
        switch (card) {
            case LargestArmy:
                return armyMinimum;
            case LongestRoad:
                return roadMinimum;
            default:
                return 0;
        }
    }

    @Override
    public int getDevelopmentCardPoints(DevelopmentCard card) {
        switch (card) {
            case Market:
                return marketPoints;
            case Parliament:
                return parliamentPoints;
            case University:
                return universityPoints;
            case Chapel:
                return chapelPoints;
            case Library:
                return libraryPoints;
            default:
                return 0;
        }
    }

    @Override
    public int getDevelopmentCardCount(DevelopmentCard card) {
        switch (card) {
            case Knight:
                return soldierCount;
            case Market:
                return marketCount;
            case Parliament:
                return parliamentCount;
            case University:
                return universityCount;
            case Chapel:
                return chapelCount;
            case Library:
                return libraryCount;
            case Monopoly:
                return monopolyCount;
            case YearOfPlenty:
                return plentyCount;
            case RoadBuilding:
                return roadbuildingCount;
            default:
                return 0;
        }
    }

    @Override
    public int getMinimumPoints() {
        return minimumPoints;
    }

    @Override
    public int getLeadPoints() {
        return leadPoints;
    }

    @Override
    public int getMaxCards() {
        return maxCards;
    }

    @Override
    public int getMaxPaths() {
        return maxPaths;
    }

    @Override
    public int getMaxSettlements() {
        return maxSettlements;
    }

    @Override
    public int getMaxCities() {
        return maxCities;
    }

    @Override
    public String toString() {
        return "StaticGameRules";
    }

    public boolean equals(Object o) {
        return this == o || (o != null && this.getClass() == o.getClass());
    }

    public int hashCode() {
        return 0;
    }
}
