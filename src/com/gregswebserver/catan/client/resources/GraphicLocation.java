package com.gregswebserver.catan.client.resources;

import java.awt.*;

/**
 * Created by Greg on 1/7/2015.
 * Locations used to render Graphic objects from GraphicSource objects.
 */
public enum GraphicLocation {

    Root(new Point()),

    TileHillLocation(new Point(235, 523)),
    TileForestLocation(new Point(586, 321)),
    TilePastureLocation(new Point(484, 551)),
    TileMountainLocation(new Point(248, 104)),
    TileFieldLocation(new Point(476, 116)),
    TileDesertLocation(new Point(131, 284)),

    SettlementLocation(Root),
    CityLocation(Root),
    HorizontalLocation(Root),
    DiagonalUpLocation(Root),
    DiagonalDownLocation(Root),

    DiceTwoLocation(Root),
    DiceThreeLocation(Root),
    DiceFourLocation(Root),
    DiceFiveLocation(Root),
    DiceSixLocation(Root),
    DiceSevenLocation(Root),
    DiceEightLocation(Root),
    DiceNineLocation(Root),
    DiceTenLocation(Root),
    DiceElevenLocation(Root),
    DiceTwelveLocation(Root),

    BeachSingleUpLocation(Root),
    BeachSingleUpRightLocation(Root),
    BeachSingleDownRightLocation(Root),
    BeachSingleDownLocation(Root),
    BeachSingleDownLeftLocation(Root),
    BeachSingleUpLeftLocation(Root),

    BeachDoubleUpRightLocation(Root),
    BeachDoubleRightLocation(Root),
    BeachDoubleDownRightLocation(Root),
    BeachDoubleDownLeftLocation(Root),
    BeachDoubleLeftLocation(Root),
    BeachDoubleUpLeftLocation(Root),

    OceanBackgroundLocation(Root),

    TradeUpRightLocation(Root),
    TradeRightLocation(Root),
    TradeDownRightLocation(Root),
    TradeDownLeftLocation(Root),
    TradeLeftLocation(Root),
    TradeUpLeftLocation(Root),

    TradeRatioTwoLocation(Root),
    TradeRatioThreeLocation(Root),

    TradeIconBrickLocation(Root),
    TradeIconLumberLocation(Root),
    TradeIconWoolLocation(Root),
    TradeIconGrainLocation(Root),
    TradeIconOreLocation(Root),
    TradeIconWildcardLocation(Root),

    ResourceBrickLocation(Root),
    ResourceLumberLocation(Root),
    ResourceWoolLocation(Root),
    ResourceGrainLocation(Root),
    ResourceOreLocation(Root),

    DevelopmentKnightLocation(Root),
    DevelopmentVictoryPointLocation(Root),
    DevelopmentMonopolyLocation(Root),
    DevelopmentYearOfPlentyLocation(Root),
    DevelopmentRoadBuildingLocation(Root),

    AchievementRoadLocation(Root),
    AchievementArmyLocation(Root),

    RobberLocation(Root);

    private final Point location;

    GraphicLocation(Point location) {
        this.location = location;
    }

    GraphicLocation(GraphicLocation other) {
        this.location = other.location;
    }

    public Point getLocation() {
        return location;
    }
}
