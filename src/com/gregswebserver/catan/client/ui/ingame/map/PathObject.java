package com.gregswebserver.catan.client.ui.ingame.map;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.common.game.board.paths.Path;

import java.awt.event.MouseEvent;

/**
 * Created by greg on 2/7/16.
 * A graphical renderer object for displaying paths.
 */
public class PathObject extends ScreenRegion {

    private final Path path;

    protected PathObject(int priority, Path path) {
        super(priority);
        this.path = path;
        int orientation = (path.getPosition().x + 1) % 3;
        Graphic background = path.getTeam().getRoadGraphic(orientation);
        add(new GraphicObject(0, background) {
            @Override
            public String toString() {
                return "PathBackground";
            }
        });
        setMask(background.getMask());
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        return new UserEvent(this, UserEventType.Edge_Clicked, path.getPosition());
    }

    @Override
    public String toString() {
        return null;
    }
}
