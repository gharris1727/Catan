package com.gregswebserver.catan.client.renderer;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the RenderEvent indicating what is to be done with the data at the renderer.
 * General render interface information.
 * References are used to generate InputListener actions.
 */
public enum RenderEventType {

    Chat_Update,
    Game_Create,
    Game_Update,
    Player_Update,
    Window_Resize,
    Render_Disable,
    Render_Enable,
    Chat_Create,
    Game_Scroll,
}
