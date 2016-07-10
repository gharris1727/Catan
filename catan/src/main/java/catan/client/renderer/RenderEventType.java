package catan.client.renderer;

import catan.client.graphics.graphics.ScreenCanvas;
import catan.client.graphics.ui.UIConfig;
import catan.client.ui.ClientScreen;
import catan.client.ui.PopupWindow;
import catan.client.ui.taskbar.TaskbarMenu;
import catan.common.event.EventType;

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
