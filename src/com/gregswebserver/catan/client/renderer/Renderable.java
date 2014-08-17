package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.graphics.Graphic;

import java.awt.*;

/**
 * Created by Greg on 8/16/2014.
 * An object that can be rendered to the screen.
 */
public interface Renderable {

    public void renderTo(Graphic to, Point toPos);
}
