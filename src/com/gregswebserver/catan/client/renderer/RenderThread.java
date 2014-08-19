package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.ClientEvent;
import com.gregswebserver.catan.client.ClientEventType;
import com.gregswebserver.catan.client.chat.ChatLog;
import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.Screen;
import com.gregswebserver.catan.client.hitbox.ColorHitbox;
import com.gregswebserver.catan.client.hitbox.GridHitbox;
import com.gregswebserver.catan.client.hitbox.HitboxColor;
import com.gregswebserver.catan.client.masks.OffsetMask;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.game.CatanGame;
import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Tile;
import com.gregswebserver.catan.game.gameplay.GameAction;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread {

    private Client client;
    private CatanGame activeGame;
    private Graphic gameRender;
    private Point viewPosition;
    private ChatLog chat;

    //Tiered Hitboxes
    //T1
    private ColorHitbox foregroundHitbox;
    //T2
    private GridHitbox backgroundHitbox;
    //T3
    private ColorHitbox gameHitbox;
    private ColorHitbox sidebarHitbox;
    private ColorHitbox bottomHitbox;
    private ColorHitbox cornerHitbox;

    //Tiered graphics.
    //T1
    private Screen screen;
    //T2
    private Graphic background;
    private Graphic foreground;
    //T3
    private Graphic gameVisible; //Visible region
    private Graphic sidebar;
    private Graphic bottom;
    private Graphic corner;

    private int[] gridColumns;
    private int[] gridRows;

    private boolean enabled = false;

    public RenderThread(Client client) {
        super(client.logger);
        this.client = client;
        screen = new Screen();
        gridColumns = new int[2];
        gridRows = new int[2];
    }

    private void resize(Dimension d) {
        //Actually resize the screen graphic and canvas.
        screen.setSize(d);
        background = new Graphic(d.width, d.height);
        foreground = new Graphic(d.width, d.height);
        viewPosition = new Point();

        //Generate the sizes for use in the GridHitbox.
        //TODO: add config values for these.
        int SIDEBAR_WIDTH = 256;
        int BOTTOM_HEIGHT = 256;
        gridColumns[0] = d.width - SIDEBAR_WIDTH;
        gridColumns[1] = SIDEBAR_WIDTH;
        gridRows[0] = d.height - BOTTOM_HEIGHT;
        gridRows[1] = BOTTOM_HEIGHT;

        //Create the T1 and T2 hitboxes
        foregroundHitbox = new ColorHitbox(screen);
        backgroundHitbox = new GridHitbox(d.width, d.height, gridColumns, gridRows);
        foregroundHitbox.setObject(HitboxColor.Default, backgroundHitbox);

        //Create the T3 hitboxes and pass them to the T2
        gameVisible = new Graphic(gridColumns[0], gridRows[0]);
        gameHitbox = new ColorHitbox(gameVisible);
        backgroundHitbox.addObject(0, 0, gameHitbox);

        bottom = new Graphic(gridColumns[0], gridRows[1]);
        bottomHitbox = new ColorHitbox(bottom);
        backgroundHitbox.addObject(0, 1, bottom);

        sidebar = new Graphic(gridColumns[1], gridRows[0]);
        sidebarHitbox = new ColorHitbox(sidebar);
        backgroundHitbox.addObject(1, 0, sidebar);

        corner = new Graphic(gridColumns[1], gridRows[1]);
        cornerHitbox = new ColorHitbox(corner);
        backgroundHitbox.addObject(1, 1, corner);

        client.addEvent(new ClientEvent(this, ClientEventType.Canvas_Update, screen.getCanvas()));
        client.addEvent(new ClientEvent(this, ClientEventType.Hitbox_Update, foregroundHitbox));
    }

    public void render() {
        //TODO: pre-rendering.

        //Render the large view to the small one, using an OffsetMask.
        if (gameRender != null)
            gameRender.renderTo(gameVisible, new OffsetMask(gameVisible.getMask(), viewPosition), new Point(), 0);
        gameVisible.renderTo(background, null, new Point(), 0);

        background.renderTo(screen, null, new Point(), 0);
        foreground.renderTo(screen, null, new Point(), 0);
    }

    private void actionRender(GameAction action) {
        //Process the GameAction and re-render that region of the gameRender.
        //TODO: implement.
    }

    private void gameRender() {
        //Render the activeGame board in it's entirety.
        GameBoard board = activeGame.getBoard();

        //TODO: determine real minimum size.
        gameRender = new Graphic(board.hexArray.spaces.sizeX * 128, board.hexArray.spaces.sizeY * 128);

        HashMap<Coordinate, Tile> tiles = board.hexArray.spaces.toHashMap();
        HashMap<Coordinate, Path> paths = board.hexArray.edges.toHashMap();
        HashMap<Coordinate, Building> buildings = board.hexArray.vertices.toHashMap();

        for (Coordinate c : tiles.keySet()) {
            tiles.get(c).getTerrain().image.renderTo(gameRender, null, tileRenderOffset(c, 100, 100, 50), 0);
        }
    }

    private Point tileRenderOffset(Coordinate c, int multX, int multY, int offY) {
        int outX = c.x * multX;
        int outY = c.y * multY;
        if (c.x % 2 == 1) outY += offY;
        return new Point(outX, outY);
    }

    private Point pathCoordinate(Coordinate c) {
        //TODO: finish logic in here.
        return new Point();
    }

    private Point buildingCoordinate(Coordinate c) {
        return new Point();
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
                    this.viewPosition = (Point) event.data;
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
