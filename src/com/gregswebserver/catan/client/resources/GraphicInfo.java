package com.gregswebserver.catan.client.resources;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;

import static com.gregswebserver.catan.client.resources.GraphicLocation.*;
import static com.gregswebserver.catan.client.resources.GraphicSourceInfo.*;
import static com.gregswebserver.catan.client.resources.RenderMasks.*;

/**
 * Created by Greg on 1/6/2015.
 * A list of graphics that can be loaded by the ResourceLoader.
 */
public enum GraphicInfo {

    Default(null, null, null),

    TileHill(Tiles, TileMask, TileHillLocation),
    TileForest(Tiles, TileMask, TileForestLocation),
    TilePasture(Tiles, TileMask, TilePastureLocation),
    TileMountain(Tiles, TileMask, TileMountainLocation),
    TileField(Tiles, TileMask, TileFieldLocation),
    TileDesert(Tiles, TileMask, TileDesertLocation),

    BeachSingleUp(Beach, TileMask, BeachSingleUpLocation),
    BeachSingleUpRight(Beach, TileMask, BeachSingleUpRightLocation),
    BeachSingleDownRight(Beach, TileMask, BeachSingleDownRightLocation),
    BeachSingleDown(Beach, TileMask, BeachSingleDownLocation),
    BeachSingleDownLeft(Beach, TileMask, BeachSingleDownLeftLocation),
    BeachSingleUpLeft(Beach, TileMask, BeachSingleUpLeftLocation),

    BeachDoubleUpRight(Beach, TileMask, BeachDoubleUpRightLocation),
    BeachDoubleRight(Beach, TileMask, BeachDoubleRightLocation),
    BeachDoubleDownRight(Beach, TileMask, BeachDoubleDownRightLocation),
    BeachDoubleDownLeft(Beach, TileMask, BeachDoubleDownLeftLocation),
    BeachDoubleLeft(Beach, TileMask, BeachDoubleLeftLocation),
    BeachDoubleUpLeft(Beach, TileMask, BeachDoubleUpLeftLocation),

    OceanBackground(Beach, OceanBackgroundMask, OceanBackgroundLocation),

    TradeUpRight(Trade, TradeDiagonalUpMask, TradeUpRightLocation),
    TradeRight(Trade, TradeHorizontalMask, TradeRightLocation),
    TradeDownRight(Trade, TradeDiagonalDownMask, TradeDownRightLocation),
    TradeDownLeft(Trade, TradeDiagonalUpMask, TradeDownLeftLocation),
    TradeLeft(Trade, TradeHorizontalMask, TradeLeftLocation),
    TradeUpLeft(Trade, TradeDiagonalDownMask, TradeUpLeftLocation),

    ResourceBrick(Resource, ResourceCardMask, ResourceBrickLocation),
    ResourceLumber(Resource, ResourceCardMask, ResourceLumberLocation),
    ResourceWool(Resource, ResourceCardMask, ResourceWoolLocation),
    ResourceGrain(Resource, ResourceCardMask, ResourceGrainLocation),
    ResourceOre(Resource, ResourceCardMask, ResourceOreLocation),

    IconBrick(Resource, ResourceIconMask, TradeIconBrickLocation),
    IconLumber(Resource, ResourceIconMask, TradeIconLumberLocation),
    IconWool(Resource, ResourceIconMask, TradeIconWoolLocation),
    IconGrain(Resource, ResourceIconMask, TradeIconGrainLocation),
    IconOre(Resource, ResourceIconMask, TradeIconOreLocation),
    IconWildcard(Resource, ResourceIconMask, TradeIconWildcardLocation),

    DevelopmentKnight(Development, ResourceCardMask, DevelopmentKnightLocation),
    DevelopmentVictoryPoint(Development, ResourceCardMask, DevelopmentVictoryPointLocation),
    DevelopmentMonopoly(Development, ResourceCardMask, DevelopmentMonopolyLocation),
    DevelopmentYearOfPlenty(Development, ResourceCardMask, DevelopmentYearOfPlentyLocation),
    DevelopmentRoadBuilding(Development, ResourceCardMask, DevelopmentRoadBuildingLocation),

