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

    UILargeCenterLocation(new Point(0, 0)),
    UILargeUpLocation(new Point(32, 0)),
    UILargeDownLocation(new Point(64, 0)),
    UILargeLeftLocation(new Point(96, 0)),
    UILargeRightLocation(new Point(128, 0)),
    UILargeUpLeftLocation(new Point(160, 0)),
    UILargeDownLeftLocation(new Point(192, 0)),
    UILargeUpRightLocation(new Point(224, 0)),
    UILargeDownRightLocation(new Point(256, 0)),

    UISmallReleasedCenterLocation(new Point(0, 56)),
    UISmallReleasedUpLocation(new Point(8, 56)),
    UISmallReleasedDownLocation(new Point(16, 56)),
    UISmallReleasedLeftLocation(new Point(24, 56)),
    UISmallReleasedRightLocation(new Point(32, 56)),
    UISmallReleasedUpLeftLocation(new Point(40, 56)),
    UISmallReleasedDownLeftLocation(new Point(48, 56)),
    UISmallReleasedUpRightLocation(new Point(56, 56)),
    UISmallReleasedDownRightLocation(new Point(64, 56)),

    UISmallCenterLocation(new Point(0, 32)),
    UISmallUpLocation(new Point(8, 32)),
    UISmallDownLocation(new Point(16, 32)),
    UISmallLeftLocation(new Point(24, 32)),
    UISmallRightLocation(new Point(32, 32)),
    UISmallUpLeftLocation(new Point(40, 32)),
    UISmallDownLeftLocation(new Point(48, 32)),
    UISmallUpRightLocation(new Point(56, 32)),
    UISmallDownRightLocation(new Point(64, 32)),

    UISmallPressingCenterLocation(new Point(0, 40)),
    UISmallPressingUpLocation(new Point(8, 40)),
    UISmallPressingDownLocation(new Point(16, 40)),
    UISmallPressingLeftLocation(new Point(24, 40)),
    UISmallPressingRightLocation(new Point(32, 40)),
    UISmallPressingUpLeftLocation(new Point(40, 40)),
    UISmallPressingDownLeftLocation(new Point(48, 40)),
    UISmallPressingUpRightLocation(new Point(56, 40)),
    UISmallPressingDownRightLocation(new Point(64, 40)),

    UISmallPressedCenterLocation(new Point(0, 48)),
    UISmallPressedUpLocation(new Point(8, 48)),
    UISmallPressedDownLocation(new Point(16, 48)),
    UISmallPressedLeftLocation(new Point(24, 48)),
    UISmallPressedRightLocation(new Point(32, 48)),
    UISmallPressedUpLeftLocation(new Point(40, 48)),
    UISmallPressedDownLeftLocation(new Point(48, 48)),
    UISmallPressedUpRightLocation(new Point(56, 48)),
    UISmallPressedDownRightLocation(new Point(64, 48));

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
