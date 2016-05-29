package com.gregswebserver.catan.client.ui.game.postgame;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenContainer;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.client.ui.ClientScreen;
import com.gregswebserver.catan.client.ui.game.ContextRegion;
import com.gregswebserver.catan.client.ui.game.TimelineRegion;
import com.gregswebserver.catan.client.ui.game.map.MapRegion;
import com.gregswebserver.catan.client.ui.game.map.TeamColors;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.scoring.reporting.team.LocalizedTeamScorePrinter;

import java.awt.*;

/**
 * Created by greg on 5/28/16.
 * A Screen that is displayed after a game is finished.
 * Allows the players to explore the map, and view the scores that various players recieved.
 */
public class PostGameScreenRegion extends ClientScreen {

    private final CatanGame game;
    private final ContextRegion context;
    private final ScrollingScreenContainer map;
    private final TimelineRegion timeline;

    private int sidebarWidth;
    private int inventoryHeight;
    private int contextHeight;
    private int timelineHeight;
    private Dimension borderBuffer;

    public PostGameScreenRegion(GameManager manager, TeamColors teamColors) {
        super("postgame");
        this.game = manager.getLocalGame();
        context = new ContextRegion(manager, null);
        map = new ScrollingScreenContainer(0, "scroll", new MapRegion(context, game.getBoard(), teamColors)) {
            @Override
            public String toString() {
                return "MapScrollContainer";
            }
        };
        timeline = new TimelineRegion(context, manager, teamColors);
        //TODO: add sidebar with score information about the game.
        add(context);
        add(map);
        add(timeline);
    }

    public void target(Object o) {
        context.target(o);
    }

    @Override
    public void refresh() {
        map.update();
        timeline.update();
        context.forceRender();
    }

    @Override
    public void loadConfig(UIConfig config) {
        sidebarWidth = config.getLayout().getInt("sidebar.width");
        inventoryHeight = config.getLayout().getInt("inventory.height");
        contextHeight = config.getLayout().getInt("context.height");
        timelineHeight = config.getLayout().getInt("timeline.height");
        borderBuffer = config.getLayout().getDimension("borderbuffer");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth();
        int timelineWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - timelineHeight;
        int tradeHeight = mask.getHeight() - inventoryHeight - contextHeight;

        context.setPosition(new Point(timelineWidth, inventoryHeight + tradeHeight));
        timeline.setPosition(new Point(0,mainHeight));

        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));

        context.setMask(new RectangularMask(new Dimension(sidebarWidth, contextHeight)));
        timeline.setMask(new RectangularMask(new Dimension(timelineWidth, timelineHeight)));

        Insets viewInsets = new Insets(borderBuffer.height, borderBuffer.width, borderBuffer.height, borderBuffer.width);
        map.setInsets(viewInsets);
        map.center();
    }

    private void printScore() {
        if (getConfig() != null) {
            LocalizedTeamScorePrinter p = new LocalizedTeamScorePrinter(getConfig().getLocale());
            System.out.println(p.getLocalization(game.getScore()));
        }
    }

    public String toString() {
        return "PostGameScreenRegion";
    }
}
