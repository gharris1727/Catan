package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.ClientEvent;
import com.gregswebserver.catan.client.event.ClientEventType;
import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.ScreenCanvas;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.renderer.ingame.InGameScreenRegion;
import com.gregswebserver.catan.client.renderer.primary.connecting.ConnectingScreenRegion;
import com.gregswebserver.catan.client.renderer.primary.disconnected.DisconnectedScreenRegion;
import com.gregswebserver.catan.client.renderer.primary.disconnecting.DisconnectingScreenRegion;
import com.gregswebserver.catan.client.renderer.secondary.connected.ServerScreenRegion;
import com.gregswebserver.catan.client.state.ClientState;
import com.gregswebserver.catan.client.ui.primary.ServerPool;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.lobby.MatchmakingPool;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;

import static com.gregswebserver.catan.client.state.ClientState.*;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread<RenderEvent> {

    private Client client;
    private UIStyle style = UIStyle.Blue;
    private RenderMask screenMask = new RectangularMask(new Dimension(640, 480));
    private ScreenCanvas canvas;
    private HashMap<ClientState, ScreenRegion> areas;
    private ScreenRegion root;

    public RenderThread(Client client) {
        super(client.logger);
        this.client = client;
        areas = new HashMap<>();
        root = null;
    }

    protected void execute() throws ThreadStop {
        //Process the event queue, blocking for every event. Only re-renders what needs to be re-rendered.
        RenderEvent event = getEvent(false);
        if (event != null) {
            switch (event.getType()) {
                case ConnectionListCreate:
                    DisconnectedScreenRegion connect = new DisconnectedScreenRegion(style, (ServerPool) event.getPayload());
                    areas.put(Disconnected, connect);
                    break;
                case ConnectProgress:
                    //TODO: use the progress indicator.
                    ConnectingScreenRegion connecting = new ConnectingScreenRegion(style);
                    areas.put(Connecting, connecting);
                    break;
                case LobbyListUpdate:
                    ServerScreenRegion lobby = new ServerScreenRegion(style, (MatchmakingPool) event.getPayload());
                    areas.put(Connected, lobby);
                    break;
                case DisconnectMessage:
                    DisconnectingScreenRegion disconnect = new DisconnectingScreenRegion(style, (String) event.getPayload());
                    areas.put(Disconnecting, disconnect);
                    break;
                case GameCreate:
                    InGameScreenRegion gameCreate = new InGameScreenRegion(style, (CatanGame) event.getPayload());
                    areas.put(InGame, gameCreate);
                    break;
                case Game_Update:
                    InGameScreenRegion gameUpdate = (InGameScreenRegion) areas.get(InGame);
                    gameUpdate.update();
                    break;
                case Canvas_Update:
                    canvas = (ScreenCanvas) event.getPayload();
                    screenMask = new RectangularMask(canvas.getSize());
                    for (ScreenRegion area : areas.values())
                        area.setMask(screenMask);
                    break;
                case Animation_Step:
                    if (root != null && root.isAnimated())
                        root.step();
                    break;
            }
        } else { //No event to be processed this round.
            ScreenRegion next = areas.get(client.getState());
            if (root != next) {
                //Need to load the next screen region
                client.addEvent(new ClientEvent(this, ClientEventType.Hitbox_Update, next));
                //Resize the screen if it is newly loaded.
                if (next != null)
                    next.setMask(screenMask);
            }
            root = next;
            if (canvas != null) {
                Graphic screen = canvas.getGraphic();
                screen.clear();
                if (root != null)
                    root.getGraphic().renderTo(screen, new Point(), 0);
                canvas.render();
            }
        }
    }

    public String toString() {
        return "RenderThread";
    }

}
