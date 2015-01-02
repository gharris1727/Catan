package com.gregswebserver.catan.client.input.clickables;

import com.gregswebserver.catan.common.game.board.buildings.Building;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

/**
 * Created by Greg on 8/21/2014.
 * A clickable object with a reference to the coordinate for a space for a building.
 */
public class ClickableBuilding extends ClickableBoardObject {

    private final Building building;

    public ClickableBuilding(Coordinate position, Clickable mapDrag, Building building) {
        super(position, mapDrag);
        this.building = building;
    }

    public String toString() {
        return super.toString() + " Building: " + building;
    }

}
