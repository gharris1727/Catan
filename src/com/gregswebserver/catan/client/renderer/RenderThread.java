package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.ClientEvent;
import com.gregswebserver.catan.client.ClientEventType;
import com.gregswebserver.catan.client.chat.ChatLog;
import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.Screen;
import com.gregswebserver.catan.client.graphics.ScreenArea;
import com.gregswebserver.catan.client.graphics.StaticGraphic;
import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.client.input.ClickableBuilding;
import com.gregswebserver.catan.client.input.ClickablePath;
import com.gregswebserver.catan.client.input.ClickableTile;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.game.CatanGame;
import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Tile;
import com.gregswebserver.catan.game.gameplay.GameAction;
import com.gregswebserver.catan.game.player.Team;
import com.gregswebserver.catan.util.GraphicsConfig;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

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

    private Point gamePosition = new Point(0, 0);
    private Point sidebarPosition = new Point(256, 0);
    private Point bottomPosition = new Point(0, 256);
    private Point cornerPosition = new Point(256, 256);

    private Screen screen;
    private ScreenArea area;
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
        game = new ScreenArea(gameSize, gamePosition, 0);
        area.addScreenObject(game);
        sidebar = new ScreenArea(sidebarSize, sidebarPosition, 1);
        area.addScreenObject(sidebar);
        bottom = new ScreenArea(bottomSize, bottomPosition, 2);
        area.addScreenObject(bottom);
        corner = new ScreenArea(cornerSize, cornerPosition, 3);
        area.addScreenObject(corner);

        client.addEvent(new ClientEvent(this, ClientEventType.Hitbox_Update, area));
    }

    private void resize(Dimension screenSize) {
        this.screenSize = screenSize;
        gameSize = new Dimension(screenSize.width - 256, screenSize.height - 256);
        sidebarSize = new Dimension(256, gameSize.height);
        bottomSize = new Dimension(gameSize.width, 256);

        //Resize the necessary ScreenAreas.
        area.resize(screenSize);
        game.resize(gameSize);
        sidebar.resize(sidebarSize);
        bottom.resize(bottomSize);
        gamePosition.setLocation(0, 0);
        sidebarPosition.setLocation(gameSize.getWidth(), 0);
        bottomPosition.setLocation(0, gameSize.getHeight());
        cornerPosition.setLocation(gameSize.width, gameSize.height);

        //Actually resize the screen graphic and canvas.
        screen.setSize(screenSize);

        client.addEvent(new ClientEvent(this, ClientEventType.Canvas_Update, screen.getCanvas()));
    }

    public void render() {
        screen.clear();
        area.getGraphic().renderTo(screen, null, new Point(), 0);
    }

    private void actionRender(GameAction action) {
        //Process the GameAction and mark that region to be re-rendered.
        //TODO: implement.
    }

    private void gameRender() {
        //Render the activeGame board in it's entirety.
        GameBoard board = activeGame.getBoard();

        HashSet<Coordinate> spaces = board.hexArray.spaces.getAllCoordinates();
        HashSet<Coordinate> edges = board.hexArray.edges.getAllCoordinates();
        HashSet<Coordinate> vertices = board.hexArray.vertices.getAllCoordinates();

        HashMap<Coordinate, Tile> tiles = board.hexArray.spaces.toHashMap();
        HashMap<Coordinate, Path> paths = board.hexArray.edges.toHashMap();
        HashMap<Coordinate, Building> buildings = board.hexArray.vertices.toHashMap();

        for (Coordinate c : spaces) {
            Tile tile = tiles.get(c);
            Graphic graphic = tile.getGraphic();
            Clickable clickable = new ClickableTile(c);
            game.addScreenObject(new StaticGraphic(
                    graphic,
                    GraphicsConfig.tileToScreen(c),
                    c.hashCode(),
                    clickable));
        }
        for (Coordinate c : edges) {
            Path path = paths.get(c);
            Graphic graphic = null;
            if (path != null) graphic = path.getGraphic();
            if (graphic == null) graphic = Team.Blank.paths[c.x % 3];
            Clickable clickable = new ClickablePath(c);
            game.addScreenObject(new StaticGraphic(
                    graphic,
                    GraphicsConfig.edgeToScreen(c),
                    c.hashCode(),
                    clickable));
        }
        for (Coordinate c : vertices) {
            Building building = buildings.get(c);
            Graphic graphic = null;
            if (building != null) graphic = building.getGraphic();
            if (graphic == null) graphic = Team.Blank.settlement[c.x % 2];
            Clickable clickable = new ClickableBuilding(c);
            game.addScreenObject(new StaticGraphic(
                    graphic,
                    GraphicsConfig.vertexToScreen(c),
                    c.hashCode(),
                    clickable));
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
                    game.changeView((Point) event.data);
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
