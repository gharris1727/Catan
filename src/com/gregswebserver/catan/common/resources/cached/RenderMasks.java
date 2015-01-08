package com.gregswebserver.catan.common.resources.cached;

import com.gregswebserver.catan.client.graphics.masks.*;

import java.awt.*;

/**
 * Created by Greg on 1/6/2015.
 * A set of cached RenderMasks.
 */
public enum RenderMasks {

    TileMask(new HexagonalMask(new Dimension(112, 96))),
    OceanBackgroundMask(new RectangularMask(new Dimension(32, 32))),
    VertexSettlementMask(new RectangularMask(new Dimension(32, 32))),
    VertexCityMask(new RectangularMask(new Dimension(32, 64))),
    EdgeHorizontalMask(new RectangularMask(new Dimension(64, 16))),
    EdgeDiagonalUpMask(new DiagonalMask(new Dimension(16, 54))),
    EdgeDiagonalDownMask(new FlippedMask(EdgeDiagonalUpMask.getMask(), FlippedMask.Direction.VERTICAL)),

    ResourceCardMask(new RoundedRectangularMask(new Dimension(128, 192))),
    AchievementCardMask(new RectangularMask(new Dimension(192, 192))),
    DiceRollMask(new RoundedMask(new Dimension(32, 32))),
    TradeRatioMask(new RectangularMask(new Dimension(32, 16))),
    ResourceIconMask(new RectangularMask(new Dimension(16, 16)));

    private final RenderMask mask;

    RenderMasks(RenderMask mask) {
        this.mask = mask;
    }

    public RenderMask getMask() {
        return mask;
    }

}
