package catan.common.resources;

import catan.client.graphics.masks.RenderMask;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Greg on 1/6/2015.
 * Details needed by the ResourceLoader in order to load a Graphic object.
 */
public final class GraphicInfo extends ResourceCacheKey {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphicInfo that = (GraphicInfo) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (mask != null ? !mask.equals(that.mask) : that.mask != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        return Arrays.equals(swaps, that.swaps);
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (mask != null ? mask.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(swaps);
        return result;
    }

    public String toString() {
        return "GraphicInfo(" + source + "/" + mask + "/" + location + (swaps != null ? "/" + swaps.length : "")+ ")";
    }
}
