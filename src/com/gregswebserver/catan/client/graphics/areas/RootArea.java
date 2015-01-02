package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.input.clickables.Clickable;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Master screen area containing a grid of sub-areas for the different regions.
 */
public class RootArea extends GridScreenArea {

    private final static int sidebarWidth = 256;
    private final static int bottomHeight = 256;
    Point mapPosition = new Point(0, 0);
    private Logger logger;
    private CatanGame game;
    private MapArea map;

    public RootArea(Logger logger) {
        super(new Point(), 0, new RootClickable(logger));
        this.logger = logger;
    }

    public void resize(Dimension d) {
        int[] widths = new int[]{d.width - sidebarWidth, sidebarWidth};
        int[] heights = new int[]{d.height - bottomHeight, bottomHeight};
        super.resize(widths, heights);
        if (map != null)
            map.resize(getCellDimension(mapPosition));
    }

    protected void render() {
        clear();
        if (map != null) {
            add(map);
        }
    }

    public void setActiveGame(CatanGame game) {
        this.game = game;

        map = new MapArea(getCellDimension(mapPosition), mapPosition, 0, game.getBoard());
    }

    public void updateMap() {
        map.update();
    }

    private static class RootClickable implements Clickable {

        private Logger logger;

        private RootClickable(Logger logger) {
            this.logger = logger;
        }

        public void onMouseClick(int button) {
            logger.log(this + " Mouse clicked " + button, LogLevel.DEBUG);
        }

        public void onKeyTyped(int key) {
            logger.log(this + " Key typed " + key, LogLevel.DEBUG);
        }

        public void onMouseScroll(int wheelRotation) {
            logger.log(this + " Mouse scroll " + wheelRotation, LogLevel.DEBUG);
        }

        public String toString() {
            return "RootClickable";
        }
    }
}
