package catan.common.game;

import catan.common.crypto.Username;
import catan.common.game.gameplay.trade.Trade;
import catan.common.game.gameplay.trade.TradingPostType;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.players.Player;
import catan.common.game.teams.TeamColor;
import catan.common.game.util.GameResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by greg on 12/20/16.
 * Observer for details of a game relevant to a single player.
 */
public class PlayerObserver {

    private final CatanGame game;
    private final Player player;
    private final Username username;

    PlayerObserver(CatanGame game, Username username) {
        this.game = game;
        synchronized (game) {
            this.player = game.players.getPlayer(username);
        }
        this.username = username;
    }

    public Username getUsername() {
        return username;
    }

    public List<Trade> getTrades() {
        synchronized (game) {
            List<Trade> trades = new ArrayList<>();
            for (Username u : game.players) {
                for (Trade trade : game.players.getPlayer(u).getTrades())
                    if (player.canMakeTrade(trade))
                        trades.add(trade);
            }
            for (TradingPostType type : game.board.getTrades(player.getTeamColor())) {
                for (Trade trade : type.getTrades())
                    if (player.canMakeTrade(trade))
                        trades.add(trade);
            }
            Collections.sort(trades);
            return trades;
        }
    }

    public Username getName() {
        synchronized (game) {
            return player.getName();
        }
    }

    public TeamColor getTeamColor() {
        synchronized (game) {
            return player.getTeamColor();
        }
    }

    public void eachResource(Consumer<GameResource> action) {
        synchronized (game) {
            player.getInventory().forEach(action);
        }
    }

    public void eachDevelopment(Consumer<DevelopmentCard> action) {
        synchronized (game) {
            player.getDevelopmentCards().forEach(action);
        }
    }
}
