package com.gregswebserver.catan.client.renderer;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the RenderEvent indicating what is to be done with the data at the renderer.
 * General render interface information.
 * References are used to generate InputListener actions.
 */
public enum RenderEventType {

    Object_Add,
    Object_Remove,
    Object_Move,
    Screen_Clear,
}
