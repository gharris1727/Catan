package com.gregswebserver.catan.common.structure.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.gameplay.enums.AchievementCard;
import com.gregswebserver.catan.common.game.gameplay.enums.DevelopmentCard;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;

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
    private final Map<DevelopmentCard, Integer> development;
    private final Set<AchievementCard> achievement;

    public Player(Username name, Team team) {
        this.name = name;
        this.team = team;
        inventory = new EnumMap<>(GameResource.class);
        for (GameResource r : GameResource.values())
            inventory.put(r, 0);
        development = new EnumMap<>(DevelopmentCard.class);
        for (DevelopmentCard d: DevelopmentCard.values())
            development.put(d, 0);
        achievement = EnumSet.noneOf(AchievementCard.class);
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

    public Map<DevelopmentCard, Integer> getDevelopmentCards() {
        return development;
    }

    public Set<AchievementCard> getAchievementCards() {
        return achievement;
    }
}
