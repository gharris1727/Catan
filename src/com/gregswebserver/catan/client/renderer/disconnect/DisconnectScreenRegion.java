package com.gregswebserver.catan.client.renderer.disconnect;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;

import java.awt.*;

/**
 * Created by Greg on 1/17/2015.
 * A screen shown when the client disconnects for whatever reason.
 */
public class DisconnectScreenRegion extends UIScreenRegion {

    private String message;

    public DisconnectScreenRegion(RenderMask screenMask, UIStyle style, String message) {
        super(new Point(), 0, screenMask, style);
        this.message = message;
    }

    public String toString() {
        return "DisconnectScreen";
    }
}
