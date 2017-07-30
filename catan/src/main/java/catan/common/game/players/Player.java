package catan.common.game.players;

import catan.common.crypto.Username;
import catan.common.event.ReversibleEventConsumer;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.teams.TeamColor;
import catan.common.game.util.EnumCounter;
import catan.common.game.util.GameResource;

import java.io.Serializable;

/**
 * Created by greg on 6/26/16.
 * Interface defining the behavior of a player in a catan game.
 */
public interface Player extends Serializable, ReversibleEventConsumer<PlayerEvent>, Trader {
    Username getName();

    TeamColor getTeamColor();

    EnumCounter<GameResource> getInventory();

    EnumCounter<DevelopmentCard> getBoughtCards();

    EnumCounter<DevelopmentCard> getDevelopmentCards();

    int getDiscardCount();

}
