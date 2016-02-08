package com.gregswebserver.catan.client.ui.ingame.map;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.game.board.towns.City;
import com.gregswebserver.catan.common.game.board.towns.EmptyTown;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.board.towns.Town;

import java.awt.event.MouseEvent;

/**
 * Created by greg on 2/7/16.
 * Class representing the graphical version of a building.
 */
public class TownObject extends ScreenRegion {

    private final Town town;

    TownObject(int priority, Town town) {
        super(priority);
        this.town = town;
        Graphic background;
        if (town instanceof Settlement || town instanceof EmptyTown) {
            background = town.getTeam().getSettlementGraphic();
        } else if (town instanceof City) {
            background = town.getTeam().getCityGraphic();
        } else
            throw new IllegalStateException();
        add(new GraphicObject(0, background) {
            @Override
            public String toString() {
                return "TownObjectBackground";
            }
        });
        setMask(background.getMask());
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        return new UserEvent(this, UserEventType.Vertex_Clicked, town.getPosition());
    }

    @Override
    public String toString() {
        return null;
    }
}
