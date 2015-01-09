package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.resources.GraphicInfo;
import com.gregswebserver.catan.client.resources.RenderMasks;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Resource;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;
import com.gregswebserver.catan.common.resources.GraphicsConfig;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.awt.*;

import static com.gregswebserver.catan.client.resources.GraphicInfo.*;

/**
 * Created by Greg on 8/22/2014.
 * A tile for producing resources on the map.
 */
public class ResourceTile extends Tile {

    private final static GraphicInfo[] tileGraphics = new GraphicInfo[]{
            TileHill, TileForest, TilePasture, TileMountain, TileField, TileDesert
    };
    private final static GraphicInfo[] diceGraphics = new GraphicInfo[]{
            DiceTwo, DiceThree, DiceFour, DiceFive, DiceSix, DiceSeven, DiceEight, DiceNine, DiceTen, DiceEleven, DiceTwelve
    };


    private DiceRoll diceRoll;
    private Terrain terrain;
    private Graphic graphic;
    private boolean robber;

    public ResourceTile(Terrain terrain, DiceRoll diceRoll) {
        this.terrain = terrain;
        this.diceRoll = diceRoll;
    }

    public DiceRoll getDiceRoll() {
        return diceRoll;
    }

    public void placeRobber() {
        robber = true;
        graphic = null;
    }

    public void removeRobber() {
        robber = false;
        graphic = null;
    }

    public boolean hasRobber() {
        return robber;
    }

    public Resource getResource() {
        if (robber || terrain == null) return null;
        return terrain.resource;
    }

    public String toString() {
        return "ResourceTile " + terrain;
    }

    public Graphic getGraphic() {
        if (graphic == null) {
            graphic = new Graphic(RenderMasks.TileMask.getMask());
            Graphic t = ResourceLoader.getGraphic(tileGraphics[terrain.ordinal()]);
            t.renderTo(graphic, new Point(), 0);
            Graphic d = ResourceLoader.getGraphic(diceGraphics[diceRoll.ordinal() - 2]);
            d.renderTo(graphic, GraphicsConfig.diceRollRender, 0);
            if (robber) {
                Graphic r = ResourceLoader.getGraphic(Robber);
                r.renderTo(graphic, GraphicsConfig.robberRender, 0);
            }
        }
        return graphic;
    }
}
