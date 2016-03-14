package com.gregswebserver.catan.common.structure.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;

import java.util.*;

/**
 * Created by greg on 2/5/16.
 * A configuration of users and teams that are participating in a catan game.
 */
public class PlayerPool {

    private final Map<Username, Player> players;
    private final Map<Team, List<Player>> teams;

    public PlayerPool(Map<Username, Team> playerTeams) {
        this.players = new HashMap<>();
        this.teams = new EnumMap<>(Team.class);
        for (Map.Entry<Username, Team> playerTeam : playerTeams.entrySet()) {
            Player player = new Player(playerTeam.getKey(), playerTeam.getValue());
            List<Player> userList = teams.get(playerTeam.getValue());
            if (userList == null)
                userList = new ArrayList<>();
            userList.add(player);
            teams.put(playerTeam.getValue(), userList);
            players.put(playerTeam.getKey(), player);
        }
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

    @Override
    public String toString() {
        return "PlayerPool(" + players + ")";
    }

    public Set<Team> getTeamSet() {
        Set<Team> set = EnumSet.noneOf(Team.class);
        for (Player p : players.values())
            set.add(p.getTeam());
        return set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerPool that = (PlayerPool) o;

        if (!players.equals(that.players)) return false;
        return teams.equals(that.teams);

    }
}
