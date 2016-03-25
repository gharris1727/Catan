package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.common.game.board.paths.Path;

import java.awt.event.MouseEvent;

/**
 * Created by greg on 2/7/16.
 * A graphical renderer object for displaying paths.
 */
public class PathObject extends MapObject {

    private final Path path;

    protected PathObject(MapRegion container, Path path) {
        super(1, container);
        this.path = path;
        int orientation = 0;
        if (path.getPosition().x % 6 == 0 || path.getPosition().x % 6 == 4) orientation = 1;
        if (path.getPosition().x % 6 == 1 || path.getPosition().x % 6 == 3) orientation = 2;
        Graphic background = container.getBuildingGraphics(path.getTeam()).getGraphic(orientation);
        add(new GraphicObject(0, background) {
            @Override
            public String toString() {
                return "PathBackground";
            }
        }).setClickable(this);
        setMask(background.getMask());
    }

    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        return new UserEvent(this, UserEventType.Edge_Clicked, path.getPosition());
    }

    @Override
    public String toString() {
        return "PathObject";
    }
}
