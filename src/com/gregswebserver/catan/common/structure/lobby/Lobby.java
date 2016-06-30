package com.gregswebserver.catan.common.structure.lobby;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.gameplay.allocator.PreferenceTeamAllocator;
import com.gregswebserver.catan.common.game.gameplay.allocator.TeamAllocator;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.generator.better.BetterGenerator;
import com.gregswebserver.catan.common.game.gameplay.generator.copy.CopyGenerator;
import com.gregswebserver.catan.common.game.gameplay.generator.random.RandomBoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.resources.BoardLayoutInfo;
import com.gregswebserver.catan.common.resources.GameRulesInfo;
import com.gregswebserver.catan.common.resources.ResourceLoadException;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.structure.game.GameSettings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Greg on 8/22/2014.
 * A lobby containing information relevant to organizing groups of users setting up for a game.
 */
public class Lobby implements Serializable {

    private final Map<Username, LobbyUser> users;
    private final Set<Username> connected;
    private int gameID;
    private LobbyState state;
    private LobbyConfig config;

    public Lobby(LobbyConfig config) {
        this.users = new HashMap<>(config.getMaxPlayers());
        this.connected = new HashSet<>();
        setConfig(config);
    }

    public void addPlayer(Username username) {
        users.put(username, new LobbyUser());
    }

    public void removePlayer(Username username) {
        users.remove(username);
    }

    public void connect(Username username) {
        connected.add(username);
    }

    public void disconnect(Username username) {
        connected.remove(username);
    }

    public Set<Username> getPlayers() {
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
            e.printStackTrace();
            // Fall back to the text seeded map.
            boardLayout = ResourceLoader.getBoardLayout(new BoardLayoutInfo(config.getLayoutName().hashCode()));
        }

        //We really don't have an alternative than to spit the error back up.
        GameRules rules = ResourceLoader.getGameRuleSet(new GameRulesInfo(config.getRulesetName()));

        //Keyword 'copy' for selecting the copy generator
        Matcher copyGenerator = Pattern.compile("^[Cc]opy$").matcher(config.getGeneratorName());
        //Keyword 'better settlers' for selecting the better generator.
        Matcher betterGenerator = Pattern.compile("^[Bb]etter(?: )??(?:[Ss]ettlers)??$").matcher(config.getGeneratorName());
        //Choose the generator to use.
        BoardGenerator boardGenerator;
        if (copyGenerator.find())
            boardGenerator = CopyGenerator.instance;
        else if (betterGenerator.find())
            boardGenerator = BetterGenerator.instance;
        else
            boardGenerator = RandomBoardGenerator.instance;

        Map<Username, TeamColor> preferences = new HashMap<>();
        //Save all of the players that have made an explicit teamColor choice.
        for (Map.Entry<Username, LobbyUser> user : users.entrySet())
            preferences.put(user.getKey(), user.getValue().getTeamColor());

        TeamAllocator players = new PreferenceTeamAllocator(preferences);

        return new GameSettings(System.nanoTime(), boardLayout, boardGenerator, rules, players);
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
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

    public Set<Username> getConnectedPlayers() {
        return connected;
    }

    public Username getPlayer() {
        return users.keySet().iterator().next();
    }
}
