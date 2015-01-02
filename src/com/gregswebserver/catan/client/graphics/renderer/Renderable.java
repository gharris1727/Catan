package com.gregswebserver.catan.client.graphics.renderer;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.util.Graphic;

import java.awt.*;

/**
 * Created by Greg on 8/16/2014.
 * An object that can be rendered to the screen.
 */
public interface Renderable {

    public void renderTo(Graphic to, RenderMask toMask, Point toPos, int color);
}
