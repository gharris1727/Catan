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

    RobberLocation(Root),

    UIBlueBackgroundCenterLocation(new Point(0, 0)),
    UIBlueBackgroundUpLocation(new Point(32, 0)),
    UIBlueBackgroundDownLocation(new Point(64, 0)),
    UIBlueBackgroundLeftLocation(new Point(96, 0)),
    UIBlueBackgroundRightLocation(new Point(128, 0)),
    UIBlueBackgroundUpLeftLocation(new Point(160, 0)),
    UIBlueBackgroundUpRightLocation(new Point(192, 0)),
    UIBlueBackgroundDownLeftLocation(new Point(224, 0)),
    UIBlueBackgroundDownRightLocation(new Point(256, 0)),
    UIGreenBackgroundCenterLocation(new Point(0, 32)),
    UIGreenBackgroundUpLocation(new Point(32, 32)),
    UIGreenBackgroundDownLocation(new Point(64, 32)),
    UIGreenBackgroundLeftLocation(new Point(96, 32)),
    UIGreenBackgroundRightLocation(new Point(128, 32)),
    UIGreenBackgroundUpLeftLocation(new Point(160, 32)),
    UIGreenBackgroundUpRightLocation(new Point(192, 32)),
    UIGreenBackgroundDownLeftLocation(new Point(224, 32)),
    UIGreenBackgroundDownRightLocation(new Point(256, 32));

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
