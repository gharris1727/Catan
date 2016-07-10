package catan.common.resources;

import catan.client.graphics.graphics.Graphic;
import catan.client.graphics.masks.RenderMask;
import catan.client.renderer.NotYetRenderableException;
import catan.common.config.ConfigSource;

import java.awt.*;
import java.util.Arrays;


/**
 * Created by Greg on 1/16/2015.
 * A set of graphics that can be tiled to create a coherent shape.
 */
public class GraphicSet {

    private final RenderMask[] masks;
    private final GraphicInfo[] graphics;

    public GraphicSet(ConfigSource definition, String configKey, int[] swaps) {
        ConfigSource config = definition.narrow(configKey);
        GraphicSourceInfo source = new GraphicSourceInfo(config.get("path"));
        RenderMask mask = RenderMask.parseMask(config.narrow("mask"));
        int num = config.getInt("count");
        masks = new RenderMask[num];
        Arrays.fill(masks, mask);
        graphics = new GraphicInfo[num];
        int width = mask.getWidth();
        for (int i = 0; i < graphics.length; i++) {
            graphics[i] = new GraphicInfo(source, mask, new Point(i * width, 0), swaps);
        }
    }

    public GraphicSet(GraphicSourceInfo source, RenderMask[] masks, int[] swaps) {
        this.masks = masks;
        graphics = new GraphicInfo[masks.length];
        int width = 0;
        for (int i = 0; i < graphics.length; i++) {
            if (masks[i] != null) {
                graphics[i] = new GraphicInfo(source, masks[i], new Point(width, 0), swaps);
                width += masks[i].getWidth();
            }
        }
    }

    public Graphic getGraphic(Enum e) {
        return getGraphic(e.ordinal());
    }

    public Graphic getGraphic(int index) {
        try {
            if (index < 0 || index >= graphics.length)
                throw new IllegalArgumentException("Unable to provide selected graphic: Out of range");
            return ResourceLoader.getGraphic(graphics[index]);
        } catch (ResourceLoadException e) {
            throw new NotYetRenderableException("Unable to load external graphic.", e);
        }
    }

    public RenderMask getMask() {
        return getMask(0);
    }

    public RenderMask getMask(int ordinal) {
        if (ordinal < 0 || ordinal >= masks.length)
            throw new IllegalArgumentException("Unable to provide selected graphic: Out of range");
        return masks[ordinal];
    }
}
