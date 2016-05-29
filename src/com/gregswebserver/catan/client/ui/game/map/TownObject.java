package com.gregswebserver.catan.client.ui.game.map;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.input.UserEvent;
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
public class TownObject extends MapObject {

    private final MapRegion container;
    private final Town town;

    TownObject(MapRegion container, Town town) {
        super(2, container);
        this.container = container;
        this.town = town;
        Graphic background;
        if (town instanceof Settlement || town instanceof EmptyTown) {
            background = container.getBuildingGraphics(town.getTeam()).getGraphic(3);
        } else if (town instanceof City) {
            background = container.getBuildingGraphics(town.getTeam()).getGraphic(4);
        } else
            throw new IllegalStateException();
        add(new GraphicObject(0, background) {
            @Override
            public String toString() {
                return "TownObjectBackground";
            }
        }).setClickable(this);
        setMask(background.getMask());
    }

    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        container.target(town);
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
