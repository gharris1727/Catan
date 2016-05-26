package com.gregswebserver.catan.common.game.gameplay.rules;

import com.gregswebserver.catan.common.config.PropertiesFile;
import com.gregswebserver.catan.common.game.gameplay.achievement.AchievementCard;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.resources.PropertiesFileInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.io.Serializable;

/**
 * Created by greg on 1/24/16.
 * A set of rules regarding playing the game and determining win conditions.
 */
public class GameRules implements Serializable {

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

    public GameRules(String path) {
        PropertiesFile file = ResourceLoader.getPropertiesFile(new PropertiesFileInfo(path,"Game Rules"));
        settlementResources = file.getInt("rules.resources.settlement");
        cityResources = file.getInt("rules.resources.city");
        pathPoints = file.getInt("rules.points.build.path");
        settlementPoints = file.getInt("rules.points.build.settlement");
        cityPoints = file.getInt("rules.points.build.city");
        armyPoints = file.getInt("rules.points.achievement.army");
        roadPoints = file.getInt("rules.points.achievement.road");
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

    public int getSettlementResources() {
        return settlementResources;
    }

    public int getCityResources() {
        return cityResources;
    }

    public int getPathPoints() {
        return pathPoints;
    }

    public int getSettlementPoints() {
        return settlementPoints;
    }

    public int getCityPoints() {
        return cityPoints;
    }

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

    public int getMinimumPoints() {
        return minimumPoints;
    }

    public int getLeadPoints() {
        return leadPoints;
    }

    public int getSoldierCount() {
        return soldierCount;
    }

    public int getMarketCount() {
        return marketCount;
    }

    public int getParliamentCount() {
        return parliamentCount;
    }

    public int getUniversityCount() {
        return universityCount;
    }

    public int getChapelCount() {
        return chapelCount;
    }

    public int getLibraryCount() {
        return libraryCount;
    }

    public int getMonopolyCount() {
        return monopolyCount;
    }

    public int getRoadbuildingCount() {
        return roadbuildingCount;
    }

    public int getPlentyCount() {
        return plentyCount;
    }

    public int getMaxCards() {
        return maxCards;
    }

    public int getMaxPaths() {
        return maxPaths;
    }

    public int getMaxSettlements() {
        return maxSettlements;
    }

    public int getMaxCities() {
        return maxCities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameRules gameRules = (GameRules) o;

        if (settlementResources != gameRules.settlementResources) return false;
        if (cityResources != gameRules.cityResources) return false;
        if (pathPoints != gameRules.pathPoints) return false;
        if (settlementPoints != gameRules.settlementPoints) return false;
        if (cityPoints != gameRules.cityPoints) return false;
        if (armyPoints != gameRules.armyPoints) return false;
        if (roadPoints != gameRules.roadPoints) return false;
        if (marketPoints != gameRules.marketPoints) return false;
        if (parliamentPoints != gameRules.parliamentPoints) return false;
        if (universityPoints != gameRules.universityPoints) return false;
        if (chapelPoints != gameRules.chapelPoints) return false;
        if (libraryPoints != gameRules.libraryPoints) return false;
        if (minimumPoints != gameRules.minimumPoints) return false;
        if (leadPoints != gameRules.leadPoints) return false;
        if (soldierCount != gameRules.soldierCount) return false;
        if (marketCount != gameRules.marketCount) return false;
        if (parliamentCount != gameRules.parliamentCount) return false;
        if (universityCount != gameRules.universityCount) return false;
        if (chapelCount != gameRules.chapelCount) return false;
        if (libraryCount != gameRules.libraryCount) return false;
        if (monopolyCount != gameRules.monopolyCount) return false;
        if (roadbuildingCount != gameRules.roadbuildingCount) return false;
        if (plentyCount != gameRules.plentyCount) return false;
        if (maxCards != gameRules.maxCards) return false;
        if (maxPaths != gameRules.maxPaths) return false;
        if (maxSettlements != gameRules.maxSettlements) return false;
        return maxCities == gameRules.maxCities;

    }

    @Override
    public String toString() {
        return "GameRules";
    }
}
