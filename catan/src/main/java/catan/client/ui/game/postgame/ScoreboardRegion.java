package catan.client.ui.game.postgame;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.*;
import catan.client.structure.GameManager;
import catan.common.game.scoring.reporting.team.TeamScoreReport;
import catan.common.locale.game.scoring.LocalizedTeamScorePrinter;

/**
 * Created by greg on 5/30/16.
 * Region to display post-game score information
 */
public class ScoreboardRegion extends ConfigurableScreenRegion implements Updatable {

    //Required instance information
    private final GameManager manager;

    //Configuration dependencies
    private LocalizedTeamScorePrinter teamScorePrinter;

    //Sub-regions
    private final TiledBackground background;
    private final TextLabel text;

    //TODO: replace this lazy printing string method to a fancy UI.

    public ScoreboardRegion(GameManager manager) {
        super("ScoreboardRegion", 1, "scoreboard");
        this.manager = manager;
        background = new EdgedTiledBackground();
        text = new TextLabel("ScoreboardText", 1, "all", null);
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    @Override
    protected void renderContents() {
        TeamScoreReport report;
        synchronized (manager) {
            report = manager.getLocalGame().getScore();
        }
        String localization = teamScorePrinter.getLocalization(report);
        text.setText(localization);
        center(text);
    }

    @Override
    public void loadConfig(UIConfig config) {
        teamScorePrinter = new LocalizedTeamScorePrinter(config.getLocale());
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public void update() {
        forceRender();
    }
}
