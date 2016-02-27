package com.gregswebserver.catan.common.structure.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.gameplay.enums.*;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
public class Player implements Serializable {

    private final Username name;
    private final Team team;
    private final Map<GameResource, Integer> inventory;
    private final Map<DevelopmentCard, Integer> bought;
    private final Map<DevelopmentCard, Integer> active;
    private final Set<AchievementCard> achievement;
    public Settlement lastSettlement;
    public PlayerState state;

    public Player(Username name, Team team) {
        this.name = name;
        this.team = team;
        inventory = new EnumMap<>(GameResource.class);
        for (GameResource r : GameResource.values())
            inventory.put(r, 0);
        bought = new EnumMap<>(DevelopmentCard.class);
        for (DevelopmentCard d: DevelopmentCard.values())
            bought.put(d, 0);
        active = new EnumMap<>(DevelopmentCard.class);
        for (DevelopmentCard d: DevelopmentCard.values())
            active.put(d, 0);
        achievement = EnumSet.noneOf(AchievementCard.class);
        state = PlayerState.Settlement_1;
    }

    public Username getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public Map<GameResource, Integer> getInventory() {
        return inventory;
    }

    public Set<AchievementCard> getAchievementCards() {
        return achievement;
    }

    public boolean canMakePurchase(Purchase purchase) {
        switch (purchase) {
            case Road:
                return
                    inventory.get(GameResource.Brick) >= 1 &&
                    inventory.get(GameResource.Lumber) >= 1;
            case Settlement:
                return
                    inventory.get(GameResource.Brick) >= 1 &&
                    inventory.get(GameResource.Lumber) >= 1 &&
                    inventory.get(GameResource.Grain) >= 1 &&
                    inventory.get(GameResource.Wool) >= 1;
            case City:
                return
                    inventory.get(GameResource.Grain) >= 3 &&
                    inventory.get(GameResource.Ore) >= 2;
            case DevelopmentCard:
                return
                    inventory.get(GameResource.Wool) >= 1 &&
                    inventory.get(GameResource.Grain) >= 1 &&
                    inventory.get(GameResource.Ore) >= 1;
            default:
                return false;
        }
    }

    public void makePurchase(Purchase purchase) {
        switch (purchase) {
            case Road:
                inventory.put(GameResource.Brick, inventory.get(GameResource.Brick) - 1);
                inventory.put(GameResource.Lumber, inventory.get(GameResource.Lumber) - 1);
                break;
            case Settlement:
                inventory.put(GameResource.Brick, inventory.get(GameResource.Brick) - 1);
                inventory.put(GameResource.Lumber, inventory.get(GameResource.Lumber) - 1);
                inventory.put(GameResource.Grain, inventory.get(GameResource.Grain) - 1);
                inventory.put(GameResource.Wool, inventory.get(GameResource.Wool) - 1);
                break;
            case City:
                inventory.put(GameResource.Grain, inventory.get(GameResource.Grain) - 3);
                inventory.put(GameResource.Ore, inventory.get(GameResource.Ore) - 2);
                break;
            case DevelopmentCard:
                inventory.put(GameResource.Wool, inventory.get(GameResource.Wool) - 1);
                inventory.put(GameResource.Grain, inventory.get(GameResource.Grain) - 1);
                inventory.put(GameResource.Ore, inventory.get(GameResource.Ore) - 1);
                break;
        }
    }

    public void addResource(Tile tile, int count) {
        if (tile instanceof ResourceTile) {
            ResourceTile t = (ResourceTile) tile;
            if (t.getResource() != null)
            inventory.put(t.getResource(), inventory.get(t.getResource()) + count);
        }
    }

    public void addDevelopmentCard(DevelopmentCard card) {
        bought.put(card, bought.get(card) + 1);
    }

    public void endTurn() {
        for (DevelopmentCard card : DevelopmentCard.values()) {
            active.put(card, active.get(card) + bought.get(card));
            bought.put(card, 0);
        }
    }

    public boolean canPlayDevelopmentCard(DevelopmentCard card) {
        return active.get(card) > 0;
    }

    public void playDevelopmentCard(DevelopmentCard card) {
        active.put(card, active.get(card) - 1);
    }

    public enum PlayerState {
        Settlement_1, Road_1, Waiting, Settlement_2, Road_2, Playing, Disconnected, Finished
    }
}
