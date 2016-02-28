package com.gregswebserver.catan.common.structure.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.gameplay.enums.*;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
public class Player implements Serializable {

    private final Username name;
    private final Team team;
    private final EnumCounter<GameResource> inventory;
    private final EnumCounter<DevelopmentCard> bought;
    private final EnumCounter<DevelopmentCard> active;
    private final EnumCounter<Purchase> purchases;
    private final Set<AchievementCard> achievement;
    public Settlement lastSettlement;
    public PlayerState state;
    public TemporaryTrade trade;

    public Player(Username name, Team team) {
        this.name = name;
        this.team = team;
        inventory = new EnumCounter<>(GameResource.class);
        bought = new EnumCounter<>(DevelopmentCard.class);
        active = new EnumCounter<>(DevelopmentCard.class);
        purchases = new EnumCounter<>(Purchase.class);
        achievement = EnumSet.noneOf(AchievementCard.class);
        state = PlayerState.Settlement_1;
    }

    public Username getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public EnumCounter<GameResource> getInventory() {
        return inventory;
    }

    public Set<AchievementCard> getAchievementCards() {
        return achievement;
    }

    public boolean canMakePurchase(Purchase purchase) {
        for (GameResource r : GameResource.values())
            if (!inventory.contains(r, purchase.get(r)))
                return false;
        return true;
    }

    public void makePurchase(Purchase purchase) {
        for (GameResource r : GameResource.values())
            inventory.decrement(r, purchase.get(r));
        purchases.increment(purchase, 1);
    }

    public boolean canMakeTrade(Trade t) {
        for (GameResource r : GameResource.values())
            if (!inventory.contains(r, t.request.get(r)))
                return false;
        return true;
    }

    public void makeTrade(Trade t) {
        for (GameResource r : GameResource.values()) {
            inventory.increment(r, t.offer.get(r));
            inventory.decrement(r, t.request.get(r));
        }
    }

    public boolean canFulfillTrade(Trade t) {
        for (GameResource r : GameResource.values()) {
            if (!inventory.contains(r, t.offer.get(r)))
                return false;
        }
        return true;
    }

    public void fulfillTrade(Trade t) {
        for (GameResource r : GameResource.values()) {
            inventory.increment(r, t.request.get(r));
            inventory.decrement(r, t.offer.get(r));
        }
    }

    public void addResource(Tile tile, int count) {
        if (tile instanceof ResourceTile) {
            ResourceTile t = (ResourceTile) tile;
            if (t.getResource() != null)
            inventory.increment(t.getResource(), count);
        }
    }

    public void addDevelopmentCard(DevelopmentCard card) {
        bought.increment(card, 1);
    }

    public void endTurn() {
        for (DevelopmentCard card : DevelopmentCard.values())
            active.increment(card, bought.clear(card));
    }

    public boolean canPlayDevelopmentCard(DevelopmentCard card) {
        return active.contains(card, 1);
    }

    public void playDevelopmentCard(DevelopmentCard card) {
        active.decrement(card, 1);
    }

    public void setTrade(TemporaryTrade trade) {
        this.trade = trade;
    }

    public TemporaryTrade getTrade() {
        return trade;
    }

    public enum PlayerState {
        Settlement_1, Road_1, Waiting, Settlement_2, Road_2, Playing, Disconnected, Finished
    }
}
