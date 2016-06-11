package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.board.BoardEvent;
import com.gregswebserver.catan.common.game.event.GameTriggerEvent;
import com.gregswebserver.catan.common.game.gamestate.GameStateEvent;
import com.gregswebserver.catan.common.game.players.PlayerEvent;
import com.gregswebserver.catan.common.game.scoring.ScoreEvent;
import com.gregswebserver.catan.common.game.teams.TeamEvent;
import com.gregswebserver.catan.common.locale.LocalizedEventPrinter;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 6/11/16.
 * A LocalizedPrinter for printing the different types of GameTriggerEvents.
 */
public class LocalizedGameTriggerEventPrinter extends LocalizedPrinter<GameTriggerEvent> {

    private final LocalizedEventPrinter playerPrinter;
    private final LocalizedEventPrinter boardPrinter;
    private final LocalizedEventPrinter teamPrinter;
    private final LocalizedEventPrinter statePrinter;
    private final LocalizedEventPrinter scorePrinter;

    public LocalizedGameTriggerEventPrinter(ConfigSource locale) {
        super(locale);
        playerPrinter = new LocalizedEventPrinter(locale, "game.player");
        boardPrinter = new LocalizedEventPrinter(locale, "game.board");
        teamPrinter = new LocalizedEventPrinter(locale, "game.team");
        statePrinter = new LocalizedEventPrinter(locale, "game.state");
        scorePrinter = new LocalizedEventPrinter(locale, "game.score");
    }

    @Override
    public String getLocalization(GameTriggerEvent instance) {
        if (instance instanceof PlayerEvent)
            return playerPrinter.getLocalization(instance);
        else if (instance instanceof BoardEvent)
            return boardPrinter.getLocalization(instance);
        else if (instance instanceof TeamEvent)
            return teamPrinter.getLocalization(instance);
        else if (instance instanceof GameStateEvent)
            return statePrinter.getLocalization(instance);
        else if (instance instanceof ScoreEvent)
            return scorePrinter.getLocalization(instance);
        else
            throw new IllegalStateException();
    }
}
