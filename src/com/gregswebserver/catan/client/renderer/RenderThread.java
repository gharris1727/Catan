package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.ClientEvent;
import com.gregswebserver.catan.client.ClientEventType;
import com.gregswebserver.catan.client.graphics.Screen;
import com.gregswebserver.catan.client.graphics.ScreenObject;
import com.gregswebserver.catan.client.hitbox.ColorHitbox;
import com.gregswebserver.catan.client.hitbox.GridHitbox;
import com.gregswebserver.catan.client.hitbox.Hitbox;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;

import java.awt.*;
import java.util.HashSet;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread {

    private Client client;

    //Hitbox tiers.
    //T1
    private ColorHitbox foregroundHitbox;
    //T2
    private GridHitbox backgroundHitbox;
    //T3
    private ColorHitbox gameHitbox;
    private ColorHitbox sidebarHitbox;
    private ColorHitbox bottomHitbox;
    private ColorHitbox cornerHitbox;

    //Graphic for passing into the ColorHitboxes.
    private Screen screen;

    //Large, resource intensive background objects. Majority of objects are here.
    //Gets relatively few updates and it's content is mostly static.
    private HashSet<ScreenObject> backgroundBuffer;

    //Small, light animations and things that need to be in front.
    //Stuff like cursors and menus.
    private HashSet<ScreenObject> foregroundBuffer;

    private boolean enabled = false;

    public RenderThread(Client client) {
        super(client.logger);
        this.client = client;
        screen = new Screen();
    }

    public void execute() throws ThreadStop {
        //Process the event queue without blocking, allowing the renderer to run more than once per event.
        RenderEvent event = (RenderEvent) getEvent(false);
        if (event != null) {
            switch (event.type) {
                case Chat_Update:
                    break;
                case Game_Update:
                    //TODO: make sure that when finally rendering the map to the screen that the screen's mask is spoofed.
                    break;
                case Player_Update:
                    break;
                case Window_Resize:
                    enabled = false;
                    screen.setSize((Dimension) event.data);
                    client.addEvent(new ClientEvent(this, ClientEventType.Canvas_Update, screen.getCanvas()));
                    break;
                case Render_Disable:
                    enabled = false;
                    break;
                case Render_Enable:
                    enabled = true;
                    break;
            }
        }
        if (enabled) {
            screen.show();
        }
        //TODO: implement a render limiter.
    }

    public Hitbox getHitbox() {
        return foregroundHitbox;
    }

}
