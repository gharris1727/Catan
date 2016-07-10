package catan.common.game.players;

import catan.common.crypto.Username;
import catan.common.event.ReversibleEventConsumer;
import catan.common.game.gameplay.trade.Trade;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.teams.TeamColor;
import catan.common.game.util.EnumCounter;
import catan.common.game.util.GameResource;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by greg on 6/26/16.
 * Interface defining the behavior of a player in a catan game.
 */
public interface Player extends Serializable, ReversibleEventConsumer<PlayerEvent> {
    Username getName();

    TeamColor getTeamColor();

    EnumCounter<GameResource> getInventory();

    EnumCounter<DevelopmentCard> getBoughtCards();

    EnumCounter<DevelopmentCard> getDevelopmentCards();

    Set<Trade> getTrades();

    boolean canMakeTrade(Trade t);

    int getDiscardCount();

}
