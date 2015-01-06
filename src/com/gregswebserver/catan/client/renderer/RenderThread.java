package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.ClientEvent;
import com.gregswebserver.catan.client.event.ClientEventType;
import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.graphics.areas.ScreenArea;
import com.gregswebserver.catan.client.graphics.util.Screen;
import com.gregswebserver.catan.client.renderer.connect.ConnectScreen;
import com.gregswebserver.catan.client.renderer.ingame.InGameScreen;
import com.gregswebserver.catan.client.state.ClientState;
import com.gregswebserver.catan.common.crypto.ServerList;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread<RenderEvent> {

    private Client client;
    private Screen screen;
    private HashMap<ClientState, ScreenArea> areas;
    private ScreenArea screenArea;
    private boolean enabled;

    public RenderThread(Client client) {
        super(client.logger);
        this.client = client;
        screen = new Screen();
        areas = new HashMap<>();
        screenArea = null;
        enabled = false;
    }

    protected void execute() throws ThreadStop {
        //Process the event queue, blocking for every event. Only re-renders what needs to be re-rendered.
        RenderEvent event = getEvent(true);
        switch (event.getType()) {
            case ConnectionList_Create:
                ConnectScreen connect = new ConnectScreen((ServerList) event.getPayload());
                areas.put(ClientState.Disconnected, connect);
                break;
            case Game_Create:
                InGameScreen gameCreate = new InGameScreen((CatanGame) event.getPayload());
                areas.put(ClientState.InGame, gameCreate);
                break;
            case Game_Scroll:
                InGameScreen gameScroll = (InGameScreen) areas.get(ClientState.InGame);
                gameScroll.scroll((Point) event.getPayload());
                break;
            case Game_Update:
                InGameScreen gameUpdate = (InGameScreen) areas.get(ClientState.InGame);
                gameUpdate.update();
                break;
            case Window_Resize:
                enabled = false;
                Dimension size = (Dimension) event.getPayload();
                screen.resize(size);
                for (ScreenArea area : areas.values())
                    area.resize(size);
                client.addEvent(new ClientEvent(this, ClientEventType.Canvas_Update, screen.getCanvas()));
                break;
            case Render_Disable:
                enabled = false;
                break;
            case Render_Enable:
                enabled = true;
                break;
            case Animation_Step:
                if (screenArea != null)
                    screenArea.step();
                break;
        }
        if (enabled) {
            ScreenArea next = areas.get(client.getState());
            screen.clear();
            if (screenArea != next)
                client.addEvent(new ClientEvent(this, ClientEventType.Hitbox_Update, screenArea));
            screenArea = next;
            if (screenArea != null && screenArea.canRender())
                screenArea.getGraphic().renderTo(screen, null, new Point(), 0);
            screen.show();
        }
    }

    public String toString() {
        return "RenderThread";
    }


}
