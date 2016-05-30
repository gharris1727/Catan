package com.gregswebserver.catan.client.ui.game.postgame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.ScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.team.TeamScoreReport;
import com.gregswebserver.catan.common.locale.game.LocalizedTeamScorePrinter;

/**
 * Created by greg on 5/30/16.
 * Region to display post-game score information
 */
public class ScoreboardRegion extends ConfigurableScreenRegion {

    //Required instance information
    private final CatanGame game;

    //Configuration dependencies
    private LocalizedTeamScorePrinter teamScorePrinter;

    //Sub-regions
    private final TiledBackground background;
    private final TextLabel text;

    public ScoreboardRegion(CatanGame game) {
        super(1, "scoreboard");
        this.game = game;
        background = new EdgedTiledBackground(0, "background");
        text = new TextLabel(1, "all", null);
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    @Override
    protected void renderContents() {
        TeamScoreReport report = game.getScore();
        String localization = teamScorePrinter.getLocalization(report);
        System.out.print(localization);
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
    public String toString() {
        return "ScoreboardRegion";
    }

    private class TeamScoreboard extends ConfigurableScreenRegion {

        private TeamScoreboard(ScoreReport report) {
            super(1, "team");
        }

        @Override
        protected void resizeContents(RenderMask mask) {

        }

        @Override
        public String toString() {
            return null;
        }

        private class PlayerScoreboard extends ConfigurableScreenRegion {


            private PlayerScoreboard(PlayerScoreReport report) {
                super(1, "player");
            }

            @Override
            protected void resizeContents(RenderMask mask) {

            }

            @Override
            public String toString() {
                return null;
            }
        }
    }
}
