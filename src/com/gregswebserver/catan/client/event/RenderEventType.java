package com.gregswebserver.catan.client.event;

import com.gregswebserver.catan.common.crypto.ServerList;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.CatanGame;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the RenderEvent indicating what is to be done with the data at the renderer.
 * General render interface information.
 * References are used to generate InputListener actions.
 */
public enum RenderEventType implements EventType {

    ConnectionList_Create(ServerList.class),
    Game_Create(CatanGame.class),
    Game_Update(null),
    Window_Resize(null),
    Render_Disable(null),
    Render_Enable(null),
    Animation_Step(null);

    private Class payloadType;

    RenderEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public Class getType() {
        return payloadType;
    }
}
