package com.gregswebserver.catan.common.game.gameplay.allocator;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.teams.TeamColor;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by greg on 6/29/16.
 * A class that allocates users to teams.
 */
public interface TeamAllocator extends Serializable {

    Set<Username> getUsers();

    Set<TeamColor> getTeams();

    Map<Username, TeamColor> getPlayerTeams();

    Map<TeamColor, Set<Username>> getTeamUsers();
}
