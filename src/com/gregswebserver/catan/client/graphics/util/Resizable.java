package com.gregswebserver.catan.client.graphics.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;

/**
 * Created by Greg on 1/5/2015.
 * A resizable graphical object.
 */
public interface Resizable {

    public RenderMask getMask();

    public void setMask(RenderMask mask);
}
