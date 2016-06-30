package com.gregswebserver.catan.common.game.players;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;
import com.gregswebserver.catan.test.common.game.AssertEqualsTestable;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by greg on 6/26/16.
 * Interface defining the behavior of a player in a catan game.
 */
public interface Player extends Serializable, ReversibleEventConsumer<PlayerEvent>,AssertEqualsTestable<Player> {
    Username getName();

    TeamColor getTeamColor();

    EnumCounter<GameResource> getInventory();

    EnumCounter<DevelopmentCard> getBoughtCards();

    EnumCounter<DevelopmentCard> getDevelopmentCards();

    Set<Trade> getTrades();

    boolean canMakeTrade(Trade t);

    int getDiscardCount();

}
