package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.chat.ChatLog;
import com.gregswebserver.catan.event.EventPayloadException;
import com.gregswebserver.catan.event.EventType;
import com.gregswebserver.catan.game.board.GameBoard;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the RenderEvent indicating what is to be done with the data at the renderer.
 * General render interface information.
 * References are used to generate InputListener actions.
 */
public enum RenderEventType implements EventType {

    Chat_Update(null),
    Game_Create(GameBoard.class),
    Game_Update(null),
    Player_Update(null),
    Window_Resize(null),
    Render_Disable(null),
    Render_Enable(null),
    Chat_Create(ChatLog.class),
    Game_Scroll(null);

    private Class payloadType;

    RenderEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public void checkPayload(Object o) {
        if (payloadType != null && o != null && o.getClass().isAssignableFrom(payloadType))
            throw new EventPayloadException(o, payloadType);
    }

    public Class getType() {
        return payloadType;
    }
}
