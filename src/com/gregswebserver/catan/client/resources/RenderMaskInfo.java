package com.gregswebserver.catan.client.resources;

import com.gregswebserver.catan.client.graphics.masks.*;
import com.gregswebserver.catan.common.resources.GraphicsConfig;

import java.awt.*;

/**
 * Created by Greg on 1/6/2015.
 * A set of cached RenderMasks.
 */
public enum RenderMaskInfo {

    TileMask(new HexagonalMask(new Dimension(112, 96))),
    VertexSettlementMask(new RectangularMask(new Dimension(32, 32))),
    VertexCityMask(new RectangularMask(new Dimension(32, 64))),
    EdgeHorizontalMask(new RectangularMask(new Dimension(64, 16))),
    EdgeDiagonalUpMask(new DiagonalMask(new Dimension(16, 54))),
    EdgeDiagonalDownMask(new FlippedMask(EdgeDiagonalUpMask.getMask(), FlippedMask.Direction.VERTICAL)),
    OceanBackgroundMask(new RectangularMask(new Dimension(32, 32))),

    TradeHorizontalMask(new RectangularMask(new Dimension(32, 16))),
    TradeDiagonalUpMask(new DiagonalMask(new Dimension(64, 16))),
    TradeDiagonalDownMask(new FlippedMask(TradeDiagonalUpMask.getMask(), FlippedMask.Direction.VERTICAL)),

    ResourceCardMask(new RoundedRectangularMask(new Dimension(128, 192))),
    ResourceIconMask(new RectangularMask(new Dimension(16, 16))),
    AchievementCardMask(new RectangularMask(new Dimension(192, 192))),
    DiceRollMask(new RoundedMask(new Dimension(32, 32))),
    TradeRatioMask(new RectangularMask(new Dimension(32, 16))),
    RobberMask(new RectangularMask(new Dimension(32, 96))),

    UILargeMask(new RectangularMask(GraphicsConfig.largeTileSize)),
    UISmallMask(new RectangularMask(GraphicsConfig.smallTileSize)),
    UILobbyArrowMask(new RectangularMask(GraphicsConfig.lobbyArrowSize));

    public static RenderMaskInfo[] tradeBridgeMasks = new RenderMaskInfo[]{null, null, null, TradeHorizontalMask,
            TradeHorizontalMask, TradeDiagonalDownMask, TradeDiagonalDownMask, TradeDiagonalUpMask, TradeDiagonalUpMask};
    public static RenderMaskInfo[] teamBuildingMasks = new RenderMaskInfo[]{EdgeHorizontalMask, EdgeDiagonalUpMask,
            EdgeDiagonalDownMask, VertexSettlementMask, VertexCityMask, RobberMask};

    private final RenderMask mask;

    RenderMaskInfo(RenderMask mask) {
        this.mask = mask;
    }

    public RenderMask getMask() {
        return mask;
    }

}
