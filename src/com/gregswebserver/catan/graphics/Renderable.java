package com.gregswebserver.catan.graphics;

/**
 * Created by Greg on 8/14/2014.
 * A renderable image, for use in rendering objects on the screen.
 */
public interface Renderable {

    public int getWidth();

    public int getHeight();

    public RenderMask getMask();

    public int[] getPixels();
}
