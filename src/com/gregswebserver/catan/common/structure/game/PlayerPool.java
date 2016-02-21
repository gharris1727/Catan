package com.gregswebserver.catan.common.structure.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;

import java.io.Serializable;
import java.util.*;

/**
 * Created by greg on 2/5/16.
 * A configuration of users and teams that are participating in a catan game.
 */
public class PlayerPool implements Serializable {

    private transient Username local;
    private final Map<Username, Player> players;
    private final Map<Team, List<Player>> teams;

    public PlayerPool(Username local, Map<Username, Player> players) {
        this.local = local;
        this.players = players;
        teams = new EnumMap<>(Team.class);
        for (Player player : players.values()){
            List<Player> userList = teams.get(player.getTeam());
            if (userList == null)
                userList = new ArrayList<>();
            userList.add(player);
            teams.put(player.getTeam(), userList);
        }
    }

    public void setLocal(Username local) {
        this.local = local;
    }

    public Set<Username> getAllUsers() {
        return players.keySet();
    }

    public Player getPlayer(Username origin) {
        return players.get(origin);
    }

    public List<Player> getTeamPlayers(Team team) {
        return teams.get(team);
    }

    public Player getLocalPlayer() {
        return players.get(local);
    }

    @Override
    public String toString() {
        return "PlayerPool(" + local + "/" + players + ")";
    }

    public Set<Team> getTeamSet() {
        Set<Team> set = EnumSet.noneOf(Team.class);
    for (Player p : players.values())
            set.add(p.getTeam());
        return set;
    }
}
