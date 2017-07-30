package catan.client.graphics.ui;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.Graphical;

/**
 * Created by greg on 3/25/16.
 * An object that has a size that can be changed dynamically.
 */
public interface Resizable extends Graphical {

    void setMask(RenderMask mask);
}