    AchievementRoad(Achievement, AchievementCardMask, AchievementRoadLocation),
    AchievementArmy(Achievement, AchievementCardMask, AchievementArmyLocation),

    DiceTwo(Dice, DiceRollMask, DiceTwoLocation),
    DiceThree(Dice, DiceRollMask, DiceThreeLocation),
    DiceFour(Dice, DiceRollMask, DiceFourLocation),
    DiceFive(Dice, DiceRollMask, DiceFiveLocation),
    DiceSix(Dice, DiceRollMask, DiceSixLocation),
    DiceSeven(Dice, DiceRollMask, DiceSevenLocation),
    DiceEight(Dice, DiceRollMask, DiceEightLocation),
    DiceNine(Dice, DiceRollMask, DiceNineLocation),
    DiceTen(Dice, DiceRollMask, DiceTenLocation),
    DiceEleven(Dice, DiceRollMask, DiceElevenLocation),
    DiceTwelve(Dice, DiceRollMask, DiceTwelveLocation),

    TradeRatioTwo(Trade, TradeRatioMask, TradeRatioTwoLocation),
    TradeRatioThree(Trade, TradeRatioMask, TradeRatioThreeLocation),

    EmptySettlement(Empty, VertexSettlementMask, SettlementLocation),
    EmptyPathHorizontal(Empty, EdgeHorizontalMask, HorizontalLocation),
    EmptyPathDiagonalUp(Empty, EdgeDiagonalUpMask, DiagonalUpLocation),
    EmptyPathDiagonalDown(Empty, EdgeDiagonalDownMask, DiagonalDownLocation),

    Robber(Empty, RobberMask, RobberLocation),

    RedSettlement(Red, VertexSettlementMask, SettlementLocation),
    RedCity(Red, VertexCityMask, CityLocation),
    RedPathHorizontal(Red, EdgeHorizontalMask, HorizontalLocation),
    RedPathDiagonalUp(Red, EdgeDiagonalUpMask, DiagonalUpLocation),
    RedPathDiagonalDown(Red, EdgeDiagonalDownMask, DiagonalDownLocation),

    OrangeSettlement(Orange, VertexSettlementMask, SettlementLocation),
    OrangeCity(Orange, VertexCityMask, CityLocation),
    OrangePathHorizontal(Orange, EdgeHorizontalMask, HorizontalLocation),
    OrangePathDiagonalUp(Orange, EdgeDiagonalUpMask, DiagonalUpLocation),
    OrangePathDiagonalDown(Orange, EdgeDiagonalDownMask, DiagonalDownLocation),

    BlueSettlement(Blue, VertexSettlementMask, SettlementLocation),
    BlueCity(Blue, VertexCityMask, CityLocation),
    BluePathHorizontal(Blue, EdgeHorizontalMask, HorizontalLocation),
    BluePathDiagonalUp(Blue, EdgeDiagonalUpMask, DiagonalUpLocation),
    BluePathDiagonalDown(Blue, EdgeDiagonalDownMask, DiagonalDownLocation),

    WhiteSettlement(White, VertexSettlementMask, SettlementLocation),
    WhiteCity(White, VertexCityMask, CityLocation),
    WhitePathHorizontal(White, EdgeHorizontalMask, HorizontalLocation),
    WhitePathDiagonalUp(White, EdgeDiagonalUpMask, DiagonalUpLocation),
    WhitePathDiagonalDown(White, EdgeDiagonalDownMask, DiagonalDownLocation);

    private final GraphicSourceInfo source;
    private final RenderMasks mask;
    private final GraphicLocation location;

    GraphicInfo(GraphicSourceInfo source, RenderMasks mask, GraphicLocation location) {
        this.source = source;
        this.mask = mask;
        this.location = location;
    }

    public GraphicSourceInfo getSource() {
        return source;
    }

    public RenderMask getMask() {
        return mask.getMask();
    }

    public Point getLocation() {
        return location.getLocation();
    }
}
