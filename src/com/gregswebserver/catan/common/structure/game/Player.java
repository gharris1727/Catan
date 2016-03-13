package com.gregswebserver.catan.common.structure.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.common.game.board.paths.Road;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.City;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.gameplay.enums.*;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
public class Player implements Serializable {

    private final Username name;
    private final Team team;
    private final EnumCounter<GameResource> inventory;
    private final Stack<EnumCounter<DevelopmentCard>> bought;
    private final EnumCounter<DevelopmentCard> active;
    private final EnumCounter<Purchase> purchases;
    private final Set<AchievementCard> achievement;
    private final Stack<TemporaryTrade> trade;
    private final Stack<Settlement> settlements;
    private PlayerState state;
    private int round;

    public Player(Username name, Team team) {
        this.name = name;
        this.team = team;
        inventory = new EnumCounter<>(GameResource.class);
        bought = new Stack<>();
        active = new EnumCounter<>(DevelopmentCard.class);
        purchases = new EnumCounter<>(Purchase.class);
        achievement = EnumSet.noneOf(AchievementCard.class);
        trade = new Stack<>();
        trade.push(null);
        settlements = new Stack<>();
        settlements.push(null);
        state = PlayerState.Settlement_1;
        round = 0;
        advanceTurn();
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

    private boolean canMakePurchase(Purchase purchase) {
        for (GameResource r : GameResource.values())
            if (!inventory.contains(r, purchase.get(r)))
                return false;
        return true;
    }

    private void makePurchase(Purchase purchase) {
        for (GameResource r : GameResource.values())
            inventory.decrement(r, purchase.get(r));
        purchases.increment(purchase, 1);
    }

    private void undoPurchase(Purchase purchase) {
        for (GameResource r : GameResource.values())
            inventory.increment(r, purchase.get(r));
        purchases.decrement(purchase, 1);
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

    public void undoTrade(Trade t) {
        for (GameResource r : GameResource.values()) {
            inventory.decrement(r, t.offer.get(r));
            inventory.increment(r, t.request.get(r));
        }
    }

    public boolean canFulfillTrade(TemporaryTrade t) {
        for (GameResource r : GameResource.values()) {
            if (!inventory.contains(r, t.offer.get(r)))
                return false;
        }
        return true;
    }

    public void fulfillTrade(TemporaryTrade t) {
        for (GameResource r : GameResource.values()) {
            inventory.increment(r, t.request.get(r));
            inventory.decrement(r, t.offer.get(r));
        }
    }

    public void undoFulfillTrade(TemporaryTrade t) {
        for (GameResource r : GameResource.values()) {
            inventory.decrement(r, t.request.get(r));
            inventory.increment(r, t.offer.get(r));
        }
    }

    public void addResource(Tile tile, int count) {
        if (tile instanceof ResourceTile) {
            ResourceTile t = (ResourceTile) tile;
            if (t.getResource() != null)
            inventory.increment(t.getResource(), count);
        }
    }

    public void removeResource(Tile tile, int count) {
        if (tile instanceof ResourceTile) {
            ResourceTile t = (ResourceTile) tile;
            if (t.getResource() != null)
                inventory.decrement(t.getResource(), count);
        }
    }

    public void buyDevelopmentCard(DevelopmentCard card) {
        makePurchase(Purchase.DevelopmentCard);
        bought.peek().increment(card, 1);
    }

    public void undoBuyDevelopmentCard(DevelopmentCard card) {
        bought.peek().decrement(card, 1);
    }

    public boolean canAdvanceTurn() {
        return state == PlayerState.Waiting || state == PlayerState.FirstTurn || state == PlayerState.Playing;
    }

    public void advanceTurn() {
        if (!bought.isEmpty())
            for (DevelopmentCard card : DevelopmentCard.values())
                active.increment(card, bought.peek().get(card));
        bought.push(new EnumCounter<>(DevelopmentCard.class));
        if (state == PlayerState.Waiting)
            state = PlayerState.Settlement_2;
        if (state == PlayerState.FirstTurn) {
            HexagonalArray hexArray = settlements.peek().getHexArray();
            Coordinate position = settlements.peek().getPosition();
            for (Coordinate space : hexArray.getAdjacentSpacesFromVertex(position).values())
                addResource(hexArray.getTile(space),1);
            state = PlayerState.Playing;
        } else {
            round++;
        }
    }

    public void undoAdvanceTurn() {
        if (!bought.isEmpty())
            for (DevelopmentCard card : DevelopmentCard.values())
                active.decrement(card, bought.peek().get(card));
        bought.pop();
        if (state == PlayerState.Settlement_2)
            state = PlayerState.Waiting;
        if (state == PlayerState.Playing && round == 0) {
            HexagonalArray hexArray = settlements.peek().getHexArray();
            Coordinate position = settlements.peek().getPosition();
            for (Coordinate space : hexArray.getAdjacentSpacesFromVertex(position).values())
                removeResource(hexArray.getTile(space),1);
            state = PlayerState.FirstTurn;
        } else {
            round--;
        }
    }

    private boolean canPlayDevelopmentCard(DevelopmentCard card) {
        return active.contains(card, 1);
    }

    private void playDevelopmentCard(DevelopmentCard card) {
        active.decrement(card, 1);
    }

    private void undoPlayDevelopmentCard(DevelopmentCard card) {
        active.increment(card, 1);
    }

    public void offerTrade(TemporaryTrade trade) {
        this.trade.push(trade);
    }

    public void undoOfferTrade() {
        this.trade.pop();
    }

    public TemporaryTrade getTrade() {
        return trade.peek();
    }

    public boolean canBuildSettlement() {
        return state == PlayerState.Settlement_1 ||
                state == PlayerState.Settlement_2 ||
                canMakePurchase(Purchase.Settlement);
    }

    public void buildSettlement(Settlement settlement) {
        settlements.push(settlement);
        if (state == PlayerState.Settlement_1)
            state = PlayerState.Road_1;
        if (state == PlayerState.Settlement_2)
            state = PlayerState.Road_2;
        if (state == PlayerState.Playing)
            makePurchase(Purchase.Settlement);
    }

    public void undoBuildSettlement() {
        settlements.pop();
        if (state == PlayerState.Road_1)
            state = PlayerState.Settlement_1;
        if (state == PlayerState.Road_2)
            state = PlayerState.Settlement_2;
        if (state == PlayerState.Playing)
            undoPurchase(Purchase.Settlement);
    }

    public boolean canBuildCity() {
        return canMakePurchase(Purchase.City);
    }

    public void buildCity(City city) {
        makePurchase(Purchase.City);
    }

    public void undoBuildCity() {
        undoPurchase(Purchase.City);
    }

    public boolean canBuildRoad() {
        return state == PlayerState.Road_1 ||
                state == PlayerState.Road_2 ||
                canMakePurchase(Purchase.Road);
    }

    public void buildRoad(Road road) {
        if (state == PlayerState.Road_1)
            state = PlayerState.Waiting;
        if (state == PlayerState.Road_2)
            state = PlayerState.FirstTurn;
        if (state == PlayerState.Playing)
            makePurchase(Purchase.Road);
    }

    public void undoBuildRoad() {
        if (state == PlayerState.Waiting)
            state = PlayerState.Road_1;
        if (state == PlayerState.FirstTurn)
            state = PlayerState.Road_2;
        if (state == PlayerState.Playing)
            undoPurchase(Purchase.Road);
    }

    public boolean canMoveRobber() {
        return canPlayDevelopmentCard(DevelopmentCard.Knight);
    }

    public void moveRobber() {
        playDevelopmentCard(DevelopmentCard.Knight);
    }

    public void undoMoveRobber() {
        undoPlayDevelopmentCard(DevelopmentCard.Knight);
    }

    public boolean canBuyDevelopmentCard() {
        return canMakePurchase(Purchase.DevelopmentCard);
    }

    public enum PlayerState {
        Settlement_1, Road_1, Waiting, Settlement_2, Road_2, FirstTurn, Playing, Disconnected, Finished
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (!name.equals(player.name)) return false;
        if (team != player.team) return false;
        if (!inventory.equals(player.inventory)) return false;
        if (!bought.equals(player.bought)) return false;
        if (!active.equals(player.active)) return false;
        if (!purchases.equals(player.purchases)) return false;
        if (!achievement.equals(player.achievement)) return false;
        if (!trade.equals(player.trade)) return false;
        if (!settlements.equals(player.settlements)) return false;
        return state == player.state;

    }

    @Override
    public String toString() {
        return "Player{" +
                "name=" + name +
                ", team=" + team +
                ", inventory=" + inventory +
                ", bought=" + bought +
                ", active=" + active +
                ", purchases=" + purchases +
                ", achievement=" + achievement +
                ", trade=" + trade +
                ", settlements=" + settlements +
                ", state=" + state +
                '}';
    }
}
