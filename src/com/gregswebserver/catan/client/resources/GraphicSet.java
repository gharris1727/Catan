package com.gregswebserver.catan.client.resources;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;
import com.gregswebserver.catan.common.resources.ConfigurationException;
import com.gregswebserver.catan.common.resources.ResourceLoadException;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.sun.istack.internal.NotNull;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


/**
 * Created by Greg on 1/16/2015.
 * A set of graphics that can be tiled to create a coherent shape.
 */
public class GraphicSet {

    private final RenderMask[] masks;
    private final GraphicInfo[] graphics;

    public GraphicSet(String configKey, Class<? extends RenderMask> maskClass) {
        GraphicSourceInfo source = new GraphicSourceInfo(Main.staticConfig.get(configKey + ".path"));
        RenderMask mask;
        try {
            Constructor constructor = maskClass.getConstructor(Dimension.class);
            mask = (RenderMask) constructor.newInstance(Main.staticConfig.getDimension(configKey + ".size"));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ConfigurationException("Unable to construct mask from single dimension", e);
        }
        int num = Main.staticConfig.getInt(configKey + ".count");
        masks = new RenderMask[num];
        Arrays.fill(masks, mask);
        graphics = new GraphicInfo[num];
        int width = mask.getWidth();
        for (int i = 0; i < graphics.length; i++) {
            graphics[i] = new GraphicInfo(source, mask, new Point(i * width, 0));
        }
    }

    public GraphicSet(GraphicSourceInfo source, RenderMask[] masks) {
        this.masks = masks;
        graphics = new GraphicInfo[masks.length];
        int width = 0;
        for (int i = 0; i < graphics.length; i++) {
            if (masks[i] != null) {
                graphics[i] = new GraphicInfo(source, masks[i], new Point(width, 0));
                width += masks[i].getWidth();
            }
        }
    }

    @NotNull
    public Graphic getGraphic(int ordinal) {
        try {
            if (ordinal < 0 || ordinal >= graphics.length)
                throw new IllegalArgumentException("Unable to provide selected graphic: Out of range");
            return ResourceLoader.getGraphic(graphics[ordinal]);
        } catch (ResourceLoadException e) {
            throw new NotYetRenderableException("Unable to load external graphic.",e);
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
