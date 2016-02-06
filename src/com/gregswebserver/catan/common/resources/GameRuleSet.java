package com.gregswebserver.catan.common.resources;

import java.io.IOException;

/**
 * Created by greg on 1/24/16.
 * A set of rules regarding playing the game and determining win conditions.
 */
public class GameRuleSet {

    private int pathPoints;
    private int settlementPoints;
    private int cityPoints;
    private int armyPoints;
    private int roadPoints;
    private int marketPoints;
    private int parliamentPoints;
    private int universityPoints;
    private int chapelPoints;
    private int libraryPoints;
    private int minimumPoints;
    private int leadPoints;
    private int soldierCount;
    private int marketCount;
    private int parliamentCount;
    private int universityCount;
    private int chapelCount;
    private int libraryCount;
    private int monopolyCount;
    private int roadbuildingCount;
    private int plentyCount;
    private int maxCards;
    private int maxPaths;
    private int maxSettlements;
    private int maxCities;

    public GameRuleSet(String path) throws IOException {
        PropertiesFile file = new PropertiesFile(path,"Board layout information");
        file.open();
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

    public int getPathPoints() {
        return pathPoints;
    }

    public int getSettlementPoints() {
        return settlementPoints;
    }

    public int getCityPoints() {
        return cityPoints;
    }

    public int getArmyPoints() {
        return armyPoints;
    }

    public int getRoadPoints() {
        return roadPoints;
    }

    public int getMarketPoints() {
        return marketPoints;
    }

    public int getParliamentPoints() {
        return parliamentPoints;
    }

    public int getUniversityPoints() {
        return universityPoints;
    }

    public int getChapelPoints() {
        return chapelPoints;
    }

    public int getLibraryPoints() {
        return libraryPoints;
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
}
