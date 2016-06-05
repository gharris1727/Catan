package com.gregswebserver.catan.client.ui.game.postgame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.scoring.reporting.team.TeamScoreReport;
import com.gregswebserver.catan.common.locale.game.LocalizedTeamScorePrinter;

/**
 * Created by greg on 5/30/16.
 * Region to display post-game score information
 */
public class ScoreboardRegion extends ConfigurableScreenRegion implements Updatable {

    //Required instance information
    private final CatanGame game;

    //Configuration dependencies
    private LocalizedTeamScorePrinter teamScorePrinter;

    //Sub-regions
    private final TiledBackground background;
    private final TextLabel text;

    //TODO: replace this lazy printing string method to a fancy UI.

    public ScoreboardRegion(CatanGame game) {
        super("ScoreboardRegion", 1, "scoreboard");
        this.game = game;
        background = new EdgedTiledBackground();
        text = new TextLabel("ScoreboardText", 1, "all", null);
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    @Override
    protected void renderContents() {
        TeamScoreReport report = game.getScore();
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
