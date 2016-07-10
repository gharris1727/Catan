package catan.client.graphics.ui;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.GraphicObject;
import catan.common.util.Direction;

import java.awt.*;

/**
 * Created by Greg on 1/16/2015.
 * a TiledBackground that has additional edging shown.
 */
public class EdgedTiledBackground extends TiledBackground {

    public EdgedTiledBackground() {
    }

    public EdgedTiledBackground(String name, int priority, String configKey) {
        super(name, priority, configKey);
    }

    @Override
    protected void renderContents() {
        assertRenderable();
        RenderMask textureMask = graphics.getMask();
        int totWidth = getMask().getWidth();
        int totHeight = getMask().getHeight();
        int texHeight = textureMask.getHeight();
        int texWidth = textureMask.getWidth();
        int corWidth = totWidth - texWidth;
        int corHeight = totHeight - texHeight;
        //Four corners
        clear();
        addTile(new Point(), Direction.upleft);
        addTile(new Point(corWidth, 0), Direction.upright);
        addTile(new Point(0, corHeight), Direction.downleft);
        addTile(new Point(corWidth, corHeight), Direction.downright);
        //Top and bottom edges.
        for (int x = texWidth; x < corWidth; x += texWidth) {
            addTile(new Point(x, 0), Direction.up);
            addTile(new Point(x, corHeight), Direction.down);
        }
        //Left and right edges.
        for (int y = texHeight; y < corHeight; y += texHeight) {
            addTile(new Point(0, y), Direction.left);
            addTile(new Point(corWidth, y), Direction.right);
        }
        renderFillerTiles(totWidth, totHeight);
    }

    private void addTile(Point position, Direction d) {
        GraphicObject tile = new GraphicObject("Tile inside " + this, d.ordinal(), graphics.getGraphic(d.ordinal()));
        add(tile).setClickable(this).setPosition(position);
    }
}
