package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.graphics.graphics.ScreenCanvas;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.ui.ClientScreen;
import com.gregswebserver.catan.client.ui.PopupWindow;
import com.gregswebserver.catan.client.ui.taskbar.TaskbarMenu;
import com.gregswebserver.catan.common.event.EventType;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the RenderEvent indicating what is to be done with the data at the renderer.
 * General render interface information.
 * References are used to generate InputListener actions.
 */
public enum RenderEventType implements EventType {

    Canvas_Update(ScreenCanvas.class), //Update the screen canvas with a new one.
    Set_Configuration(UIConfig.class),
    Screen_Update(ClientScreen.class),
    Screen_Refresh(null),
    Taskbar_Add(TaskbarMenu.class),
    Taskbar_Remove(TaskbarMenu.class),
    Popup_Show(PopupWindow.class),
    Popup_Remove(PopupWindow.class),
    Animation_Step(null); //Step all animations.

    private final Class payloadType;

    RenderEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
