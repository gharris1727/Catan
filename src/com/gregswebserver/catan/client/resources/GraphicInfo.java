package com.gregswebserver.catan.client.resources;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;

/**
 * Created by Greg on 1/6/2015.
 * Details needed by the ResourceLoader in order to load a Graphic object.
 */
public class GraphicInfo {

    private final GraphicSourceInfo source;
    private final RenderMaskInfo mask;
    private final Point location;

    public GraphicInfo(GraphicSourceInfo source, RenderMaskInfo mask, Point location) {
        this.source = source;
        this.mask = mask;
        this.location = location;
    }

    public GraphicSourceInfo getSource() {
        return source;
    }

    public RenderMask getMask() {
        return mask.getMask();
    }

    public Point getLocation() {
        return location;
    }

    public String toString() {
        return "GraphicInfo{" +
                "source=" + source +
                ", mask=" + mask +
                ", location=" + location +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphicInfo that = (GraphicInfo) o;

        if (!location.equals(that.location)) return false;
        if (mask != that.mask) return false;
        return source == that.source;

    }

    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + mask.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }
}
