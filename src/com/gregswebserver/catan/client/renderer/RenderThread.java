package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.ClientEvent;
import com.gregswebserver.catan.client.ClientEventType;
import com.gregswebserver.catan.client.chat.ChatLog;
import com.gregswebserver.catan.client.graphics.*;
import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.client.input.clickables.ClickableBuilding;
import com.gregswebserver.catan.client.input.clickables.ClickableInventoryItem;
import com.gregswebserver.catan.client.input.clickables.ClickablePath;
import com.gregswebserver.catan.client.input.clickables.ClickableTile;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.game.CatanGame;
import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Tile;
import com.gregswebserver.catan.game.gameplay.GameAction;
import com.gregswebserver.catan.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.game.player.Player;
import com.gregswebserver.catan.game.player.Team;
import com.gregswebserver.catan.util.GraphicsConfig;
import com.gregswebserver.catan.util.Statics;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread {

    //TODO: clean up this class and break up parts.

    private final Point viewPosition;
    //All the position information is final because it is passed by reference, and must stay the same.
    //Positions can be changed by using setLocation(Point);
    //the offScreen point is set arbitrarily high so that the render method fails immediately.
    private final Point onScreen = new Point(0, 0);
    private final Point gamePosition = onScreen;
    private final Point offScreen = new Point(1920, 1080);
    private final Point serverPosition = offScreen;
    private final Point connectPosition = offScreen;
    private final Point lobbyPosition = offScreen;
    private final Point sidebarPosition = new Point(256, 0);
    private final Point bottomPosition = new Point(0, 256);
    private final Point cornerPosition = new Point(256, 256);
    private Client client;
    private CatanGame activeGame;
    private Player localPlayer;
    private ChatLog chat;
    //Default values, require the real values from the Screen_Resize event.
    private Dimension screenSize = new Dimension(512, 512);
    private Dimension focusSize = new Dimension(256, 256);
    private Dimension sidebarSize = new Dimension(256, 256);
    private Dimension bottomSize = new Dimension(256, 256);
    private Dimension contextSize = new Dimension(256, 256);
    private Screen screen;
    private ScreenArea area;

    private ScreenArea gameScreen;

    private ScreenArea connectScreen;
    private Dialog connectDialog;

    private ScreenArea serverScreen;
    private Dialog serverDialog;

    private ScreenArea lobbyScreen;
    private Dialog lobbyDialog;

    private ScreenArea sidebar;
    private ScreenArea bottom;
    private ScreenArea contextMenu;

    private boolean enabled = false;

    public RenderThread(Client client) {
        super(client.logger);
        this.client = client;
        screen = new Screen();
        viewPosition = new Point();

        //T1 ScreenArea
        area = new ScreenArea(screenSize, new Point(), new Point(), 0);

        //T2 ScreenAreas
        gameScreen = new ScreenArea(focusSize, gamePosition, viewPosition, 0);
        area.addScreenObject(gameScreen);
        connectScreen = new ScreenArea(focusSize, connectPosition, new Point(), 1);
        area.addScreenObject(connectScreen);
        serverScreen = new ScreenArea(focusSize, serverPosition, new Point(), 2);
        area.addScreenObject(serverScreen);
        lobbyScreen = new ScreenArea(focusSize, lobbyPosition, new Point(), 3);
        area.addScreenObject(lobbyScreen);
        sidebar = new ScreenArea(sidebarSize, sidebarPosition, new Point(), 4);
        area.addScreenObject(sidebar);
        bottom = new ScreenArea(bottomSize, bottomPosition, new Point(), 5);
        area.addScreenObject(bottom);
        contextMenu = new ScreenArea(contextSize, cornerPosition, new Point(), 6);
        area.addScreenObject(contextMenu);

        client.addEvent(new ClientEvent(this, ClientEventType.Hitbox_Update, area));
    }

    private void resize(Dimension screenSize) {
        this.screenSize = screenSize;
        if (offScreen.x < screenSize.width || offScreen.y < screenSize.height)
            //Ensure that the off-screen coordinate is truly 'off the screen'
            offScreen.setLocation(screenSize.width, screenSize.height);
        focusSize = new Dimension(screenSize.width - 256, screenSize.height - 256);
        sidebarSize = new Dimension(256, focusSize.height);
        bottomSize = new Dimension(focusSize.width, 256);

        //Resize the necessary ScreenAreas.
        area.resize(screenSize);
        gameScreen.resize(focusSize);
        sidebar.resize(sidebarSize);
        bottom.resize(bottomSize);
        sidebarPosition.setLocation(focusSize.getWidth(), 0);
        bottomPosition.setLocation(0, focusSize.getHeight());
        cornerPosition.setLocation(focusSize.width, focusSize.height);

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

    private void tradeRender() {
        //TODO: get all of the trades and render them on the side bar.
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
            Clickable clickable = new ClickableTile(c, tile);
            gameScreen.addScreenObject(new StaticGraphic(
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
            Clickable clickable = new ClickablePath(c, path);
            gameScreen.addScreenObject(new StaticGraphic(
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
            Clickable clickable = new ClickableBuilding(c, building);
            gameScreen.addScreenObject(new StaticGraphic(
                    graphic,
                    GraphicsConfig.vertexToScreen(c),
                    c.hashCode(),
                    clickable));
        }
    }

    public void playerRender() {
        HashMap<Tradeable, Integer> inventory = localPlayer.getInventory();
        ArrayList<Point> positions = new ArrayList<>();
        for (Tradeable t : inventory.keySet()) {
            int num = inventory.get(t);
            Graphic graphic = Statics.nullGraphic;
            if (t instanceof Graphical)
                graphic = ((Graphical) t).getGraphic();
            Point position = new Point();
            positions.add(position);
            Clickable clickable = new ClickableInventoryItem();
            for (int i = 0; i < num; i++) {
                bottom.addScreenObject(new StaticGraphic(
                        graphic,
                        position,
                        0,
                        clickable));
            }
        }
        int divX = (bottomSize.width - 128) / positions.size();
        for (int i = 0; i < positions.size(); i++) {
            positions.get(i).setLocation(divX * (i + 1), 16);
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
                    gameScreen.changeView((Point) event.data);
                    break;
                case Game_Update:
                    //process the game action, find out what changed, and re-render that section of the screen.
                    actionRender((GameAction) event.data);
                    break;
                case Player_Update:
                    localPlayer = (Player) event.data;
                    playerRender();
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
        //TODO: add a better frame limiter/profiling/fps tool.
        if (event == null && enabled) {
            //Compile all of the renderings.
            render();
            //Send the rendered image to the output screen.
            screen.show();
        }
    }

    public String toString() {
        return client + "RenderThread";
    }

}
