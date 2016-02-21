package com.gregswebserver.catan.common.structure.lobby;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.generator.better.BetterGenerator;
import com.gregswebserver.catan.common.game.gameplay.generator.copy.CopyGenerator;
import com.gregswebserver.catan.common.game.gameplay.generator.random.RandomBoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;
import com.gregswebserver.catan.common.game.gameplay.rules.GameRules;
import com.gregswebserver.catan.common.resources.BoardLayoutInfo;
import com.gregswebserver.catan.common.resources.GameRulesInfo;
import com.gregswebserver.catan.common.resources.ResourceLoadException;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.common.structure.game.Player;
import com.gregswebserver.catan.common.structure.game.PlayerPool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Greg on 8/22/2014.
 * A lobby containing information relevant to organizing groups of users setting up for a game.
 */
public class Lobby {

    private final Map<Username, LobbyUser> users;
    private final Username local;
    private LobbyState state;
    private LobbyConfig config;

    public Lobby(LobbyConfig config, Username local) {
        this.users = new HashMap<>(config.getMaxPlayers());
        this.local = local;
        state = LobbyState.Preparing;
        setConfig(config);
    }

    public void add(Username username) {
        users.put(username, new LobbyUser());
    }

    public void remove(Username username) {
        users.remove(username);
    }

    public Set<Username> getUsers() {
        return users.keySet();
    }

    public LobbyConfig getConfig() {
        return config;
    }

    public void setConfig(LobbyConfig config) {
        this.config = config;
    }

    public int size() {
        return users.size();
    }

    public GameSettings getGameSettings() {
        //TODO: error handling and reporting errors to the user.

        //We need to construct the parts of a GameSettings object.
        BoardLayout boardLayout;
        GameRules gameRules;
        BoardGenerator boardGenerator;
        PlayerPool playerPool;
        try {
            BoardLayoutInfo info = null;
            //TODO: put the regex stuff somewhere so that we can use it to validate when saving the game.
            //Static map file name, maybe having .properties appended (ignored).
            Matcher staticConfigName = Pattern.compile("^(?<name>[a-zA-Z]+)(?:\\.properties)??$").matcher(config.getLayoutName());
            //Dynamic numeric seed that consists of "dynamic.<number>" where the number is the actual seed.
            Matcher dynamicNumericSeed = Pattern.compile("^\\d+$").matcher(config.getLayoutName());
            // Each of these options are mutually exclusive, and can fail for whatever reason.
            if (staticConfigName.find())
                info = new BoardLayoutInfo(staticConfigName.group("name"));
            else if (dynamicNumericSeed.find()) // Number specifies a manual seed.
                info = new BoardLayoutInfo(Long.parseLong(config.getLayoutName()));
            else if ("".equals(config.getLayoutName())) // Empty string triggers random seed.
                info = new BoardLayoutInfo(System.nanoTime());
            boardLayout = ResourceLoader.getBoardLayout(info);
        } catch (ResourceLoadException e) {
            // Fall back to the text seeded map.
            boardLayout = ResourceLoader.getBoardLayout(new BoardLayoutInfo(config.getLayoutName().hashCode()));
        }

        //We really don't have an alternative than to spit the error back up.
        gameRules = ResourceLoader.getGameRuleSet(new GameRulesInfo(config.getRulesetName()));

        //Keyword 'copy' for selecting the copy generator
        Matcher copyGenerator = Pattern.compile("^[Cc]opy$").matcher(config.getGeneratorName());
        //Keyword 'better settlers' for selecting the better generator.
        Matcher betterGenerator = Pattern.compile("^[Bb]etter(?: )??(?:[Ss]ettlers)??$").matcher(config.getGeneratorName());
        //Choose the generator to use.
        if (copyGenerator.find())
            boardGenerator = CopyGenerator.instance;
        else if (betterGenerator.find())
            boardGenerator = BetterGenerator.instance;
        else
            boardGenerator = RandomBoardGenerator.instance;

        //Keep track which teams people have selected so that they are not randomly allocated later.
        Set<Team> teamsToAllocate = Team.getTeamSet();
        Map<Username, Player> players = new HashMap<>();
        //Create all of the players that have made an explicit team choice.
        for (Map.Entry<Username, LobbyUser> user : users.entrySet()) {
            Team team = user.getValue().getTeam();
            if (team != Team.None) {
                Username username = user.getKey();
                teamsToAllocate.remove(team);
                players.put(username,new Player(username, team));
            }
        }
        //Create all of the players that didn't explicitly select a team, allocating them one manually.
        Iterator<Team> teamAllocator = teamsToAllocate.iterator();
        for (Map.Entry<Username, LobbyUser> user : users.entrySet()) {
            Team team = user.getValue().getTeam();
            if (team == Team.None) {
                //If we exhausted all the teams, then refresh the list and keep going.
                if (!teamAllocator.hasNext()) {
                    teamsToAllocate = Team.getTeamSet();
                    teamAllocator = teamsToAllocate.iterator();
                }
                team = teamAllocator.next();
                Username username = user.getKey();
                teamAllocator.remove();
                players.put(username,new Player(username, team));
            }
        }
        playerPool = new PlayerPool(local, players);

        return new GameSettings(System.nanoTime(), boardLayout, boardGenerator, gameRules, playerPool);
    }

    public String toString() {
        return "Lobby " + config;
    }

    public void setState(LobbyState state) {
        this.state = state;
    }

    public LobbyState getState() {
        return state;
    }
}