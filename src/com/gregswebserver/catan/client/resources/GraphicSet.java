package com.gregswebserver.catan.client.resources;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.common.game.gameplay.enums.*;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;
import java.util.Arrays;

import static com.gregswebserver.catan.client.resources.RenderMaskInfo.*;


/**
 * Created by Greg on 1/16/2015.
 * A set of graphics that can be tiled to create a coherent shape.
 */
public enum GraphicSet {

    Land(GraphicSourceInfo.Land, TileMask, Terrain.values().length),
    BeachSingle(GraphicSourceInfo.BeachSingle, TileMask, Direction.values().length),
    BeachDouble(GraphicSourceInfo.BeachDouble, TileMask, Direction.values().length),
    Ocean(GraphicSourceInfo.Ocean, UILargeMask, 16),

    Dice(GraphicSourceInfo.Dice, DiceRollMask, DiceRoll.values().length),
    ResourceCard(GraphicSourceInfo.ResourceCard, ResourceCardMask, GameResource.values().length),
    Development(GraphicSourceInfo.Development, ResourceCardMask, DevelopmentCard.values().length),
    Achievement(GraphicSourceInfo.Achievement, AchievementCardMask, AchievementCard.values().length),
    TradeBridges(GraphicSourceInfo.TradeBridges, RenderMaskInfo.tradeBridgeMasks),
    TradeRatio(GraphicSourceInfo.TradeRatios, TradeRatioMask, 2),
    TradeIcons(GraphicSourceInfo.TradeIcons, ResourceIconMask, GameResource.values().length),

    TeamEmpty(GraphicSourceInfo.EmptyTeam, RenderMaskInfo.teamBuildingMasks),
    TeamRed(GraphicSourceInfo.RedTeam, RenderMaskInfo.teamBuildingMasks),
    TeamOrange(GraphicSourceInfo.OrangeTeam, RenderMaskInfo.teamBuildingMasks),
    TeamBlue(GraphicSourceInfo.BlueTeam, RenderMaskInfo.teamBuildingMasks),
    TeamWhite(GraphicSourceInfo.WhiteTeam, RenderMaskInfo.teamBuildingMasks),

    UIBlueBackground(GraphicSourceInfo.BlueUIBackground, UILargeMask, Direction.values().length),
    UIBlueWindow(GraphicSourceInfo.BlueUIWindow, UISmallMask, Direction.values().length),
    UIBlueText(GraphicSourceInfo.BlueUIText, UISmallMask, Direction.values().length),
    UIBlueButton(GraphicSourceInfo.BlueUIButton, UISmallMask, Direction.values().length),

    UIBlueLobbyIcons(GraphicSourceInfo.BlueUIArrows, UILobbyArrowMask, 6);

    private final RenderMaskInfo[] masks;
    private final GraphicInfo[] graphics;

    GraphicSet(GraphicSourceInfo source, RenderMaskInfo[] masks) {
        this.masks = masks;
        graphics = new GraphicInfo[masks.length];
        int width = 0;
        for (int i = 0; i < graphics.length; i++) {
            if (masks[i] != null) {
                graphics[i] = new GraphicInfo(source, masks[i], new Point(width, 0));
                width += masks[i].getMask().getWidth();
            }
        }
    }

    GraphicSet(GraphicSourceInfo source, RenderMaskInfo mask, int num) {
        masks = new RenderMaskInfo[num];
        Arrays.fill(masks, mask);
        graphics = new GraphicInfo[num];
        int width = mask.getMask().getWidth();
        for (int i = 0; i < graphics.length; i++) {
            graphics[i] = new GraphicInfo(source, mask, new Point(i * width, 0));
        }
    }

    public Graphic getGraphic(int ordinal) {
        if (ordinal < 0 || ordinal >= graphics.length)
            throw new IllegalArgumentException("Unable to provide selected graphic: Out of range");
        return ResourceLoader.getGraphic(graphics[ordinal]);
    }

    public RenderMask getMask() {
        return getMask(0);
    }

    public RenderMask getMask(int ordinal) {
        if (ordinal < 0 || ordinal >= masks.length)
            throw new IllegalArgumentException("Unable to provide selected graphic: Out of range");
        return masks[ordinal].getMask();
    }
}
