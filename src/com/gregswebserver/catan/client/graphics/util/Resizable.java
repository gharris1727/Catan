package com.gregswebserver.catan.client.graphics.util;

import java.awt.*;

/**
 * Created by Greg on 1/5/2015.
 * A resizable graphical object.
 */
public interface Resizable {

    public void resize(Dimension d);

    public Dimension getSize();
}
