package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Greg on 1/6/2015.
 * Details needed by the ResourceLoader in order to load a Graphic object.
 */
public class GraphicInfo {

    private final GraphicSourceInfo source;
    private final RenderMask mask;
    private final Point location;
    private final int[] swaps;

    public GraphicInfo(GraphicSourceInfo source, RenderMask mask, Point location, int[] swaps) {
        this.source = source;
        this.mask = mask;
        this.location = location;
        this.swaps = swaps;
    }

    public GraphicSourceInfo getSource() {
        return source;
    }

    public RenderMask getMask() {
        return mask;
    }

    public Point getLocation() {
        return location;
    }

    public int[] getSwaps(){
        return swaps;
    }

    public String toString() {
        return "GraphicInfo(" + source + "/" + mask + "/" + location + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraphicInfo)) return false;

        GraphicInfo that = (GraphicInfo) o;

        if (!source.equals(that.source)) return false;
        if (!mask.equals(that.mask)) return false;
        if (!location.equals(that.location)) return false;
        return Arrays.equals(swaps, that.swaps);

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + mask.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + Arrays.hashCode(swaps);
        return result;
    }
}
