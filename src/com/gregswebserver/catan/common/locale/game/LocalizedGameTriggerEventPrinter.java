package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.board.BoardEvent;
import com.gregswebserver.catan.common.game.event.GameTriggerEvent;
import com.gregswebserver.catan.common.game.gamestate.GameStateEvent;
import com.gregswebserver.catan.common.game.players.PlayerEvent;
import com.gregswebserver.catan.common.game.scoring.ScoreEvent;
import com.gregswebserver.catan.common.game.teams.TeamEvent;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;
import com.gregswebserver.catan.common.locale.game.triggers.LocalizedBoardEventPrinter;
import com.gregswebserver.catan.common.locale.game.triggers.LocalizedGameStateEventPrinter;
import com.gregswebserver.catan.common.locale.game.triggers.LocalizedPlayerEventPrinter;
import com.gregswebserver.catan.common.locale.game.triggers.LocalizedTeamEventPrinter;

/**
 * Created by greg on 6/11/16.
 * A LocalizedPrinter for printing the different types of GameTriggerEvents.
 */
public class LocalizedGameTriggerEventPrinter extends LocalizedPrinter<GameTriggerEvent> {

    private final LocalizedPlayerEventPrinter playerPrinter;
    private final LocalizedBoardEventPrinter boardPrinter;
    private final LocalizedTeamEventPrinter teamPrinter;
    private final LocalizedGameStateEventPrinter statePrinter;

    public LocalizedGameTriggerEventPrinter(ConfigSource locale) {
        super(locale);
        playerPrinter = new LocalizedPlayerEventPrinter(locale);
        boardPrinter = new LocalizedBoardEventPrinter(locale);
        teamPrinter = new LocalizedTeamEventPrinter(locale);
        statePrinter = new LocalizedGameStateEventPrinter(locale);
    }

    @Override
    public String getLocalization(GameTriggerEvent instance) {
        if (instance instanceof PlayerEvent)
            return playerPrinter.getLocalization((PlayerEvent) instance);
        else if (instance instanceof BoardEvent)
            return boardPrinter.getLocalization((BoardEvent) instance);
        else if (instance instanceof TeamEvent)
            return teamPrinter.getLocalization((TeamEvent) instance);
        else if (instance instanceof GameStateEvent)
            return statePrinter.getLocalization((GameStateEvent) instance);
        else if (instance instanceof ScoreEvent)
            return null;
        else
            throw new IllegalStateException();
    }
}
