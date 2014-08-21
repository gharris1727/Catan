package com.gregswebserver.catan.game.player;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.util.Statics;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the colored sprites for each player's team.
 */
public enum Team {

    Red(
            new Graphic[]{Statics.redSettlementRight, Statics.redSettlementLeft},
            new Graphic[]{Statics.redCityRight, Statics.redCityLeft},
            new Graphic[]{Statics.redDiagonalUpPath, Statics.redDiagonalDownPath, Statics.redHorizontalPath}),
    Orange(
            new Graphic[]{Statics.orangeSettlementRight, Statics.orangeSettlementLeft},
            new Graphic[]{Statics.orangeCityRight, Statics.orangeCityLeft},
            new Graphic[]{Statics.orangeDiagonalUpPath, Statics.orangeDiagonalDownPath, Statics.orangeHorizontalPath}),
    Blue(
            new Graphic[]{Statics.blueSettlementRight, Statics.blueSettlementLeft},
            new Graphic[]{Statics.blueCityRight, Statics.blueCityLeft},
            new Graphic[]{Statics.blueDiagonalUpPath, Statics.blueDiagonalDownPath, Statics.blueHorizontalPath}),
    White(
            new Graphic[]{Statics.whiteSettlementRight, Statics.whiteSettlementLeft},
            new Graphic[]{Statics.whiteCityRight, Statics.whiteCityLeft},
            new Graphic[]{Statics.whiteDiagonalUpPath, Statics.whiteDiagonalDownPath, Statics.whiteHorizontalPath}),
    Ocean(
            new Graphic[]{Statics.oceanVertexRight, Statics.oceanVertexLeft},
            new Graphic[]{Statics.oceanVertexRight, Statics.oceanVertexLeft},
            new Graphic[]{Statics.oceanDiagonalUpPath, Statics.oceanDiagonalDownPath, Statics.oceanHorizontalPath}),
    Blank(
            new Graphic[]{Statics.blankVertexRight, Statics.blankVertexLeft},
            new Graphic[]{Statics.blankVertexRight, Statics.blankVertexLeft},
            new Graphic[]{Statics.blankDiagonalUpPath, Statics.blankDiagonalDownPath, Statics.blankHorizontalPath});

    public final Graphic[] settlement;
    public final Graphic[] city;
    public final Graphic[] paths;

    Team(Graphic[] settlement, Graphic[] city, Graphic[] paths) {
        this.settlement = settlement;
        this.city = city;
        this.paths = paths;
    }
}
