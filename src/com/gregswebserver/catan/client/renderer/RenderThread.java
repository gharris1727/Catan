package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.ClientEvent;
import com.gregswebserver.catan.client.ClientEventType;
import com.gregswebserver.catan.client.chat.ChatLog;
import com.gregswebserver.catan.client.graphics.Screen;
import com.gregswebserver.catan.client.graphics.ScreenArea;
import com.gregswebserver.catan.client.graphics.StaticGraphic;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.game.CatanGame;
import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Tile;
import com.gregswebserver.catan.game.gameplay.GameAction;
import com.gregswebserver.catan.util.GraphicsConfig;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread {

    //TODO: clean up this class and break up parts.

    private Client client;
    private CatanGame activeGame;
    private Point viewPosition;
    private ChatLog chat;

    //Default values, require the real values from the Screen_Resize event.
    private Dimension screenSize = new Dimension(512, 512);
    private Dimension gameSize = new Dimension(256, 256);
    private Dimension sidebarSize = new Dimension(256, 256);
    private Dimension bottomSize = new Dimension(256, 256);
    private Dimension cornerSize = new Dimension(256, 256);

    private Screen screen;
    private ScreenArea area;
    private ScreenArea foreground;
    private ScreenArea background;
    private ScreenArea game;
    private ScreenArea sidebar;
    private ScreenArea bottom;
    private ScreenArea corner;

    private boolean enabled = false;

    public RenderThread(Client client) {
        super(client.logger);
        this.client = client;
        screen = new Screen();
        //Default size, used to create the ScreenAreas.
        viewPosition = new Point();

        //T1 ScreenArea
        area = new ScreenArea(screenSize, new Point(), 0);

        //T2 ScreenAreas
        foreground = new ScreenArea(screenSize, new Point(), 1);
        area.addScreenObject(foreground);
        background = new ScreenArea(screenSize, new Point(), 0);
        area.addScreenObject(background);

        //T3 ScreenAreas
        game = new ScreenArea(gameSize, new Point(), 0);
        background.addScreenObject(game);
        sidebar = new ScreenArea(sidebarSize, new Point(gameSize.width, 0), 1);
        background.addScreenObject(sidebar);
        bottom = new ScreenArea(bottomSize, new Point(0, gameSize.height), 2);
        background.addScreenObject(bottom);
        corner = new ScreenArea(cornerSize, new Point(gameSize.width, gameSize.height), 3);
        background.addScreenObject(corner);

        client.addEvent(new ClientEvent(this, ClientEventType.Hitbox_Update, area));
    }

    private void resize(Dimension screenSize) {
        this.screenSize = screenSize;
        gameSize = new Dimension(screenSize.width - 256, screenSize.height - 256);
        sidebarSize = new Dimension(256, gameSize.height);
        bottomSize = new Dimension(gameSize.width, 256);

        //Resize the necessary ScreenAreas.
        foreground.resize(screenSize);
        background.resize(screenSize);
        game.resize(gameSize);
        sidebar.resize(sidebarSize);
        bottom.resize(bottomSize);
        area.resize(screenSize);

        //Actually resize the screen graphic and canvas.
        screen.setSize(screenSize);

        client.addEvent(new ClientEvent(this, ClientEventType.Canvas_Update, screen.getCanvas()));
    }

    public void render() {
        area.getGraphic().renderTo(screen, null, new Point(), area.getHitboxColor());
    }

    private void actionRender(GameAction action) {
        //Process the GameAction and mark that region to be re-rendered.
        //TODO: implement.
    }

    private void gameRender() {
        //Render the activeGame board in it's entirety.
        GameBoard board = activeGame.getBoard();
        //TODO: add all objects to the (game) ScreenArea.

        HashMap<Coordinate, Tile> tiles = board.hexArray.spaces.toHashMap();
        HashMap<Coordinate, Path> paths = board.hexArray.edges.toHashMap();
        HashMap<Coordinate, Building> buildings = board.hexArray.vertices.toHashMap();

        for (Coordinate c : tiles.keySet()) {
            int colorIndex = c.x % 2 + 2 * (c.y % 2);
            Tile tile = tiles.get(c);
            game.addScreenObject(new StaticGraphic(
                    tile.getGraphic(),
                    GraphicsConfig.tileToScreen(c),
                    c.hashCode(),
                    tile));
        }
        for (Coordinate c : paths.keySet()) {
            int colorIndex = c.x % 6;
            Path path = paths.get(c);
            game.addScreenObject(new StaticGraphic(
                    path.getGraphic(),
                    GraphicsConfig.edgeToScreen(c),
                    c.hashCode(),
                    path));
        }
        for (Coordinate c : buildings.keySet()) {
            int colorIndex = c.x % 4;
            Building building = buildings.get(c);
            game.addScreenObject(new StaticGraphic(
                    building.getGraphic(),
                    GraphicsConfig.vertexToScreen(c),
                    c.hashCode(),
                    building));
        }
    }

    protected void execute() throws ThreadStop {
        //Process the event queue without blocking, allowing the renderer to run more than once per event.
        RenderEvent event = (RenderEvent) getEvent(false);
        if (event != null) {
            switch (event.type) {
                case Chat_Create:
                    this.chat = (ChatLog) event.data;
                    break;
                case Chat_Update:
                    break;
                case Game_Create:
                    this.activeGame = (CatanGame) event.data;
                    gameRender();
                    break;
                case Game_Scroll:
                    game.setView((Point) event.data);
                    break;
                case Game_Update:
                    //process the game action, find out what changed, and re-render that section of the screen.
                    actionRender((GameAction) event.data);
                    break;
                case Player_Update:
                    break;
                case Window_Resize:
                    enabled = false;
                    resize((Dimension) event.data);
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
            //Compile all of the renderings.
            render();
            //Send the rendered image to the output screen.
            screen.show();
        }
        //TODO: implement a render limiter.
    }

    public String toString() {
        return client + "RenderThread";
    }

}
